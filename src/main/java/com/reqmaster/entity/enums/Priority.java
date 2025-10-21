package com.reqmaster.entity.enums;

/**
 * 优先级枚举
 */
public enum Priority {
    HIGH("高"),
    MEDIUM("中"),
    LOW("低"),
    CRITICAL("紧急");

    private final String description;

    Priority(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}