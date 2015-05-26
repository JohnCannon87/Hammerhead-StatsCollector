#!/bin/sh
java -jar StatsCollector-1.0.war -DconfigFile.path=GerritStatistics.properties -Dserver.port=8080 >> statsCollector.log &
