<!DOCTYPE HTML>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html ng-app="app">
<head>
<%@include file="common/allPages.jsp"%>
<%@include file="common/gerrit.jsp"%>
<%@include file="common/sonar.jsp"%>
<%@include file="common/charting.jsp"%>
<%@include file="common/configPages.jsp"%>
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
	<div ng-controller="SonarStatsCtrl" class="col-sm-12 hidden">
	<div class="col-sm-4">
			<h3>Sonar stats for server {{sonarHostname}} are:</h3>
			<ul class="list-group">
				<li class="list-group-item">Method Complexity: <span class="badge">{{methodComplexity}}</span></li>
				<li class="list-group-item">File Complexity: <span class="badge">{{fileComplexity}}</span></li>
				<li class="list-group-item">Test Coverage: <span class="badge">{{testCoverage}}</span></li>
				<li class="list-group-item">Rules Compliance: <span class="badge">{{rulesCompliance}}</span></li>
				<li class="list-group-item">Total Lines: <span class="badge">{{linesOfCode}}</span></li>
								
			</ul>
		</div>
		<div class="col-sm-8">		
			<canvas tc-chartjs-line chart-data="methodComplexityChartData" chart-options="lineChartOptions"></canvas>	
		</div>
	</div>
</body>
</html>