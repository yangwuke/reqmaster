# ReqMaster - 智能需求分析与管理平台

## 项目概述
ReqMaster是一个基于大模型的智能需求分析与管理平台，旨在提高需求工程流程的效率和质量。该平台集成了先进的人工智能技术，能够自动解析需求文档、生成用户故事、检查需求一致性和完整性，为团队提供全方位的需求管理解决方案。

## 技术栈

### 后端技术
- Java 17
- Spring Boot 3.2
- Spring Data JPA
- MySQL 数据库
- Swagger/OpenAPI 文档

### 前端技术
- HTML5
- Thymeleaf 模板引擎
- Bootstrap (推测，根据项目结构)

### AI 集成
- DeepSeek 大模型 API
- 通义千问 API
- 豆包 API

### 其他工具
- Maven 构建工具
- Docker 容器化支持

## 核心功能

### 1. 项目管理
- 项目创建、查询、更新和删除
- 项目信息管理
- 项目权限控制（待实现）

### 2. 需求管理
- 需求的完整CRUD操作
- 需求分类管理（功能性需求、非功能性需求等）
- 需求优先级管理
- 需求搜索和筛选
- 需求统计分析

### 3. 智能需求分析
- 文档自动解析（支持PDF、DOCX、TXT格式）
- 基于大模型的需求提取和分类
- 用户故事自动生成
- 需求一致性检查
- 需求完整性分析

### 4. 智能对话系统
- 需求相关问题智能回答
- 基于上下文的需求分析

### 5. 系统管理
- API文档（Swagger）
- 日志管理
- 配置管理

## 项目结构

```
reqmaster/
├── .gitignore               # Git忽略文件
├── Dockerfile               # Docker构建文件
├── README.md                # 项目说明文档
├── config/                  # 配置文件目录
├── docker-compose.yml       # Docker Compose配置
├── docs/                    # 项目文档目录
│   ├── PROJECT_SUMMARY.md   # 项目摘要
│   ├── api-test.http        # API测试文件
│   └── demo-guide.md        # 演示指南
├── pom.xml                  # Maven项目配置
├── sample_requirements.docx # 需求示例文档
├── scripts/                 # 脚本目录
│   ├── shutdown.sh          # 关闭脚本(Linux)
│   ├── startup.bat          # 启动脚本(Windows)
│   └── startup.sh           # 启动脚本(Linux)
├── sql/                     # SQL脚本目录
│   ├── init_database.sql    # 数据库初始化脚本
│   └── sample_data.sql      # 示例数据脚本
└── src/                     # 源代码目录
    ├── main/                # 主源代码
    │   ├── java/            # Java源代码
    │   └── resources/       # 资源文件
    ├── resources/           # 配置资源
    │   ├── application.yml  # 应用配置
    │   ├── logback-spring.xml # 日志配置
    │   ├── prompts/         # AI提示模板
    │   ├── static/          # 静态资源
    │   └── templates/       # 视图模板
    └── test/                # 测试代码
        ├── java/            # Java测试代码
        └── resources/       # 测试资源
```

## 安装与部署

### 环境要求
- JDK 17 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0 或更高版本
- 支持的浏览器：Chrome, Firefox, Safari, Edge

### 本地开发环境搭建

1. **克隆项目**
   ```bash
   git clone <项目仓库地址>
   cd reqmaster
   ```

2. **配置数据库**
   - 创建MySQL数据库
   - 执行初始化SQL脚本
   ```bash
   mysql -u <用户名> -p <数据库名> < sql/init_database.sql
   ```
   - 可选：导入示例数据
   ```bash
   mysql -u <用户名> -p <数据库名> < sql/sample_data.sql
   ```

3. **配置应用**
   - 修改 `src/main/resources/application.yml` 文件，配置数据库连接和大模型API密钥

4. **构建项目**
   ```bash
   mvn clean package -DskipTests
   ```

5. **启动应用**
   ```bash
   # Linux/Mac
   ./scripts/startup.sh
   # Windows
   .\scripts\startup.bat
   ```

### Docker部署

1. **构建Docker镜像**
   ```bash
   docker build -t reqmaster .
   ```

2. **使用Docker Compose启动**
   ```bash
   docker-compose up -d
   ```

## 使用指南

### 访问应用
- 启动后，访问 http://localhost:8080 进入应用首页
- 访问 http://localhost:8080/swagger-ui.html 查看API文档

### 主要功能使用

1. **创建项目**
   - 登录后，在项目管理页面点击"新建项目"
   - 填写项目名称、描述等信息

2. **导入需求文档**
   - 进入项目详情页
   - 点击"导入文档"按钮
   - 上传支持格式的文档文件（PDF、DOCX、TXT）

3. **智能分析需求**
   - 文档导入后，系统自动进行分析
   - 可以在需求列表中查看提取的需求
   - 对需求进行编辑、分类和优先级设置

4. **使用智能对话**
   - 进入智能对话页面
   - 输入关于需求的问题
   - 系统将基于已有的需求信息提供智能回答

## API接口文档

系统提供了完整的RESTful API接口，可通过Swagger UI进行查看和测试：
- 访问地址：http://localhost:8080/swagger-ui.html
- 主要API包括：项目管理、需求管理、智能分析等模块

## 配置说明

主要配置文件：`src/main/resources/application.yml`

关键配置项：
- 数据库连接信息
- 服务器端口（默认8080）
- 大模型API配置（DeepSeek、通义千问、豆包）
- 日志级别设置

## 故障排查

### 常见问题

1. **数据库连接失败**
   - 检查数据库是否启动
   - 验证用户名、密码和数据库名是否正确
   - 检查网络连接

2. **大模型API调用失败**
   - 检查API密钥是否正确配置
   - 验证网络连接是否正常
   - 查看日志文件获取详细错误信息

3. **文档解析失败**
   - 检查文件格式是否支持
   - 验证文件大小是否在限制范围内
   - 查看日志获取详细错误信息

## 日志

- 日志文件位置：根据配置不同，通常在应用目录下的logs文件夹
- 日志级别：可在application.yml中配置
- 主要日志包括：系统日志、业务日志、错误日志

## 许可

本项目采用 [MIT License](LICENSE) 开源许可。

## 贡献

欢迎提交Issue和Pull Request！

## 联系方式

- 项目维护者：[ywk]
- 邮箱：[y2960220863@163.com]

- 项目仓库：[仓库地址]
