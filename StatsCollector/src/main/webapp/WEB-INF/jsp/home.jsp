<!DOCTYPE HTML>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html ng-app="app">
<head>
<script
	src="http://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.1/Chart.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular.js"></script>
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
					class="list-group-item">No Peer Reviewers: <span class="badge">{{noPeerReviewers}}</span></li>
					<div collapse="!isNoPeerCollapsed">
						<div ng-repeat="review in noPeerReviews">
							<a href="http://nreojp.git:8080/#/c/{{review.changeNumber}}/" class="btn btn-success btn-block margin-both-05" target="_blank">{{review.id}}</a>							
						</div>
					</div>
				<li ng-class="getOnePeerReviewRowClass(onePeerPercentage)"
					class="list-group-item">One Peer Reviewer: <span class="badge">{{onePeerReviewer}}</span></li>
				<li ng-class="getTwoPeerReviewRowClass(twoPeerPercentage)"
					class="list-group-item">Two Peer Reviewers: <span
					class="badge">{{twoPeerReviewers}}</span></li>
				<li
					ng-class="getCollabrativeDevelopmentRowClass(collaborativePercentage)"
					class="list-group-item">Collaborative Development: <span
					class="badge">{{collabrativeDevelopment}}</span></li>
				<li class="list-group-item">Total Reviews: <span class="badge">{{totalReviews}}</span></li>
			</ul>
		</div>
		<div class="col-sm-8">
			<div class="col-sm-6">
				<canvas tc-chartjs-pie chart-data="gerritChartData"
					chart-options="gerritChartOptions" chart-legend="gerritChart"
					height="500" width="500"></canvas>
			</div>
			<div tc-chartjs-legend chart-legend="gerritChart"
				class="ng-isolate-scope col-sm-6"></div>
		</div>
	</div>
</body>
</html>