package com.reqmaster.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 对话会话实体
 */
@Data
@Entity
@Table(name = "chat_sessions")
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "session_type", length = 20)
    private String sessionType; // REQUIREMENT_ANALYSIS, DOCUMENT_PARSING, etc.

    @Column(columnDefinition = "TEXT")
    private String summary; // 会话摘要

    @Column(name = "message_count")
    private Integer messageCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "chatSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("timestamp ASC")
    private List<ChatMessage> messages = new ArrayList<>();

    // 业务方法
    public void addMessage(ChatMessage message) {
        messages.add(message);
        message.setChatSession(this);
        this.messageCount = messages.size();
    }

    public void updateSummary(String summary) {
        this.summary = summary;
        this.updatedAt = LocalDateTime.now();
    }
}