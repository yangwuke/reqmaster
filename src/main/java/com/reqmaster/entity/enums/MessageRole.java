package com.reqmaster.entity.enums;

/**
 * 消息角色枚举
 */
public enum MessageRole {
    USER("用户"),
    ASSISTANT("助手"),
    SYSTEM("系统");

    private final String description;

    MessageRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}