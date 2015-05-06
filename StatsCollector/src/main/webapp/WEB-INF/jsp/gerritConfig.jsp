<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html ng-app="app">
<head>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular.js"></script>
<script src="/javascript/gerrit-app.js"></script>
<script src="/javascript/gerrit-stats.js"></script>
<script src="/javascript/gerrit-stats-service.js"></script>
<script src="/javascript/gerrit-stats-controller.js"></script>
</head>
<body>
	<div ng-controller="GerritCtrl">
		<h3>Gerrit Host Name Is {{gerritHostName}}</h2>
		<h3>Gerrit Stats Are:</h3>
		<ul>
			<li>No Peer Reviewers: {{noPeerReviewers}}</li>
			<li>One Peer Reviewer: {{onePeerReviewer}}</li>
			<li>Two Peer Reviewers: {{twoPeerReviewers}}</li>
			<li>Collaborative Development: {{collabrativeDevelopment}}</li>
			<li>Total Reviews: {{totalReviews}}</li>
		</ul>
		
	</div>	
</body>
</html>