#!/bin/bash

DEPLOY_PATH=/home/ubuntu
LOG_PATH=$DEPLOY_PATH/deploy.log
ERR_LOG_PATH=$DEPLOY_PATH/deploy_err.log

echo ">>> 배포 경로: $DEPLOY_PATH" >> $LOG_PATH

mkdir -p $DEPLOY_PATH
echo ">>> 디렉토리 생성 완료" >> $LOG_PATH

BUILD_JAR=$(ls $DEPLOY_PATH/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo ">>> build 파일명: $JAR_NAME" >> $LOG_PATH

echo ">>> build 파일 복사" >> $LOG_PATH
cp $BUILD_JAR $DEPLOY_PATH 2>> $ERR_LOG_PATH

echo ">>> 현재 실행중인 애플리케이션 pid 확인 후 종료" >> $LOG_PATH
PIDS=$(ps -ef | grep java | grep -v grep | awk '{print $2}')
if [ -n "$PIDS" ]; then
  kill -15 $PIDS
  echo ">>> 기존 애플리케이션 종료 완료" >> $LOG_PATH
  sleep 5
else
  echo ">>> 종료할 애플리케이션 없음" >> $LOG_PATH
fi

DEPLOY_JAR=$DEPLOY_PATH/$JAR_NAME
echo ">>> $DEPLOY_JAR 실행 시작" >> $LOG_PATH
nohup java -jar -Dspring.profiles.active=prod $DEPLOY_JAR >> $LOG_PATH 2>> $ERR_LOG_PATH &
