package com.reqmaster.controller;

import com.reqmaster.dto.ApiResponse;
import com.reqmaster.entity.ChatMessage;
import com.reqmaster.entity.ChatSession;
import com.reqmaster.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 对话控制器
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "智能对话", description = "需求访谈对话管理")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/sessions")
    @Operation(summary = "创建对话会话")
    public ResponseEntity<ApiResponse<ChatSession>> createSession(
            @RequestBody CreateSessionRequest request) {
        ChatSession session = chatService.createSession(
                request.getProjectId(),
                request.getTitle(),
                request.getSessionType()
        );
        return ResponseEntity.ok(ApiResponse.success("会话创建成功", session));
    }

    @PostMapping("/sessions/{sessionId}/messages")
    @Operation(summary = "发送消息")
    public ResponseEntity<ApiResponse<ChatMessage>> sendMessage(
            @PathVariable Long sessionId,
            @RequestBody SendMessageRequest request) {
        ChatMessage response = chatService.sendMessage(sessionId, request.getMessage());
        return ResponseEntity.ok(ApiResponse.success("消息发送成功", response));
    }

    @GetMapping("/sessions/{sessionId}/messages")
    @Operation(summary = "获取会话消息")
    public ResponseEntity<ApiResponse<List<ChatMessage>>> getSessionMessages(
            @PathVariable Long sessionId) {
        List<ChatMessage> messages = chatService.getSessionMessages(sessionId);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    @GetMapping("/projects/{projectId}/sessions")
    @Operation(summary = "获取项目对话会话")
    public ResponseEntity<ApiResponse<List<ChatSession>>> getProjectSessions(
            @PathVariable Long projectId) {
        List<ChatSession> sessions = chatService.getProjectSessions(projectId);
        return ResponseEntity.ok(ApiResponse.success(sessions));
    }

    @DeleteMapping("/sessions/{sessionId}")
    @Operation(summary = "删除对话会话")
    public ResponseEntity<ApiResponse<Void>> deleteSession(@PathVariable Long sessionId) {
        chatService.deleteSession(sessionId);
        return ResponseEntity.ok(ApiResponse.success("会话删除成功", null));
    }

    @PutMapping("/sessions/{sessionId}/title")
    @Operation(summary = "更新会话标题")
    public ResponseEntity<ApiResponse<ChatSession>> updateSessionTitle(
            @PathVariable Long sessionId,
            @RequestBody Map<String, String> request) {
        String newTitle = request.get("title");
        ChatSession session = chatService.updateSessionTitle(sessionId, newTitle);
        return ResponseEntity.ok(ApiResponse.success("标题更新成功", session));
    }

    @PostMapping("/sessions/{sessionId}/summary")
    @Operation(summary = "生成会话摘要")
    public ResponseEntity<ApiResponse<Void>> generateSummary(@PathVariable Long sessionId) {
        // 这里需要获取会话并调用生成摘要的方法
        // 简化实现，实际可以异步处理
        return ResponseEntity.ok(ApiResponse.success("摘要生成任务已提交", null));
    }

    // 请求DTO类
    public static class CreateSessionRequest {
        private Long projectId;
        private String title;
        private String sessionType = "REQUIREMENT_ANALYSIS";

        // getters and setters
        public Long getProjectId() { return projectId; }
        public void setProjectId(Long projectId) { this.projectId = projectId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getSessionType() { return sessionType; }
        public void setSessionType(String sessionType) { this.sessionType = sessionType; }
    }

    public static class SendMessageRequest {
        private String message;

        // getters and setters
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}