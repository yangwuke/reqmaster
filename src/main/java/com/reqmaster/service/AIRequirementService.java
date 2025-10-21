package com.reqmaster.service;

import com.reqmaster.entity.Project;
import com.reqmaster.entity.Requirement;
import com.reqmaster.exception.BusinessException;
import com.reqmaster.repository.ProjectRepository;
import com.reqmaster.repository.RequirementRepository;
import com.reqmaster.service.llm.LLMService;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.reqmaster.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.io.IOException;

/**
 * 智能需求服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AIRequirementService {

    private final RequirementRepository requirementRepository;
    private final ProjectRepository projectRepository;
    private final DocumentParserService documentParserService;
    private final LLMService llmService;

    /**
     * 智能解析文档并创建需求
     */
    @Transactional
    public Requirement parseDocumentAndCreateRequirements(MultipartFile file, Long projectId) {
        try {
            // 验证文件类型
            if (!documentParserService.isSupportedFileType(file.getOriginalFilename())) {
                throw new BusinessException("不支持的文件类型，请上传PDF、Word或文本文件");
            }

            // 查找项目
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new BusinessException("项目不存在: " + projectId));

            // 解析文档内容
            String content = documentParserService.parseDocument(file);
            log.info("文档解析成功，内容长度: {}", content.length());

            // 使用大模型分析文档
            Map<String, Object> analysisResult = llmService.parseDocument(content);
            log.info("大模型分析完成，结果类型: {}", analysisResult.keySet());

            // 创建主需求记录
            Requirement mainRequirement = new Requirement();
            mainRequirement.setTitle("从文档解析的需求集合 - " + file.getOriginalFilename());
            mainRequirement.setDescription("通过AI解析文档自动生成的需求集合");
            mainRequirement.setProject(project);
            mainRequirement.setSourceType("DOCUMENT");
            mainRequirement.setIsAnalyzed(true);

            // 存储分析结果到metadata（原单行代码删除，替换为以下try-catch块）
            try {
                mainRequirement.setMetadata(JsonUtil.toJson(analysisResult));
            } catch (Exception e) {
                log.error("JSON序列化失败", e);
                throw new BusinessException("需求元数据序列化失败: " + e.getMessage());
            }

            // 存储分析结果到metadata
            // 注释掉重复的代码，避免重复执行
            // mainRequirement.setMetadata(JsonUtil.toJson(analysisResult));

            Requirement savedRequirement = requirementRepository.save(mainRequirement);
            log.info("创建主需求成功: {}", savedRequirement.getTitle());

            return savedRequirement;

        } catch (IOException e) {
            log.error("文档解析失败", e);
            throw new BusinessException("文档解析失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("智能需求分析失败", e);
            throw new BusinessException("智能需求分析失败: " + e.getMessage());
        }
    }

    /**
     * 生成用户故事
     */
    public List<LLMService.UserStory> generateUserStories(Long requirementId) {
        Requirement requirement = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new BusinessException("需求不存在: " + requirementId));

        try {
            List<LLMService.UserStory> userStories = llmService.generateUserStories(requirement);
            log.info("为用户故事生成成功，数量: {}", userStories.size());
            return userStories;
        } catch (Exception e) {
            log.error("生成用户故事失败", e);
            throw new BusinessException("生成用户故事失败: " + e.getMessage());
        }
    }

    /**
     * 检查需求一致性
     */
    public List<LLMService.RequirementIssue> checkConsistency(Long projectId) {
        List<Requirement> requirements = requirementRepository.findByProjectId(projectId);

        if (requirements.isEmpty()) {
            throw new BusinessException("项目中没有需求可供分析");
        }

        try {
            List<LLMService.RequirementIssue> issues = llmService.checkConsistency(requirements);
            log.info("一致性检查完成，发现问题: {}", issues.size());
            return issues;
        } catch (Exception e) {
            log.error("需求一致性检查失败", e);
            throw new BusinessException("需求一致性检查失败: " + e.getMessage());
        }
    }

    /**
     * 分析需求完整性
     */
    public LLMService.CompletenessAnalysis analyzeCompleteness(Long requirementId) {
        Requirement requirement = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new BusinessException("需求不存在: " + requirementId));

        try {
            LLMService.CompletenessAnalysis analysis = llmService.analyzeCompleteness(requirement);
            log.info("完整性分析完成，得分: {}", analysis.score);
            return analysis;
        } catch (Exception e) {
            log.error("需求完整性分析失败", e);
            throw new BusinessException("需求完整性分析失败: " + e.getMessage());
        }
    }

    /**
     * 智能对话分析需求
     */
    public String chatAnalysis(String question, Long projectId) {
        List<Requirement> requirements = requirementRepository.findByProjectId(projectId);

        if (requirements.isEmpty()) {
            throw new BusinessException("项目中没有需求可供分析");
        }

        // 构建需求上下文
        StringBuilder context = new StringBuilder();
        context.append("以下是项目的需求信息：\\n");
        for (Requirement req : requirements) {
            context.append("- ").append(req.getTitle())
                    .append(": ").append(req.getDescription())
                    .append(" [").append(req.getType()).append("]\\n");
        }
        context.append("\\n问题：").append(question);

        try {
            String response = llmService.chatCompletion(context.toString(), 0.5);
            log.info("智能对话分析完成");
            return response;
        } catch (Exception e) {
            log.error("智能对话分析失败", e);
            throw new BusinessException("智能对话分析失败: " + e.getMessage());
        }
    }
}