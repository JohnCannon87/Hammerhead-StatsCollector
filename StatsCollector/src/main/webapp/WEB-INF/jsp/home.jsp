<!DOCTYPE HTML>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html ng-app="statsCollectorApp">
<head>
<%@include file="common/header.jspf"%>
<script src="/javascript/controller/statsCollector-statsDisplayController.js"></script>
</head>
<body ng-controller="StatsDisplayCtrl as vm">
<%@include file="common/navbar.jspf"%>
	<div id="fireworks-template">
	 <div id="fw" class="firework"></div>
	 <div id="fp" class="fireworkParticle"><img src="image/particles.gif" alt="" /></div>
	</div>

	<div id="fireContainer"></div>
	<div>
		<div ng-show="!vm.reviewStats" class="col-sm-3 well min-pad">
			<h1>
				<div>
					<p class="text-center"><i class="fa fa-spinner fa-spin fa-3x fa-fw margin-bottom text-center"></i></p>
				</div>
				<div>
					<p class="text-center">Loading Gerrit Statistics...</p>
				</div>
			</h1>
		</div>
		<div ng-show="vm.reviewStats" class="col-sm-3 well min-pad">
			<div class="gerritActitvity">
				<canvas id="gerritActivity" class="chart chart-line"
				  chart-dataset-override="vm.gerritActivityDatasetOverride"
				  chart-data="vm.gerritActivityChartData"
				  chart-series="vm.gerritActivityChartSeries"
				  chart-labels="vm.gerritActivityChartLabels"
				  chart-options="vm.gerritActivityChartOptions"
				  chart-colors="vm.gerritActivityChartColors">
				</canvas>
			</div>
			<div class="col-sm-2"></div>
			<div class="col-sm-8 gerritStatus">
				<canvas id="gerritPie" class="chart chart-pie"
				  chart-data="vm.gerritPieChartData"
				  chart-labels="vm.gerritPieChartLabels"
				  chart-options="vm.gerritPieChartOptions"
				  chart-colors="vm.gerritPieChartColors">
				</canvas>
			</div>
			<div class="col-sm-2"></div>
			<div class="col-sm-12 no-pad">
				<ul class="list-group">
					<li ng-repeat="reviewType in vm.reviewStats" ng-show="reviewType.count > 0" ng-click="reviewType.isCollapsed = !reviewType.isCollapsed" class="list-group-item">
						<span class="legendBox img-circle" style="background-color:#CC0000"></span>{{reviewType.type}}: <span class="badge">{{reviewType.count}}</span>
						<div ng-show="reviewType.isCollapsed" class="">
							<div ng-repeat="review in reviewType.list">
								<a href="http://{{gerritHostname}}:{{gerritHostPort}}/#/c/{{review._number}}/" class="btn btn-success btn-block margin-both-05" target="_blank" data-toggle="tooltip" title="{{review.subject}}">{{review.subject | limitTo : 50}}{{review.subject.length > 50 ? '...' : ''}}</a>
							</div>
						</div>
					</li>
				</ul>
				<div class="row no-gutter">
					<div class="col-sm-6 no-pad-right">
						<ul class="list-group">
							<h4 class="list-group-item active">Authors</h4>
							<li ng-repeat="author in vm.gerritReviewStats.authorsCountList" class="list-group-item"><span class="hideOverflow"><img ng-class="vm.getAvatarClass(author.user.avatars[0].url)" src="{{author.user.avatars[0].url}} "  width="25" height="25"/> {{author.user.name}}: <span class="badge">{{author.count}}</span></span></li>
						</ul>
					</div>
					<div class="col-sm-6 no-pad-left">
						<ul class="list-group">
							<h4 class="list-group-item active">Reviewers</h4>
							<li ng-repeat="reviewer in vm.gerritReviewStats.reviewersCountList" ng-class="{'list-group-item-warning-dim': reviewer.didDoOwnReview == true}" class="list-group-item"><span class="hideOverflow"><img ng-class="vm.getAvatarClass(reviewer.user.avatars[0].url)" src="{{reviewer.user.avatars[0].url}}" width="25" height="25"/> {{reviewer.user.name}}: <span class="badge">{{reviewer.count}}</span></span></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div ng-show="!vm.sonarStats" class="col-sm-9 well min-pad">
			<h1>
				<div>
					<p class="text-center"><i class="fa fa-spinner fa-spin fa-3x fa-fw margin-bottom text-center"></i></p>
				</div>
				<div>
					<p class="text-center">Loading Sonar Statistics...</p>
				</div>
			</h1>
		</div>
		<div ng-show="vm.sonarStats" class="col-sm-9 well min-pad">
			<div class="col-sm-12">
				<div class="col-sm-6 well min-pad">
					<div class="input-group">
						<span ng-show="vm.sonarChart1.trendDirection && vm.sonarChart1.trendDirection !== 'none'" class="input-group-btn">
						    <button type="button" class="btn {{vm.sonarChart1.targetStatusClass}}" ng-click="vm.showOff(vm.sonarChart1)">
		               			<i ng-show="vm.sonarChart1.trendDirection === 'down'" class="fa fa-arrow-down" aria-hidden="true"></i>
		               			<i ng-show="vm.sonarChart1.trendDirection === 'up'" class="fa fa-arrow-up" aria-hidden="true"></i>
		               			<span>{{vm.sonarChart1.latestScore}}</span>
				            </button>
			            </span>
						<ui-select ng-model="vm.selectedMetric1">
							<ui-select-match placeholder="Select or search a person in the list...">{{$select.selected.name}}</ui-select-match>
							<ui-select-choices repeat="item in vm.listOfAvailableMetrics | filter: {name: $select.search} | orderBy: 'name'">
								<div ng-bind-html="item.name | highlight: $select.search"></div>
								<small ng-show="!item.raw">Calculated</small>
							</ui-select-choices>
						</ui-select>
						<span class="input-group-btn">
						  <button ng-disabled="vm.selectedMetric1 === undefined" type="button" ng-click="vm.editSonarMetricParameters(vm.selectedMetric1)" class="btn btn-info">
			               	<i class="fa fa-pencil" aria-hidden="true"></i>
			                 </button>
			            </span>
		            </div>
					<br/>
					<canvas id="sonarChart1" class="chart-base"
					chart-type="vm.sonarChart1.type"
					chart-series ="vm.sonarChart1.series"
					chart-data="vm.sonarChart1.data"
					chart-labels="vm.sonarChart1.labels"
					chart-options="vm.sonarChartOptions"
					chart-colors="vm.sonarChart1.colors"
					chart-dataset-override="vm.sonarChart1.datasetOverride">
					</canvas>
				</div>
				<div class="col-sm-6 well min-pad">
					<div class="input-group">
						<span ng-show="vm.sonarChart2.trendDirection && vm.sonarChart2.trendDirection !== 'none'" class="input-group-btn">
						    <button type="button" class="btn {{vm.sonarChart2.targetStatusClass}}" ng-click="vm.showOff(vm.sonarChart2)">
		               			<i ng-show="vm.sonarChart2.trendDirection === 'down'" class="fa fa-arrow-down" aria-hidden="true"></i>
		               			<i ng-show="vm.sonarChart2.trendDirection === 'up'" class="fa fa-arrow-up" aria-hidden="true"></i>
		               			<span>{{vm.sonarChart2.latestScore}}</span>
				            </button>
			            </span>
						<ui-select ng-model="vm.selectedMetric2">
							<ui-select-match placeholder="Select or search a person in the list...">{{$select.selected.name}}</ui-select-match>
							<ui-select-choices repeat="item in vm.listOfAvailableMetrics | filter: {name: $select.search} | orderBy: 'name'">
								<div ng-bind-html="item.name | highlight: $select.search"></div>
								<small ng-show="!item.raw">Calculated</small>
							</ui-select-choices>
						</ui-select>
						<span class="input-group-btn">
						  <button ng-disabled="vm.selectedMetric2 === undefined" type="button" ng-click="vm.editSonarMetricParameters(vm.selectedMetric2)" class="btn btn-info">
			               	<i class="fa fa-pencil" aria-hidden="true"></i>
			                 </button>
			            </span>
		            </div>
					<br/>
					<canvas id="sonarChart2" class="chart-base"
					chart-type="vm.sonarChart2.type"
					chart-series ="vm.sonarChart2.series"
					chart-data="vm.sonarChart2.data"
					chart-labels="vm.sonarChart2.labels"
					chart-options="vm.sonarChartOptions"
					chart-colors="vm.sonarChart2.colors"
					chart-dataset-override="vm.sonarChart2.datasetOverride">
					</canvas>
				</div>
			</div>
			<div class="col-sm-12">
				<div class="col-sm-6 well min-pad">
					<div class="input-group">
						<span ng-show="vm.sonarChart3.trendDirection && vm.sonarChart3.trendDirection !== 'none'" class="input-group-btn">
						    <button type="button" class="btn {{vm.sonarChart3.targetStatusClass}}" ng-click="vm.showOff(vm.sonarChart3)">
		               			<i ng-show="vm.sonarChart3.trendDirection === 'down'" class="fa fa-arrow-down" aria-hidden="true"></i>
		               			<i ng-show="vm.sonarChart3.trendDirection === 'up'" class="fa fa-arrow-up" aria-hidden="true"></i>
		               			<span>{{vm.sonarChart3.latestScore}}</span>
				            </button>
			            </span>
						<ui-select ng-model="vm.selectedMetric3">
							<ui-select-match placeholder="Select or search a person in the list...">{{$select.selected.name}}</ui-select-match>
							<ui-select-choices repeat="item in vm.listOfAvailableMetrics | filter: {name: $select.search} | orderBy: 'name'">
								<div ng-bind-html="item.name | highlight: $select.search"></div>
								<small ng-show="!item.raw">Calculated</small>
							</ui-select-choices>
						</ui-select>
						<span class="input-group-btn">
						  <button ng-disabled="vm.selectedMetric3 === undefined" type="button" ng-click="vm.editSonarMetricParameters(vm.selectedMetric3)" class="btn btn-info">
			               	<i class="fa fa-pencil" aria-hidden="true"></i>
			                 </button>
			            </span>
		            </div>
					<br/>
					<canvas id="sonarChart3" class="chart-base"
					chart-type="vm.sonarChart3.type"
					chart-series ="vm.sonarChart3.series"
					chart-data="vm.sonarChart3.data"
					chart-labels="vm.sonarChart3.labels"
					chart-options="vm.sonarChartOptions"
					chart-colors="vm.sonarChart3.colors"
					chart-dataset-override="vm.sonarChart3.datasetOverride">
					</canvas>
				</div>
				<div class="col-sm-6 well min-pad">
					<div class="input-group">
						<span ng-show="vm.sonarChart4.trendDirection && vm.sonarChart4.trendDirection !== 'none'" class="input-group-btn">
						    <button type="button" class="btn {{vm.sonarChart4.targetStatusClass}}" ng-click="vm.showOff(vm.sonarChart4)">
		               			<i ng-show="vm.sonarChart4.trendDirection === 'down'" class="fa fa-arrow-down" aria-hidden="true"></i>
		               			<i ng-show="vm.sonarChart4.trendDirection === 'up'" class="fa fa-arrow-up" aria-hidden="true"></i>
		               			<span>{{vm.sonarChart4.latestScore}}</span>
				            </button>
			            </span>
						<ui-select ng-model="vm.selectedMetric4">
							<ui-select-match placeholder="Select or search a person in the list...">{{$select.selected.name}}</ui-select-match>
							<ui-select-choices repeat="item in vm.listOfAvailableMetrics | filter: {name: $select.search} | orderBy: 'name'">
								<div ng-bind-html="item.name | highlight: $select.search"></div>
								<small ng-show="!item.raw">Calculated</small>
							</ui-select-choices>
						</ui-select>
						<span class="input-group-btn">
						  <button ng-disabled="vm.selectedMetric4 === undefined" type="button" ng-click="vm.editSonarMetricParameters(vm.selectedMetric4)" class="btn btn-info">
			               	<i class="fa fa-pencil" aria-hidden="true"></i>
			                 </button>
			            </span>
		            </div>
					<br/>
					<canvas id="sonarChart4" class="chart-base"
					chart-type="vm.sonarChart4.type"
					chart-series ="vm.sonarChart4.series"
					chart-data="vm.sonarChart4.data"
					chart-labels="vm.sonarChart4.labels"
					chart-options="vm.sonarChartOptions"
					chart-colors="vm.sonarChart4.colors"
					chart-dataset-override="vm.sonarChart4.datasetOverride">
					</canvas>
				</div>
			</div>
			<div class="col-sm-10">
				<span class="text center">Sonar Review Length:  {{vm.sonarDistanceValue}} Months</span>
				<slider ng-model="vm.sonarDistanceValue" tooltip_position="bottom" min="6" max="vm.sonarMaxDistance" step="1" value="vm.sonarDistanceValue"></slider>
			</div>
			<div class="col-sm-2">
				<button class="btn btn-primary" ng-click="vm.redraw()"><i class="fa fa-retweet" aria-hidden="true"></i> Redraw Graphs</button>
			</div>
		</div>
	</div>
<%@include file="common/footer.jspf"%>
</body>
</html>