#!/bin/bash

# 本地jar包路径
LOCAL_SERVER_JAR_PATH="/Users/apple/gongan/springboot-spark/target/springboot-spark-server.jar"
LOCAL_JOB_JAR_PATH="/Users/apple/gongan/GonganCloudSpark/target/memory-computation-1.0-SNAPSHOT-shaded.jar"

# 远程服务器信息
REMOTE_USER="root"
REMOTE_HOST="192.168.110.9"
REMOTE_PATH="/root"
REMOTE_JOB_PATH="/root/sparkJob"
REMOTE_SERVER_JAR_NAME="springboot-spark-server.jar"
REMOTE_JOB_JAR_NAME="memory-computation-1.0-SNAPSHOT-shaded.jar"


PID=$(ssh $REMOTE_USER@$REMOTE_HOST "pgrep -f $REMOTE_SERVER_JAR_NAME")
echo "Find PID: $PID"

# 上传jar包到远程服务器
echo "Uploading $LOCAL_SERVER_JAR_PATH to $REMOTE_HOST:$REMOTE_PATH..."
scp $LOCAL_SERVER_JAR_PATH $REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH/$REMOTE_JAR_NAME

echo "Uploading $LOCAL_LOCAL_JOB_JAR_PATH to $REMOTE_HOST:$REMOTE_JOB_PATH..."
scp $LOCAL_JOB_JAR_PATH $REMOTE_USER@$REMOTE_HOST:$REMOTE_JOB_PATH/$REMOTE_JOB_JAR_NAME

# 检查并杀死之前运行的进程
echo "Checking and killing previous process..."
if [ -z "$PID" ]; then
  echo "No process found with name '$REMOTE_SERVER_JAR_NAME'"
else
  echo "Found process with PID: $PID, killing it..."

  ssh $REMOTE_USER@$REMOTE_HOST "kill -9 $PID"
  sleep 5
fi

# 使用nohup部署新的jar包
echo "Deploying new jar with nohup..."
ssh $REMOTE_USER@$REMOTE_HOST "nohup /usr/jdk/bin/java -jar $REMOTE_PATH/$REMOTE_SERVER_JAR_NAME > /root/springboot.log 2>&1"

echo "Deployment complete."