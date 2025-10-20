# 使用官方Java 17运行时作为父镜像
FROM openjdk:17-jdk-slim

# 设置维护者信息
LABEL maintainer="your-email@example.com"
LABEL version="1.0.0"
LABEL description="ReqMaster - 智能需求分析平台"

# 设置工作目录
WORKDIR /app

# 将打包好的jar文件复制到容器中
COPY target/reqmaster-1.0.0.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs

# 暴露端口
EXPOSE 8080

# 设置环境变量（可以在运行时覆盖）
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/reqmaster?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=password
ENV DEEPSEEK_API_KEY=your_actual_deepseek_api_key_here
ENV SPRING_PROFILES_ACTIVE=prod

# 添加健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/reqmaster/actuator/health || exit 1

# 运行jar文件
ENTRYPOINT ["java", "-jar", "app.jar"]