<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html ng-app="app">
<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular.min.js"></script>
<script src="/javascript/gerrit-app.js"></script>
<script src="/javascript/gerrit-stats.js"></script>
<script src="/javascript/gerrit-stats-service.js"></script>
<script src="/javascript/gerrit-stats-controller.js"></script>
<link rel="stylesheet"
	href="https://netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">

</head>
<body>
	<%@include file="common/navbar.jsp"%>

	<div ng-controller="GerritCtrl" class="col-sm-4">
			<h3>Gerrit stats for server {{gerritHostName}} are:</h3>
			<ul class="list-group">
				<li class="list-group-item">No Peer Reviewers: <span class="badge">{{noPeerReviewers}}</span></li>
				<li class="list-group-item">One Peer Reviewer: <span class="badge">{{onePeerReviewer}}</span></li>
				<li class="list-group-item">Two Peer Reviewers: <span class="badge">{{twoPeerReviewers}}</span></li>
				<li class="list-group-item">Collaborative Development: <span class="badge">{{collabrativeDevelopment}}</span></li>
				<li class="list-group-item">Total Reviews: <span class="badge">{{totalReviews}}</span></li>
			</ul>
	</div>
	<div class="col-sm-8"></div>

</body>
</html>