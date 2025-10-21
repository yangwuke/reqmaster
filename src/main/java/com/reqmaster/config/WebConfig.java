package com.reqmaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

/**
 * Web配置
 */
@Configuration
public class WebConfig {

    /**
     * 分页配置
     */
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer paginationCustomizer() {
        return pageableResolver -> {
            pageableResolver.setMaxPageSize(100); // 最大每页数量
            pageableResolver.setOneIndexedParameters(true); // 使用1-based页码
        };
    }
}