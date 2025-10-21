@echo off
REM ReqMaster 项目启动脚本 (Windows)

echo ==========================================
echo    ReqMaster 智能需求分析平台启动脚本
echo ==========================================

REM 设置环境变量
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

REM 检查Java环境
java -version > nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请确保Java 17已安装
    pause
    exit /b 1
)

echo Java环境检查通过

REM 数据库配置
set SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/reqmaster?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
set SPRING_DATASOURCE_USERNAME=root
set SPRING_DATASOURCE_PASSWORD=password

REM 大模型API配置
set DEEPSEEK_API_KEY=your_actual_deepseek_api_key_here

REM 检查配置文件
if not exist "target\reqmaster-1.0.0.jar" (
    echo 错误: 未找到可执行JAR文件，请先运行 mvn clean package
    pause
    exit /b 1
)

REM 创建日志目录
if not exist "logs" mkdir logs

echo 启动参数:
echo  - 数据库: %SPRING_DATASOURCE_URL%
echo  - 数据源用户: %SPRING_DATASOURCE_USERNAME%
echo  - DeepSeek API: %DEEPSEEK_API_KEY%

echo 正在启动 ReqMaster 应用...
java -jar target\reqmaster-1.0.0.jar

pause