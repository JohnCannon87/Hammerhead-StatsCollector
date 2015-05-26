Stats Collector
---------------
Simple configurable web application to collect and display statistics from various sources (currently Gerrit, in the future Sonar and Jenkins)

Designed to be displayed on a TV or other visual device to allow software teams to view statistics.

Usage
-----

* Copy the contents of the "dist" folder to a directory on the machine you wish to run the application on.
* Alter the .sh or .bat script to specify the port you wish the web interface to run on.
* Open up http://localhost:<YOUR-PORT> (default is 8080)
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

