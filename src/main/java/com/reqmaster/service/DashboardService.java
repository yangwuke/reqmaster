package com.reqmaster.service;

import com.reqmaster.repository.ProjectRepository;
import com.reqmaster.repository.RequirementRepository;
import com.reqmaster.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 仪表板服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final RequirementRepository requirementRepository;
    private final ChatSessionRepository chatSessionRepository;

    /**
     * 获取系统仪表板数据
     */
    public DashboardData getDashboardData() {
        DashboardData data = new DashboardData();

        // 基础统计
        data.setTotalProjects(projectRepository.count());
        data.setTotalRequirements(requirementRepository.count());
        data.setTotalChatSessions(chatSessionRepository.count());

        // 需求类型分布
        Map<String, Long> requirementTypeDistribution = new HashMap<>();
//        requirementTypeDistribution.put("FUNCTIONAL",
//                requirementRepository.findByType(com.reqmaster.entity.enums.RequirementType.FUNCTIONAL).size());
        requirementTypeDistribution.put("FUNCTIONAL",
                (long) requirementRepository.findByType(com.reqmaster.entity.enums.RequirementType.FUNCTIONAL).size());

//        requirementTypeDistribution.put("NON_FUNCTIONAL",
//                requirementRepository.findByType(com.reqmaster.entity.enums.RequirementType.NON_FUNCTIONAL).size());
        requirementTypeDistribution.put("NON_FUNCTIONAL",
                (long) requirementRepository.findByType(com.reqmaster.entity.enums.RequirementType.NON_FUNCTIONAL).size());

//        requirementTypeDistribution.put("BUSINESS_RULE",
//                requirementRepository.findByType(com.reqmaster.entity.enums.RequirementType.BUSINESS_RULE).size());
        requirementTypeDistribution.put("BUSINESS_RULE",
                (long) requirementRepository.findByType(com.reqmaster.entity.enums.RequirementType.BUSINESS_RULE).size());
        data.setRequirementTypeDistribution(requirementTypeDistribution);

        // 项目统计
        data.setRecentActivity(getRecentActivity());

        log.info("仪表板数据查询完成");
        return data;
    }

    /**
     * 获取项目仪表板数据
     */
    public ProjectDashboardData getProjectDashboardData(Long projectId) {
        ProjectDashboardData data = new ProjectDashboardData();

        data.setProjectId(projectId);
        data.setTotalRequirements(requirementRepository.countByProjectId(projectId));
        data.setTotalChatSessions(chatSessionRepository.countByProjectId(projectId));

        // 需求分析状态
        long analyzedCount = requirementRepository.findByProjectId(projectId).stream()
                .filter(req -> Boolean.TRUE.equals(req.getIsAnalyzed()))
                .count();
        data.setAnalyzedRequirements(analyzedCount);

        // 最近对话
        data.setRecentSessions(chatSessionRepository.findRecentSessions(projectId, 5));

        return data;
    }

    private Map<String, Object> getRecentActivity() {
        Map<String, Object> activity = new HashMap<>();
        // 这里可以添加最近活动的逻辑，比如最近创建的项目、需求等
        activity.put("lastUpdated", LocalDateTime.now());
        return activity;
    }

    // 数据类
    public static class DashboardData {
        private Long totalProjects;
        private Long totalRequirements;
        private Long totalChatSessions;
        private Map<String, Long> requirementTypeDistribution;
        private Map<String, Object> recentActivity;

        // getters and setters
        public Long getTotalProjects() { return totalProjects; }
        public void setTotalProjects(Long totalProjects) { this.totalProjects = totalProjects; }
        public Long getTotalRequirements() { return totalRequirements; }
        public void setTotalRequirements(Long totalRequirements) { this.totalRequirements = totalRequirements; }
        public Long getTotalChatSessions() { return totalChatSessions; }
        public void setTotalChatSessions(Long totalChatSessions) { this.totalChatSessions = totalChatSessions; }
        public Map<String, Long> getRequirementTypeDistribution() { return requirementTypeDistribution; }
        public void setRequirementTypeDistribution(Map<String, Long> requirementTypeDistribution) { this.requirementTypeDistribution = requirementTypeDistribution; }
        public Map<String, Object> getRecentActivity() { return recentActivity; }
        public void setRecentActivity(Map<String, Object> recentActivity) { this.recentActivity = recentActivity; }
    }

    public static class ProjectDashboardData {
        private Long projectId;
        private Long totalRequirements;
        private Long analyzedRequirements;
        private Long totalChatSessions;
        private Object recentSessions;

        // getters and setters
        public Long getProjectId() { return projectId; }
        public void setProjectId(Long projectId) { this.projectId = projectId; }
        public Long getTotalRequirements() { return totalRequirements; }
        public void setTotalRequirements(Long totalRequirements) { this.totalRequirements = totalRequirements; }
        public Long getAnalyzedRequirements() { return analyzedRequirements; }
        public void setAnalyzedRequirements(Long analyzedRequirements) { this.analyzedRequirements = analyzedRequirements; }
        public Long getTotalChatSessions() { return totalChatSessions; }
        public void setTotalChatSessions(Long totalChatSessions) { this.totalChatSessions = totalChatSessions; }
        public Object getRecentSessions() { return recentSessions; }
        public void setRecentSessions(Object recentSessions) { this.recentSessions = recentSessions; }
    }
}