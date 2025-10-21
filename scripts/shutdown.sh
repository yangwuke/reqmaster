#!/bin/bash

echo "停止 ReqMaster 应用..."

if [ -f "logs/reqmaster.pid" ]; then
    PID=$(cat logs/reqmaster.pid)
    if ps -p $PID > /dev/null; then
        echo "正在停止进程 $PID"
        kill $PID
        sleep 5

        if ps -p $PID > /dev/null; then
            echo "强制停止进程 $PID"
            kill -9 $PID
        fi

        rm -f logs/reqmaster.pid
        echo "✅ ReqMaster 已停止"
    else
        echo "进程不存在，清理PID文件"
        rm -f logs/reqmaster.pid
    fi
else
    echo "未找到PID文件，尝试通过端口查找进程"
    PID=$(lsof -ti:8080)
    if [ ! -z "$PID" ]; then
        echo "正在停止进程 $PID"
        kill $PID
        echo "✅ ReqMaster 已停止"
    else
        echo "❌ 未找到运行的ReqMaster进程"
    fi
fi