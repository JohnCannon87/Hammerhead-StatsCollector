<!DOCTYPE HTML>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html ng-app="app">
<head>
<script
	src="http://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.1/Chart.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.13.0/ui-bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.13.0/ui-bootstrap-tpls.min.js"></script>
<script src="/javascript/tc-angular-chartjs.js"></script>
<script src="/javascript/gerrit-app.js"></script>
<script src="/javascript/gerrit-stats.js"></script>
<script src="/javascript/gerrit-service.js"></script>
<script src="/javascript/gerrit-stats-controller.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.13.0/ui-bootstrap-tpls.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/danialfarid-angular-file-upload/4.1.4/ng-file-upload-shim.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/danialfarid-angular-file-upload/4.1.4/ng-file-upload.min.js"></script>
<link rel="stylesheet"
	href="https://netdna.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/app.css">
<link rel="stylesheet" href="/css/gerritConfig.css">

</head>
<body>
	<%@include file="common/navbar.jsp"%>

	<div ng-controller="GerritStatsCtrl" class="col-sm-12">
		<div class="col-sm-4">
			<h3>Gerrit stats for server {{gerritHostname}} are:</h3>
			<ul class="list-group">
				<li ng-click="isNoPeerCollapsed = !isNoPeerCollapsed" ng-class="getNoPeerReviewRowClass(noPeerPercentage)"
					class="list-group-item"><span class="legendBox" style="background-color:#CC0000"></span>No Peer Reviewers: <span class="badge">{{noPeerReviewers}}</span></li>
					<div collapse="!isNoPeerCollapsed">
						<div ng-repeat="review in noPeerReviews">
							<a href="http://nreojp.git:8080/#/c/{{review.changeNumber}}/" class="btn btn-success btn-block margin-both-05" target="_blank">{{review.id}}</a>							
						</div>
					</div>
				<li ng-class="getOnePeerReviewRowClass(onePeerPercentage)"
					class="list-group-item"><span class="legendBox" style="background-color:#009933"></span>One Peer Reviewer: <span class="badge">{{onePeerReviewer}}</span></li>
				<li ng-class="getTwoPeerReviewRowClass(twoPeerPercentage)"
					class="list-group-item"><span class="legendBox" style="background-color:#0099FF"></span>Two Peer Reviewers: <span
					class="badge">{{twoPeerReviewers}}</span></li>
				<li
					ng-class="getCollabrativeDevelopmentRowClass(collaborativePercentage)"
					class="list-group-item"><span class="legendBox" style="background-color:#6600FF"></span>Collaborative Development: <span
					class="badge">{{collabrativeDevelopment}}</span></li>
				<li class="list-group-item">Total Reviews: <span class="badge">{{totalReviews}}</span></li>
				<li class="list-group-item">
					<!-- Single button -->
				    <div class="btn-group" dropdown is-open="status.isopen">
				      <button type="button" class="btn btn-primary dropdown-toggle" dropdown-toggle ng-disabled="disabled">
				        Stats for status {{gerritStatus}} <span class="caret"></span>
				      </button>
				      <ul class="dropdown-menu" role="menu">
				        <li><a ng-click="changeGerritStatus('merged')">Set status to merged</a></li>
				        <li><a ng-click="changeGerritStatus('open')">Set status to open</a></li>
				        <li><a ng-click="changeGerritStatus('abandoned')">Set status to abandoned</a></li>
				      </ul>
				    </div>
				    <button type="button" class="btn btn-success" ng-click="manuallyRefreshData()"><span class="glyphicon glyphicon-repeat"></span>    Manually Refresh Data Cache</button>
				</li>
			</ul>
			<div ng-show="gerritStatsStatus.show">
			  <alert type="{{gerritStatsStatus.type}}" close="closeAlert()">{{gerritStatsStatus.msg}}</alert>
			</div>
		</div>
		<div class="col-sm-8">
			<div class="col-sm-6">
				<canvas tc-chartjs-pie chart-data="gerritChartData"
					chart-options="gerritChartOptions""
					height="500" width="500"></canvas>
			</div>
		</div>
	</div>
</body>
</html>