-- 使用数据库
USE reqmaster;

-- 插入示例项目数据
INSERT INTO projects (name, description, domain, created_at, updated_at) VALUES
                                                                             ('智能电商平台', '基于AI推荐的个性化电商平台，包含智能搜索、推荐系统和数据分析', '电商', NOW(), NOW()),
                                                                             ('企业资源计划系统', '集成财务管理、人力资源、供应链管理的企业级ERP系统', '企业软件', NOW(), NOW()),
                                                                             ('在线学习平台', '提供直播授课、在线测试、学习进度跟踪的综合性教育平台', '教育科技', NOW(), NOW()),
                                                                             ('医疗健康管理系统', '患者管理、电子病历、预约挂号的医疗信息化系统', '医疗健康', NOW(), NOW());

-- 插入示例需求数据
INSERT INTO requirements (title, description, type, priority, project_id, source_type, created_at, updated_at) VALUES
-- 电商平台需求
('用户个性化推荐', '根据用户浏览历史和购买记录，使用机器学习算法推荐相关商品', 'FUNCTIONAL', 'HIGH', 1, 'MANUAL', NOW(), NOW()),
('智能搜索功能', '支持自然语言搜索，提供语义理解和搜索建议', 'FUNCTIONAL', 'HIGH', 1, 'MANUAL', NOW(), NOW()),
('购物车优化', '支持商品收藏、价格提醒、库存监控等高级购物车功能', 'FUNCTIONAL', 'MEDIUM', 1, 'MANUAL', NOW(), NOW()),
('系统响应时间', '页面加载时间不超过2秒，搜索接口响应时间不超过500毫秒', 'NON_FUNCTIONAL', 'HIGH', 1, 'MANUAL', NOW(), NOW()),
('数据安全要求', '用户支付信息必须加密存储，符合PCI DSS安全标准', 'NON_FUNCTIONAL', 'HIGH', 1, 'MANUAL', NOW(), NOW()),

-- ERP系统需求
('财务报表生成', '自动生成资产负债表、利润表、现金流量表等标准财务报表', 'FUNCTIONAL', 'HIGH', 2, 'MANUAL', NOW(), NOW()),
('员工绩效管理', '支持KPI设定、绩效评估、360度反馈等员工绩效管理功能', 'FUNCTIONAL', 'MEDIUM', 2, 'MANUAL', NOW(), NOW()),
('供应链可视化', '提供供应链全流程可视化，实时监控库存和物流状态', 'FUNCTIONAL', 'MEDIUM', 2, 'MANUAL', NOW(), NOW()),
('系统集成要求', '支持与现有HR系统、财务系统的API集成', 'CONSTRAINT', 'HIGH', 2, 'MANUAL', NOW(), NOW()),
('数据备份策略', '每日自动备份，支持数据恢复时间不超过4小时', 'NON_FUNCTIONAL', 'MEDIUM', 2, 'MANUAL', NOW(), NOW()),

-- 在线学习平台需求
('直播授课功能', '支持实时音视频传输、白板互动、在线问答的直播授课', 'FUNCTIONAL', 'HIGH', 3, 'MANUAL', NOW(), NOW()),
('智能作业批改', '使用AI技术自动批改选择题和填空题，提供学习建议', 'FUNCTIONAL', 'MEDIUM', 3, 'MANUAL', NOW(), NOW()),
('学习路径推荐', '根据学生水平和学习目标，推荐个性化的学习路径', 'FUNCTIONAL', 'MEDIUM', 3, 'MANUAL', NOW(), NOW()),
('移动端兼容性', '支持iOS和Android主流版本，响应式设计', 'NON_FUNCTIONAL', 'HIGH', 3, 'MANUAL', NOW(), NOW()),
('并发用户支持', '支持至少10000名用户同时在线学习', 'NON_FUNCTIONAL', 'HIGH', 3, 'MANUAL', NOW(), NOW()),

-- 医疗系统需求
('电子病历管理', '支持患者病历的创建、编辑、查询和归档', 'FUNCTIONAL', 'HIGH', 4, 'MANUAL', NOW(), NOW()),
('预约挂号系统', '患者在线预约医生，支持时间段选择和提醒功能', 'FUNCTIONAL', 'HIGH', 4, 'MANUAL', NOW(), NOW()),
('药品库存管理', '实时监控药品库存，自动生成采购建议', 'FUNCTIONAL', 'MEDIUM', 4, 'MANUAL', NOW(), NOW()),
('HIPAA合规要求', '系统必须符合HIPAA医疗信息安全标准', 'CONSTRAINT', 'HIGH', 4, 'MANUAL', NOW(), NOW()),
('系统可用性要求', '系统年可用性不低于99.9%，关键功能支持故障转移', 'NON_FUNCTIONAL', 'HIGH', 4, 'MANUAL', NOW(), NOW());