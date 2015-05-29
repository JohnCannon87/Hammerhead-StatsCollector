Stats Collector
---------------
Simple configurable web application to collect and display statistics from various sources (currently Gerrit, in the future Sonar and Jenkins)

Designed to be displayed on a TV or other visual device to allow software teams to view statistics.

Build Status
------------
[![Build Status](https://travis-ci.org/JohnCannon87/Hammerhead-StatsCollector.svg?branch=master)](https://travis-ci.org/JohnCannon87/Hammerhead-StatsCollector)
[![Coverage Status](https://coveralls.io/repos/JohnCannon87/Hammerhead-StatsCollector/badge.svg)](https://coveralls.io/r/JohnCannon87/Hammerhead-StatsCollector)

Usage
-----

* Copy the contents of the "dist" folder to a directory on the machine you wish to run the application on.
* Alter the .sh or .bat script to specify the port you wish the web interface to run on.
* Set your initial parameters in the GerritStatistics.properties file.
* Open up http://localhost:YOUR-PORT (default is 8080)
* Go to the config tab at the top and entry your Gerrit server details (See below for help setting the thread limits)
* Setup on a browser to show  the data ! (currently manual refresh is required)

Third Party Libraries
---------------------

Stats Collector uses the following projects:

* [Twitter Bootstrap](http://getbootstrap.com/)
* [AngularJS](https://angularjs.org/)
* [UIBootstrap](http://angular-ui.github.io/bootstrap/)
* [ChartJS](http://www.chartjs.org/)
* [angular-chart.js](http://jtblin.github.io/angular-chart.js/)
* [FileSaver.js](https://github.com/eligrey/FileSaver.js/)
* [ng-file-upload](https://github.com/danialfarid/ng-file-upload)

* [SpringBoot](http://projects.spring.io/spring-boot/)
* [google-gson](https://github.com/google/gson)
* [google-guava](https://github.com/google/guava)
* [log4j](http://logging.apache.org/log4j/2.x/)
* [Joda Time](http://www.joda.org/joda-time/)
* [Apache Commons Configuration](https://commons.apache.org/proper/commons-configuration/)
* [Apache Commons IO](https://commons.apache.org/proper/commons-io/)
* [Apache Commons Beans Utils](http://commons.apache.org/proper/commons-beanutils/)
* [JUnit](http://junit.org/)
* [Mockito](https://code.google.com/p/mockito/)
* [Powermock](https://code.google.com/p/powermock/)
* [JSTL](https://jstl.java.net/)

Setting Thread Limits
---------------------
The thread limit setting in the Gerrit config decides how many items each thread can process when pulling back details from Gerrit.
For example if you have 6000 changes in gerrit and have the thread limit set to 2000 (default) the app will run up 3 threads to process the information each time it is cached.
As each of these threads creates a HTTP connection to your Gerrit box every 15 minutes if you have the limit set low (as in the example above setting it to 200 would result in 30 threads) then you can overwhelm the gerrit server resulting in missing stats as the connections fail.
Ensure you check the logs to see if any errors occur the first time you collect stats, any errors should be reported back to the UI in the connection status alert.