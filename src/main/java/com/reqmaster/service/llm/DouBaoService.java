package com.reqmaster.service.llm;

import com.reqmaster.entity.Requirement;
import com.reqmaster.service.llm.dto.DeepSeekRequest;
import com.reqmaster.service.llm.dto.DeepSeekResponse;
import com.reqmaster.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

/**
 * DeepSeek大模型服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DouBaoService implements LLMService {

    @Value("${llm.deepseek.api-key}")
    private String apiKey;

    @Value("${llm.deepseek.url}")
    private String apiUrl;

    @Value("${llm.deepseek.model}")
    private String model;

    @Value("${llm.deepseek.max-tokens}")
    private Integer maxTokens;

    @Value("${llm.deepseek.temperature}")
    private Double temperature;

    private final WebClient webClient;

    @Override
    public String chatCompletion(String prompt, Double temperature) {
        try {
            DeepSeekRequest request = new DeepSeekRequest();
            request.setModel(model);
            request.setMessages(List.of(
                    new DeepSeekRequest.Message("user", prompt)
            ));
            request.setTemperature(temperature != null ? temperature : this.temperature);
            request.setMaxTokens(maxTokens);

            DeepSeekResponse response = webClient.post()
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(DeepSeekResponse.class)
                    .block();

            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();
            } else {
                throw new RuntimeException("DeepSeek API返回空响应");
            }

        } catch (WebClientResponseException e) {
            log.error("DeepSeek API调用失败，状态码: {}, 响应: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("DeepSeek API调用失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("DeepSeek API调用异常", e);
            throw new RuntimeException("DeepSeek服务暂时不可用");
        }
    }

    @Override
    public Map<String, Object> parseDocument(String content) {
        String prompt = loadPrompt("document_parser.prompt")
                .replace("{content}", content.substring(0, Math.min(15000, content.length())));

        String response = chatCompletion(prompt, 0.1);
        return parseJsonResponse(response);
    }

    @Override
    public List<UserStory> generateUserStories(Requirement requirement) {
        String prompt = loadPrompt("user_story_generator.prompt")
                .replace("{title}", requirement.getTitle())
                .replace("{description}", requirement.getDescription())
                .replace("{type}", requirement.getType().toString());

        String response = chatCompletion(prompt, 0.3);
        return parseUserStoriesResponse(response);
    }

    @Override
    public List<RequirementIssue> checkConsistency(List<Requirement> requirements) {
        StringBuilder requirementsText = new StringBuilder();
        for (Requirement req : requirements) {
            requirementsText.append("- ").append(req.getTitle())
                    .append(": ").append(req.getDescription())
                    .append(" [").append(req.getType()).append("]\n");
        }

        String prompt = loadPrompt("consistency_checker.prompt")
                .replace("{requirements}", requirementsText.toString());

        String response = chatCompletion(prompt, 0.2);
        return parseIssuesResponse(response);
    }

    @Override
    public CompletenessAnalysis analyzeCompleteness(Requirement requirement) {
        String prompt = loadPrompt("completeness_analyzer.prompt")
                .replace("{title}", requirement.getTitle())
                .replace("{description}", requirement.getDescription())
                .replace("{type}", requirement.getType().toString());

        String response = chatCompletion(prompt, 0.2);
        return parseCompletenessResponse(response);
    }

    @Override
    public String getProviderName() {
        return "DeepSeek";
    }

    // 辅助方法
    private String loadPrompt(String promptFile) {
        try {
            // 简化实现，实际项目中可以从文件系统或数据库加载
            return getBuiltInPrompt(promptFile);
        } catch (Exception e) {
            log.error("加载Prompt文件失败: {}", promptFile, e);
            throw new RuntimeException("加载Prompt模板失败");
        }
    }

    private String getBuiltInPrompt(String promptName) {
        // 内置的Prompt模板
        switch (promptName) {
            case "document_parser.prompt":
                return """
                    请分析以下需求文档内容，并按照JSON格式输出结果：
                    
                    文档内容：
                    {content}
                    
                    请识别并分类以下内容：
                    1. functional_requirements: 系统必须完成的具体功能
                    2. non_functional_requirements: 性能、安全、可用性等要求
                    3. business_rules: 业务领域的特定规则
                    4. constraints: 技术、时间、资源等限制
                    5. stakeholders: 与系统相关的各种角色
                    
                    要求：
                    - 每个条目都要引用原文中的依据
                    - 输出格式必须是纯JSON，不要有其他文字
                    - JSON结构参考：
                    {
                      "functional_requirements": [
                        {"description": "功能描述", "source": "原文依据"}
                      ],
                      "non_functional_requirements": [
                        {"description": "非功能需求描述", "category": "性能/安全/可用性", "source": "原文依据"}
                      ],
                      "business_rules": [
                        {"description": "业务规则描述", "source": "原文依据"}
                      ],
                      "constraints": [
                        {"description": "约束描述", "source": "原文依据"}
                      ],
                      "stakeholders": [
                        {"role": "角色名称", "description": "角色描述", "source": "原文依据"}
                      ]
                    }
                    """;

            case "user_story_generator.prompt":
                return """
                    请根据以下需求信息生成用户故事：
                    
                    需求标题：{title}
                    需求描述：{description}
                    需求类型：{type}
                    
                    请生成3-5个用户故事，每个用户故事包含：
                    - 角色 (role)
                    - 目标 (goal) 
                    - 价值 (benefit)
                    - 验收标准 (acceptance_criteria，3-5条)
                    
                    输出格式要求为纯JSON数组：
                    [
                      {
                        "role": "用户角色",
                        "goal": "用户想要完成的目标",
                        "benefit": "这样做的商业价值",
                        "acceptanceCriteria": ["标准1", "标准2", "标准3"]
                      }
                    ]
                    """;

            case "consistency_checker.prompt":
                return """
                    请分析以下需求集合，检查一致性问题：
                    
                    需求列表：
                    {requirements}
                    
                    请检查以下问题：
                    1. 逻辑矛盾：是否存在相互冲突的需求描述
                    2. 重复内容：是否有多个需求在描述同一功能
                    3. 依赖关系：哪些需求之间存在依赖关系
                    4. 完整性：基于常见软件模式，检查是否遗漏了重要功能
                    
                    对于发现的问题，请按以下JSON格式输出：
                    [
                      {
                        "type": "问题类型(CONFLICT/DUPLICATE/DEPENDENCY/INCOMPLETE)",
                        "description": "问题描述",
                        "suggestion": "修改建议",
                        "relatedRequirements": ["相关需求标题1", "相关需求标题2"]
                      }
                    ]
                    """;

            case "completeness_analyzer.prompt":
                return """
                    请分析以下需求的完整性：
                    
                    需求标题：{title}
                    需求描述：{description}
                    需求类型：{type}
                    
                    请从以下维度评估：
                    1. 需求描述是否清晰明确
                    2. 是否包含必要的业务规则
                    3. 是否考虑了边界情况
                    4. 是否包含验收标准
                    5. 技术可行性
                    
                    输出格式：
                    {
                      "score": 0.85,
                      "missingElements": ["缺失元素1", "缺失元素2"],
                      "suggestions": ["改进建议1", "改进建议2"],
                      "summary": "总体评估摘要"
                    }
                    """;

            default:
                throw new IllegalArgumentException("未知的Prompt模板: " + promptName);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJsonResponse(String response) {
        try {
            // 清理响应中的非JSON内容
            String jsonContent = response.replaceAll("```json\\n?", "").replaceAll("\\n?```", "").trim();
            return JsonUtil.fromJson(jsonContent, Map.class);
        } catch (Exception e) {
            log.error("解析JSON响应失败: {}", response, e);
            throw new RuntimeException("解析大模型响应失败");
        }
    }

    @SuppressWarnings("unchecked")
    private List<UserStory> parseUserStoriesResponse(String response) {
        try {
            String jsonContent = response.replaceAll("```json\\n?", "").replaceAll("\\n?```", "").trim();
            List<Map<String, Object>> storiesMap = JsonUtil.fromJson(jsonContent, List.class);

            return storiesMap.stream().map(map -> {
                UserStory story = new UserStory();
                story.role = (String) map.get("role");
                story.goal = (String) map.get("goal");
                story.benefit = (String) map.get("benefit");
                story.acceptanceCriteria = (List<String>) map.get("acceptanceCriteria");
                return story;
            }).toList();
        } catch (Exception e) {
            log.error("解析用户故事响应失败: {}", response, e);
            throw new RuntimeException("解析用户故事失败");
        }
    }

    @SuppressWarnings("unchecked")
    private List<RequirementIssue> parseIssuesResponse(String response) {
        try {
            String jsonContent = response.replaceAll("```json\\n?", "").replaceAll("\\n?```", "").trim();
            List<Map<String, Object>> issuesMap = JsonUtil.fromJson(jsonContent, List.class);

            return issuesMap.stream().map(map -> {
                RequirementIssue issue = new RequirementIssue();
                issue.type = (String) map.get("type");
                issue.description = (String) map.get("description");
                issue.suggestion = (String) map.get("suggestion");
                issue.relatedRequirements = (List<String>) map.get("relatedRequirements");
                return issue;
            }).toList();
        } catch (Exception e) {
            log.error("解析需求问题响应失败: {}", response, e);
            throw new RuntimeException("解析需求问题失败");
        }
    }

    @SuppressWarnings("unchecked")
    private CompletenessAnalysis parseCompletenessResponse(String response) {
        try {
            String jsonContent = response.replaceAll("```json\\n?", "").replaceAll("\\n?```", "").trim();
            Map<String, Object> analysisMap = JsonUtil.fromJson(jsonContent, Map.class);

            CompletenessAnalysis analysis = new CompletenessAnalysis();
            analysis.score = ((Number) analysisMap.get("score")).doubleValue();
            analysis.missingElements = (List<String>) analysisMap.get("missingElements");
            analysis.suggestions = (List<String>) analysisMap.get("suggestions");
            analysis.summary = (String) analysisMap.get("summary");
            return analysis;
        } catch (Exception e) {
            log.error("解析完整性分析响应失败: {}", response, e);
            throw new RuntimeException("解析完整性分析失败");
        }
    }
}