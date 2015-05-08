<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html ng-app="app">
<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular.min.js"></script>
<script src="/javascript/gerrit-app.js"></script>
<script src="/javascript/gerrit-stats.js"></script>
<script src="/javascript/gerrit-service.js"></script>
<script src="/javascript/gerrit-stats-controller.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.min.js"></script>
<link rel="stylesheet"
	href="https://netdna.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

</head>
<body>
	<%@include file="common/navbar.jsp"%>

	<div ng-controller="GerritStatsCtrl" class="col-sm-12">
		<div class="col-sm-4">
			<h3>Gerrit stats for server {{gerritHostname}} are:</h3>
			<ul class="list-group">
				<li ng-class="getNoPeerReviewRowClass(noPeerPercentage)"
					class="list-group-item">No Peer Reviewers: <span class="badge">{{noPeerReviewers}}</span></li>
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
			<canvas id="gerritPieChart" width="400" height="400"></canvas>
		</div>
	</div>
</body>
</html>