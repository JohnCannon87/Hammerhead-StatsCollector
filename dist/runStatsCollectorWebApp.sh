#!/bin/sh
kill $(ps aux | grep 'StatsCollector' | awk '{print $2}')
rm -rf statsCollector.log
java -jar StatsCollector-1.0.war -Dserver.port=8080 >> statsCollector.log &
