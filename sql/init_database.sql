-- 创建数据库
CREATE DATABASE IF NOT EXISTS reqmaster CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE reqmaster;

-- 创建项目表
CREATE TABLE IF NOT EXISTS projects (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL,
    description TEXT,
    domain VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_project_name (name)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建需求表
CREATE TABLE IF NOT EXISTS requirements (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            title VARCHAR(200) NOT NULL,
    description TEXT,
    type VARCHAR(20) NOT NULL DEFAULT 'FUNCTIONAL',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    project_id BIGINT NOT NULL,
    source_type VARCHAR(20),
    metadata JSON,
    is_analyzed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    INDEX idx_project_id (project_id),
    INDEX idx_type (type),
    INDEX idx_priority (priority),
    INDEX idx_created_at (created_at)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入示例数据
INSERT INTO projects (name, description, domain) VALUES
                                                     ('电商平台项目', '一个完整的B2C电商平台，包含商品管理、订单处理、支付集成等功能', '电商'),
                                                     ('OA办公系统', '企业内部办公自动化系统，包含流程审批、文档管理、消息通知等模块', '企业办公'),
                                                     ('在线教育平台', '提供在线课程学习、直播授课、作业提交等功能的平台', '教育');

INSERT INTO requirements (title, description, type, priority, project_id, source_type) VALUES
                                                                                           ('用户注册登录', '用户可以通过手机号或邮箱注册账号，支持密码登录和验证码登录', 'FUNCTIONAL', 'HIGH', 1, 'MANUAL'),
                                                                                           ('商品搜索功能', '用户可以根据关键词搜索商品，支持按价格、销量、评分等排序', 'FUNCTIONAL', 'HIGH', 1, 'MANUAL'),
                                                                                           ('购物车管理', '用户可以将商品加入购物车，支持修改数量、删除商品、批量结算', 'FUNCTIONAL', 'HIGH', 1, 'MANUAL'),
                                                                                           ('系统响应时间', '页面加载时间不超过3秒，核心接口响应时间不超过1秒', 'NON_FUNCTIONAL', 'MEDIUM', 1, 'MANUAL'),
                                                                                           ('数据安全性', '用户密码需要加密存储，支付接口需要支持HTTPS协议', 'NON_FUNCTIONAL', 'HIGH', 1, 'MANUAL'),
                                                                                           ('流程审批', '支持多级审批流程，可以自定义审批节点和审批人', 'FUNCTIONAL', 'HIGH', 2, 'MANUAL'),
                                                                                           ('文档版本管理', '支持文档的版本控制，可以查看历史版本和恢复旧版本', 'FUNCTIONAL', 'MEDIUM', 2, 'MANUAL');