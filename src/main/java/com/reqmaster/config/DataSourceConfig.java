package com.reqmaster.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    // 配置数据源，直接硬编码连接信息
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        // 数据库URL（包含allowPublicKeyRetrieval=true，固定正确）
        config.setJdbcUrl("jdbc:mysql://localhost:3306/reqmaster?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        // 数据库账号（替换为你的实际账号，默认root）
        config.setUsername("root");
        // 数据库密码（替换为你的实际密码，这里填12345678）
        config.setPassword("12345678");
        // MySQL驱动类
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // 连接池配置（和原配置一致）
        config.setPoolName("ReqMasterHikariPool");
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setAutoCommit(true);
        config.setValidationTimeout(5000);
        return new HikariDataSource(config);
    }

    // 配置JPA，指定Hibernate方言（避免过时警告）
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        // 指定实体类所在包（替换为你的实体类包路径，一般是com.reqmaster.entity）
        em.setPackagesToScan("com.reqmaster.entity");
        // 使用Hibernate适配器
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        // 设置Hibernate属性（解决方言问题）
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update"); // 和原配置一致
        properties.setProperty("hibernate.show_sql", "true"); // 和原配置一致
        properties.setProperty("hibernate.format_sql", "true"); // 和原配置一致
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect"); // 正确方言
        properties.setProperty("hibernate.jdbc.batch_size", "20"); // 和原配置一致
        properties.setProperty("hibernate.order_inserts", "true"); // 和原配置一致
        properties.setProperty("hibernate.order_updates", "true"); // 和原配置一致
        em.setJpaProperties(properties);
        return em;
    }
}