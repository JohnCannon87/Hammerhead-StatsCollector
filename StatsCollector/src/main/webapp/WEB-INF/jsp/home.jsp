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

	<div ng-class="GetGerritStatsClass()" ng-controller="GerritStatsCtrl" ng-if="!showGerritStats">				
			<div class="panel panel-default">
				<div ng-show="showGerritHistory"><canvas tc-chartjs chart-type="Line" chart-data="gerritHistoryChartData" chart-options="lineChartOptionsUpwards" id="fileComplexityChart"></canvas></div>
				<div ng-show="showGerritPie"><canvas tc-chartjs-pie chart-data="gerritChartData" chart-options="gerritChartOptions"></canvas></div>
			</div>
			<ul class="list-group">
				<li ng-show="noPeerReviewers > 0" ng-click="isNoPeerCollapsed = !isNoPeerCollapsed" ng-class="getNoPeerReviewRowClass(noPeerPercentage)"
					class="list-group-item"><span class="legendBox img-circle" style="background-color:#CC0000"></span>No Peer Reviewers: <span class="badge">{{noPeerReviewers}}</span></li>
					<div collapse="!isNoPeerCollapsed">
						<div ng-repeat="review in noPeerReviews">
							<a href="http://{{gerritHostname}}:{{gerritHostPort}}/#/c/{{review._number}}/" class="btn btn-success btn-block margin-both-05" target="_blank">{{review.id}}</a>							
						</div>
					</div>
				<li ng-show="onePeerReviewers > 0" ng-click="isOnePeerCollapsed = !isOnePeerCollapsed" ng-class="getOnePeerReviewRowClass(onePeerPercentage)"
					class="list-group-item"><span class="legendBox img-circle" style="background-color:#009933"></span>One Peer Reviewer: <span class="badge">{{onePeerReviewers}}</span></li>
					<div collapse="!isOnePeerCollapsed">
						<div ng-repeat="review in onePeerReviews">
							<a href="http://{{gerritHostname}}:{{gerritHostPort}}/#/c/{{review._number}}/" class="btn btn-success btn-block margin-both-05" target="_blank">{{review.id}}</a>							
						</div>
					</div>
				<li ng-show="twoPeerReviewers > 0" ng-click="isTwoPeerCollapsed = !isTwoPeerCollapsed" ng-class="getTwoPeerReviewRowClass(twoPeerPercentage)"
					class="list-group-item"><span class="legendBox img-circle" style="background-color:#0099FF"></span>Two Peer Reviewers: <span
					class="badge">{{twoPeerReviewers}}</span></li>
					<div collapse="!isTwoPeerCollapsed">
						<div ng-repeat="review in twoPeerReviews">
							<a href="http://{{gerritHostname}}:{{gerritHostPort}}/#/c/{{review._number}}/" class="btn btn-success btn-block margin-both-05" target="_blank">{{review.id}}</a>							
						</div>
					</div>
				<li ng-show="collabrativeDevelopments > 0" ng-click="isCollabrativeDevelopmentCollapsed = !isCollabrativeDevelopmentCollapsed" 
					ng-class="getCollabrativeDevelopmentRowClass(collaborativePercentage)"
					class="list-group-item"><span class="legendBox img-circle" style="background-color:#6600FF"></span>Collaborative Development: <span
					class="badge">{{collabrativeDevelopments}}</span></li>
					<div collapse="!isCollabrativeDevelopmentCollapsed">
						<div ng-repeat="review in collabrativeDevelopment">
							<a href="http://{{gerritHostname}}:{{gerritHostPort}}/#/c/{{review._number}}/" class="btn btn-success btn-block margin-both-05" target="_blank">{{review.id}}</a>							
						</div>
					</div>
					<li class="list-group-item">Total Reviews: <span class="badge">{{totalReviews}}</span></li>
				<div ng-hide="true">
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
					    <button type="button" class="btn btn-success" ng-click="manuallyRefreshGerritData()"><span class="fa fa-refresh"></span>    Refresh Data Cache</button>
					</li>
				</div>
			</ul>
			<div class="row no-gutter">
				<div class="col-sm-6 no-pad-right">
					<ul class="list-group">
						<h4 class="list-group-item active">Authors</h4>
						<li ng-repeat="author in authors" class="list-group-item"><img class = "img-circle" src="{{author.user.avatars[0].url}}" width="25" height="25"/> {{author.user.name}}: <span class="badge">{{author.count}}</span></li>
					</ul>
				</div>				
				<div class="col-sm-6 no-pad">
					<ul class="list-group">
						<h4 class="list-group-item active">Reviewers</h4>
						<li ng-class="{'list-group-item-warning-dim': reviewer.didDoOwnReview == true}" ng-repeat="reviewer in reviewers" class="list-group-item"><img class = "img-circle" src="{{reviewer.user.avatars[0].url}}" width="25" height="25"/> {{reviewer.user.name}}: <span class="badge">{{reviewer.count}}</span></li>
					</ul>
				</div>
			</div>
	</div>
	<div ng-controller="SonarStatsCtrl" ng-class="GetSonarStatsClass()"  ng-if="!showSonarStats">	
		<div class="col-sm-12 no-pad">	
			<div class="col-sm-6 no-pad">					
				<div class="panel panel-default">		
					<div class="list-group-item" ng-class="getFileComplexityClass(fileComplexity)"><i class="fa fa-arrow-down text-right" aria-hidden="true"></i> File Complexity = {{fileComplexity}}</div>
					<canvas tc-chartjs chart-type="LineAlt" chart-data="fileComplexityChartData" chart-options="lineChartOptionsUpwards" id="fileComplexityChart"></canvas>
				</div>
				<div class="panel panel-default">
					<div class="list-group-item" ng-class="getTestCoverageClass(testCoverage)"><i class="fa fa-arrow-up" aria-hidden="true"></i> Test Coverage = {{testCoverage}}%</div>
					<canvas tc-chartjs chart-type="LineAlt" chart-data="testCoverageChartData" chart-options="lineChartOptionsDownwards" id="testCoverageChart"></canvas>
				</div>
			</div>
			<div class="col-sm-6 no-pad">			
				<div class="panel panel-default">	
					<div class="list-group-item" ng-class="getMethodComplexityClass(methodComplexity)"><i class="fa fa-arrow-down" aria-hidden="true"></i> Method Complexity = {{methodComplexity}}</div>		
					<canvas tc-chartjs chart-type="LineAlt" chart-data="methodComplexityChartData" chart-options="lineChartOptionsUpwards" id="methodComplexityChart"></canvas>
				</div>
				<div class="panel panel-default">	
					<div class="list-group-item" ng-class="getRulesComplianceClass(rulesCompliance)"><i class="fa fa-arrow-up" aria-hidden="true"></i> Rules Compliance = {{rulesCompliance}}%</div>		
					<canvas tc-chartjs chart-type="LineAlt" chart-data="rulesComplianceChartData" chart-options="lineChartOptionsDownwards" id="rulesComplianceChart"></canvas>
				</div>
			</div>
		</div>
	</div>
	<%@include file="common/footer.jspf"%>
</body>
</html>