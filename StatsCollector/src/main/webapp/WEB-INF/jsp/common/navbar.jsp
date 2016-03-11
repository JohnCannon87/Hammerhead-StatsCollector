<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<div class="navbar-header" ng-controller="GerritStatsCtrl">
				<a class="navbar-brand" href="https://github.com/JohnCannon87/StatsCollector">{{gerritProjectName}} Statistics Collector</a>
			</div>
			<div>
				<ul class="nav navbar-nav">
					<li><a href="/home" target="_self">Home</a></li>
					<li><a href="/gerrit/config/view" target="_self">Gerrit Config</a></li>
					<li><a href="/sonar/config/view" target="_self">Sonar Config</a></li>
				</ul>
			</div>
		</div>
	</nav>