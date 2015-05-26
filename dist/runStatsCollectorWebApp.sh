#!/bin/sh
java -jar StatsCollector-1.0.war -DconfigFile.path=GerritStatistics.properties >> statsCollector.log &
