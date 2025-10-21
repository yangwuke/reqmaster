#!/bin/bash

# ReqMaster 项目启动脚本

echo "=========================================="
echo "   ReqMaster 智能需求分析平台启动脚本   "
echo "=========================================="

# 设置环境变量
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$PATH

# 检查Java环境
java -version > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "错误: 未找到Java环境，请确保Java 17已安装"
    exit 1
fi

echo "Java环境检查通过"

# 数据库配置（可以通过环境变量覆盖）
export SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL:-"jdbc:mysql://localhost:3306/reqmaster?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8"}
export SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME:-"root"}
export SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD:-"password"}

# 大模型API配置
export DEEPSEEK_API_KEY=${DEEPSEEK_API_KEY:-"your_actual_deepseek_api_key_here"}

# 检查配置文件
if [ ! -f "target/reqmaster-1.0.0.jar" ]; then
    echo "错误: 未找到可执行JAR文件，请先运行 mvn clean package"
    exit 1
fi

# 创建日志目录
mkdir -p logs

echo "启动参数:"
echo " - 数据库: $SPRING_DATASOURCE_URL"
echo " - 数据源用户: $SPRING_DATASOURCE_USERNAME"
echo " - DeepSeek API: ${DEEPSEEK_API_KEY:0:10}******"

# 启动应用
echo "正在启动 ReqMaster 应用..."
nohup java -jar target/reqmaster-1.0.0.jar > logs/reqmaster.log 2>&1 &

# 获取进程ID
APP_PID=$!
echo $APP_PID > logs/reqmaster.pid

echo "应用启动中..."
echo "进程ID: $APP_PID"
echo "日志文件: logs/reqmaster.log"
echo "应用地址: http://localhost:8080/reqmaster"
echo "API文档: http://localhost:8080/reqmaster/swagger-ui.html"

# 等待应用启动
sleep 10

# 检查应用状态
if curl -f http://localhost:8080/reqmaster/ > /dev/null 2>&1; then
    echo "✅ ReqMaster 启动成功!"
else
    echo "❌ ReqMaster 启动失败，请检查日志文件"
    exit 1
fi