package com.reqmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类（ReqMaster应用入口）
 * 注：@SpringBootApplication 注解包含自动配置、组件扫描、配置类三种功能
 */
@SpringBootApplication
// 若枚举所在包（com.reqmaster.entity.enums）不在启动类同级/子级，需添加@ComponentScan指定扫描范围
// 示例：@ComponentScan(basePackages = {"com.reqmaster", "com.reqmaster.entity.enums"})
public class ReqMasterApplication {

    // 程序入口方法
    public static void main(String[] args) {
        // 启动Spring Boot应用
        SpringApplication.run(ReqMasterApplication.class, args);
    }

}