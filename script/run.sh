#!/bin/sh

java -jar target/scoreboard.jar
chmod +x script/ffmpeg.sh && sh script/ffmpeg.sh
lein clean
