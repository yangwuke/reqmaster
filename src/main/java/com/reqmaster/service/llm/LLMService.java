package com.reqmaster.service.llm;

import com.reqmaster.entity.Requirement;

import java.util.List;
import java.util.Map;

/**
 * 大模型服务接口
 */
public interface LLMService {

    /**
     * 对话补全
     */
    String chatCompletion(String prompt, Double temperature);

    /**
     * 解析文档内容
     */
    Map<String, Object> parseDocument(String content);

    /**
     * 生成用户故事
     */
    List<UserStory> generateUserStories(Requirement requirement);

    /**
     * 检查需求一致性
     */
    List<RequirementIssue> checkConsistency(List<Requirement> requirements);

    /**
     * 分析需求完整性
     */
    CompletenessAnalysis analyzeCompleteness(Requirement requirement);

    /**
     * 获取服务提供商名称
     */
    String getProviderName();

    // 内部数据类
    class UserStory {
        public String role;
        public String goal;
        public String benefit;
        public List<String> acceptanceCriteria;
    }

    class RequirementIssue {
        public String type; // CONFLICT, DUPLICATE, INCOMPLETE, AMBIGUOUS
        public String description;
        public String suggestion;
        public List<String> relatedRequirements;
    }

    class CompletenessAnalysis {
        public Double score; // 0-1
        public List<String> missingElements;
        public List<String> suggestions;
        public String summary;
    }
}