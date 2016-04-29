<nav class="navbar navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header" ng-controller="StatsCtrl">
				<a class="navbar-brand" href="https://github.com/JohnCannon87/StatsCollector"><i class="fa fa-github"></i> {{gerritProjectName}} Statistics Collector</a>
			</div>
			<div>
				<ul class="nav navbar-nav">
					<li><a href="/home" target="_self"><i class="fa fa-home"></i> Home</a></li>
					<li><a href={{gerritUrl}} target="_self"><img src="images/gerritLogo.png" width="17" /> Gerrit</a></li>
					<li><a href={{sonarUrl}} target="_self"><img src="images/sonarLogo.png" width="17" /> Sonar</a></li>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="/gerrit/config/view" target="_self"><i class="fa fa-cogs"></i> Gerrit Config</a></li>
					<li><a href="/sonar/config/view" target="_self"><i class="fa fa-cogs"></i> Sonar Config</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<br/>