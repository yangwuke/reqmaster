package com.reqmaster.controller;

import com.reqmaster.dto.ApiResponse;
import com.reqmaster.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表板控制器
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "仪表板", description = "系统数据统计和展示")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "获取系统仪表板数据")
    public ResponseEntity<ApiResponse<DashboardService.DashboardData>> getDashboard() {
        DashboardService.DashboardData data = dashboardService.getDashboardData();
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/projects/{projectId}")
    @Operation(summary = "获取项目仪表板数据")
    public ResponseEntity<ApiResponse<DashboardService.ProjectDashboardData>> getProjectDashboard(
            @PathVariable Long projectId) {
        DashboardService.ProjectDashboardData data = dashboardService.getProjectDashboardData(projectId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}