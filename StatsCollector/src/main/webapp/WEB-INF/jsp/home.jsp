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

	<div class="col-sm-3" ng-controller="GerritStatsCtrl" >
				<canvas tc-chartjs-pie chart-data="gerritChartData"
					chart-options="gerritChartOptions"></canvas>
			<ul class="list-group">
				<li ng-click="isNoPeerCollapsed = !isNoPeerCollapsed" ng-class="getNoPeerReviewRowClass(noPeerPercentage)"
					class="list-group-item"><span class="legendBox" style="background-color:#CC0000"></span>No Peer Reviewers: <span class="badge">{{noPeerReviewers}}</span></li>
					<div collapse="!isNoPeerCollapsed">
						<div ng-repeat="review in noPeerReviews">
							<a href="http://{{gerritHostname}}:{{gerritHostPort}}/#/c/{{review._number}}/" class="btn btn-success btn-block margin-both-05" target="_blank">{{review.id}}</a>							
						</div>
					</div>
				<li ng-click="isOnePeerCollapsed = !isOnePeerCollapsed" ng-class="getOnePeerReviewRowClass(onePeerPercentage)"
					class="list-group-item"><span class="legendBox" style="background-color:#009933"></span>One Peer Reviewer: <span class="badge">{{onePeerReviewer}}</span></li>
					<div collapse="!isOnePeerCollapsed">
						<div ng-repeat="review in onePeerReviews">
							<a href="http://{{gerritHostname}}:{{gerritHostPort}}/#/c/{{review._number}}/" class="btn btn-success btn-block margin-both-05" target="_blank">{{review.id}}</a>							
						</div>
					</div>
				<li ng-click="isTwoPeerCollapsed = !isTwoPeerCollapsed" ng-class="getTwoPeerReviewRowClass(twoPeerPercentage)"
					class="list-group-item"><span class="legendBox" style="background-color:#0099FF"></span>Two Peer Reviewers: <span
					class="badge">{{twoPeerReviewers}}</span></li>
					<div collapse="!isTwoPeerCollapsed">
						<div ng-repeat="review in twoPeerReviews">
							<a href="http://{{gerritHostname}}:{{gerritHostPort}}/#/c/{{review._number}}/" class="btn btn-success btn-block margin-both-05" target="_blank">{{review.id}}</a>							
						</div>
					</div>
				<li ng-click="isCollabrativeDevelopmentCollapsed = !isCollabrativeDevelopmentCollapsed" 
					ng-class="getCollabrativeDevelopmentRowClass(collaborativePercentage)"
					class="list-group-item"><span class="legendBox" style="background-color:#6600FF"></span>Collaborative Development: <span
					class="badge">{{collabrativeDevelopments}}</span></li>
					<div collapse="!isCollabrativeDevelopmentCollapsed">
						<div ng-repeat="review in collabrativeDevelopment">
							<a href="http://{{gerritHostname}}:{{gerritHostPort}}/#/c/{{review._number}}/" class="btn btn-success btn-block margin-both-05" target="_blank">{{review.id}}</a>							
						</div>
					</div>
				<li class="list-group-item">Total Reviews: <span class="badge">{{totalReviews}}</span></li>
				<li class="list-group-item">
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
				    <button type="button" class="btn btn-success" ng-click="manuallyRefreshGerritData()"><span class="glyphicon glyphicon-repeat"></span>    Refresh Data Cache</button>
				</li>
			</ul>
			<div ng-show="gerritStatsStatus.show">
			  <alert type="{{gerritStatsStatus.type}}" close="closeAlert()">{{gerritStatsStatus.msg}}</alert>
			</div>
	</div>
	<div ng-controller="SonarStatsCtrl" class="col-sm-8">
		<div class="col-sm-12">		
			<div class="col-sm-6">					
				<div class="panel panel-default">		
					<div class="list-group-item" ng-class="getFileComplexityClass(fileComplexity)">File Complexity = {{fileComplexity}}</div>
					<canvas tc-chartjs chart-type="LineAlt" chart-data="fileComplexityChartData" chart-options="lineChartOptionsUpwards" id="fileComplexityChart"></canvas>
				</div>
				<div class="panel panel-default">
					<div class="list-group-item" ng-class="getTestCoverageClass(testCoverage)">Test Coverage = {{testCoverage}}</div>
					<canvas tc-chartjs chart-type="LineAlt" chart-data="testCoverageChartData" chart-options="lineChartOptionsDownwards" id="testCoverageChart"></canvas>
				</div>
			</div>
			<div class="col-sm-6">			
				<div class="panel panel-default">	
					<div class="list-group-item" ng-class="getMethodComplexityClass(methodComplexity)">Method Complexity = {{methodComplexity}}</div>		
					<canvas tc-chartjs chart-type="LineAlt" chart-data="methodComplexityChartData" chart-options="lineChartOptionsUpwards" id="methodComplexityChart"></canvas>
				</div>
				<div class="panel panel-default">	
					<div class="list-group-item" ng-class="getRulesComplianceClass(rulesCompliance)">Rules Compliance = {{rulesCompliance}}</div>		
					<canvas tc-chartjs chart-type="LineAlt" chart-data="rulesComplianceChartData" chart-options="lineChartOptionsDownwards" id="rulesComplianceChart"></canvas>
				</div>
			</div>
		</div>
	</div>
	<%@include file="common/footer.jspf"%>
</body>
</html>