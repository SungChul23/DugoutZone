#!/bin/bash
cd /home/ubuntu/app
JAR_NAME=$(ls *.jar | head -n 1)
nohup java -jar "$JAR_NAME" > app.log 2>&1 &
