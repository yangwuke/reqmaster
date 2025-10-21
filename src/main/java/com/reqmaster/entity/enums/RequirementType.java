package com.reqmaster.entity.enums;

/**
 * 需求类型枚举
 */
public enum RequirementType {
    // 枚举项：键值 + 中文描述
    FUNCTIONAL("功能性需求"),
    NON_FUNCTIONAL("非功能性需求"),
    BUSINESS_RULE("业务规则"),
    CONSTRAINT("约束条件"),
    USER_STORY("用户故事"),
    USE_CASE("用例"),
    UNKNOWN("未知类型");

    // 枚举描述字段
    private final String description;

    // 构造方法（枚举构造方法默认private，可省略）
    RequirementType(String description) {
        this.description = description;
    }

    // Getter方法：获取枚举描述
    public String getDescription() {
        return description;
    }
}