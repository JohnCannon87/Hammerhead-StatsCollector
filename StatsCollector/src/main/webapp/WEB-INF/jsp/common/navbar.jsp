<nav class="navbar navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header" ng-controller="StatsCtrl">
				<a class="navbar-brand" href="https://github.com/JohnCannon87/StatsCollector"><i class="fa fa-github"></i> {{gerritProjectName}} Statistics Collector</a>
			</div>
			<div>
				<ul class="nav navbar-nav">
					<li><a href="/home" target="_self"><i class="fa fa-home"></i> Home</a></li>
					<li><a href="/gerrit/config/view" target="_self"><i class="fa fa-cogs"></i> Gerrit Config</a></li>
					<li><a href="/sonar/config/view" target="_self"><i class="fa fa-cogs"></i> Sonar Config</a></li>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li ng-click="showGerritStats = !showGerritStats">
						<a href="#" target="_self">
							<div ng-if="!showGerritStats"><i class="fa fa-compress"></i> Hide Gerrit Stats</div>
							<div ng-if="showGerritStats"><i class="fa fa-expand"></i> Show Gerrit Stats</div>
						</a>
					</li>
					<li ng-click="showSonarStats = !showSonarStats">
						<a href="#" target="_self">
						<div ng-if="!showSonarStats"><i class="fa fa-compress"></i> Hide Sonar Stats</div>
						<div ng-if="showSonarStats"><i class="fa fa-expand"></i> Show Sonar Stats</div>
						</a>
					</li>
				</ul>
			</div>
		</div>
	</nav>
	<br/>