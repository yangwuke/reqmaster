package com.reqmaster.service;

import com.reqmaster.entity.ChatMessage;
import com.reqmaster.entity.ChatSession;
import com.reqmaster.entity.Project;
import com.reqmaster.entity.enums.MessageRole;
import com.reqmaster.exception.BusinessException;
import com.reqmaster.repository.ChatMessageRepository;
import com.reqmaster.repository.ChatSessionRepository;
import com.reqmaster.repository.ProjectRepository;
import com.reqmaster.service.llm.LLMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对话服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ProjectRepository projectRepository;
    private final LLMService llmService;

    /**
     * 创建新的对话会话
     */
    @Transactional
    public ChatSession createSession(Long projectId, String title, String sessionType) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException("项目不存在: " + projectId));

        ChatSession session = new ChatSession();
        session.setTitle(title);
        session.setProject(project);
        session.setSessionType(sessionType);
        session.setMessageCount(0);

        ChatSession savedSession = chatSessionRepository.save(session);
        log.info("创建对话会话成功: {}", savedSession.getTitle());

        return savedSession;
    }

    /**
     * 发送消息并获取AI回复
     */
    @Transactional
    public ChatMessage sendMessage(Long sessionId, String userMessage) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException("对话会话不存在: " + sessionId));

        // 保存用户消息
        ChatMessage userMsg = new ChatMessage();
        userMsg.setChatSession(session);
        userMsg.setRole(MessageRole.USER);
        userMsg.setContent(userMessage);
        userMsg.setTimestamp(LocalDateTime.now());

        chatMessageRepository.save(userMsg);

        // 构建对话历史
        String conversationHistory = buildConversationHistory(session);
        String prompt = buildChatPrompt(conversationHistory, userMessage, session.getProject());

        // 调用大模型获取回复
        String aiResponse;
        try {
            aiResponse = llmService.chatCompletion(prompt, 0.7);
        } catch (Exception e) {
            log.error("大模型调用失败", e);
            aiResponse = "抱歉，我现在无法处理您的请求。请稍后再试。";
        }

        // 保存AI回复
        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setChatSession(session);
        aiMsg.setRole(MessageRole.ASSISTANT);
        aiMsg.setContent(aiResponse);
        aiMsg.setTimestamp(LocalDateTime.now());

        chatMessageRepository.save(aiMsg);

        // 更新会话信息
        session.setMessageCount(session.getMessageCount() + 2); // 用户消息 + AI回复
        session.setUpdatedAt(LocalDateTime.now());

        // 如果会话没有摘要，自动生成一个
        if (session.getSummary() == null && session.getMessageCount() >= 4) {
            generateSessionSummary(session);
        }

        chatSessionRepository.save(session);

        log.info("对话消息处理完成，会话ID: {}, 消息总数: {}", sessionId, session.getMessageCount());

        return aiMsg;
    }

    /**
     * 获取会话的所有消息
     */
    public List<ChatMessage> getSessionMessages(Long sessionId) {
        return chatMessageRepository.findByChatSessionIdOrderByTimestampAsc(sessionId);
    }

    /**
     * 获取项目的所有对话会话
     */
    public List<ChatSession> getProjectSessions(Long projectId) {
        return chatSessionRepository.findByProjectIdOrderByUpdatedAtDesc(projectId);
    }

    /**
     * 删除对话会话
     */
    @Transactional
    public void deleteSession(Long sessionId) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException("对话会话不存在: " + sessionId));

        // 先删除所有消息
        chatMessageRepository.deleteByChatSessionId(sessionId);

        // 再删除会话
        chatSessionRepository.delete(session);

        log.info("删除对话会话成功: {}", session.getTitle());
    }

    /**
     * 更新会话标题
     */
    @Transactional
    public ChatSession updateSessionTitle(Long sessionId, String newTitle) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException("对话会话不存在: " + sessionId));

        session.setTitle(newTitle);
        session.setUpdatedAt(LocalDateTime.now());

        return chatSessionRepository.save(session);
    }

    /**
     * 生成会话摘要
     */
    @Transactional
    public void generateSessionSummary(ChatSession session) {
        try {
            List<ChatMessage> messages = getSessionMessages(session.getId());
            String conversationText = buildConversationText(messages);

            String prompt = """
                请为以下需求分析对话生成一个简洁的摘要（不超过200字）：
                
                对话内容：
                %s
                
                请总结对话的主要议题、达成的共识和待解决的问题。
                """.formatted(conversationText);

            String summary = llmService.chatCompletion(prompt, 0.3);
            session.setSummary(summary);
            chatSessionRepository.save(session);

            log.info("生成会话摘要成功，会话ID: {}", session.getId());
        } catch (Exception e) {
            log.warn("生成会话摘要失败", e);
            // 摘要生成失败不影响主要功能
        }
    }

    // 辅助方法
    private String buildConversationHistory(ChatSession session) {
        List<ChatMessage> messages = getSessionMessages(session.getId());
        return buildConversationText(messages);
    }

    private String buildConversationText(List<ChatMessage> messages) {
        StringBuilder history = new StringBuilder();
        for (ChatMessage message : messages) {
            String roleLabel = message.isUserMessage() ? "用户" : "助手";
            history.append(roleLabel).append(": ").append(message.getContent()).append("\n\n");
        }
        return history.toString();
    }

    private String buildChatPrompt(String history, String currentMessage, Project project) {
        return """
            你是一个资深的需求分析师，正在与客户进行需求访谈。
            
            项目背景：
            - 项目名称：%s
            - 项目描述：%s
            - 项目领域：%s
            
            对话历史：
            %s
            
            用户最新问题：%s
            
            请根据以下策略进行回复：
            1. 保持专业友好的态度，用中文回复
            2. 如果需求描述模糊，用5W1H法追问细节
            3. 实时识别和总结关键业务术语和需求点
            4. 提供专业的需求分析建议
            5. 对于技术问题，给出合理的实现建议
            6. 如果涉及多个需求，帮助梳理优先级和依赖关系
            
            请开始你的回复：
            """.formatted(
                project.getName(),
                project.getDescription() != null ? project.getDescription() : "暂无详细描述",
                project.getDomain() != null ? project.getDomain() : "未指定",
                history,
                currentMessage
        );
    }
}