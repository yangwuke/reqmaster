package com.reqmaster.entity;

import com.reqmaster.entity.enums.MessageRole;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 对话消息实体
 */
@Data
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession chatSession;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageRole role;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "token_count")
    private Integer tokenCount;

    @Column(columnDefinition = "JSON")
    private String metadata; // 存储分析结果、建议等

    @CreationTimestamp
    @Column(name = "timestamp", updatable = false)
    private LocalDateTime timestamp;

    // 业务方法
    public boolean isUserMessage() {
        return MessageRole.USER.equals(role);
    }

    public boolean isAssistantMessage() {
        return MessageRole.ASSISTANT.equals(role);
    }
}