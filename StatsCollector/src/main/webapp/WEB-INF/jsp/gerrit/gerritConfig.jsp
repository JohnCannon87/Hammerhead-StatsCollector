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
<script src="/javascript/gerrit-config-controller.js"></script>
<link rel="stylesheet"
	href="https://netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/css/gerritConfig.css">
</head>
<body ng-controller="GerritConfigCtrl">
	<%@include file="../common/navbar.jsp"%>
	<div class="container-fluid">
		<div class="col-sm-12">
			<!-- PAGE TITLE -->
			<div class="page-header col-mid-12">
				<h1>
					<span class="glyphicon glyphicon-cog"></span> Gerrit Config
				</h1>
			</div>

			<div class="page-header col-mid-12">
				<h3>
					<span class="glyphicon glyphicon-cog"></span> Server Config
				</h3>
			</div>
			<div class="col-sm-12">
				<div class="col-sm-6">
					<form class="form-horizontal">
						<div class="form-group">
							<label class="col-sm-2 control-label">Username:</label>
							<div class="col-sm-10">
								<input type="text" ng-model="gerritUsername"
									placeholder="Gerrit Server Username" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Password:</label>
							<div class="col-sm-10">
								<input type="text" ng-model="gerritPassword"
									placeholder="Gerrit Server Password" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Hostname:</label>
							<div class="col-sm-10">
								<input type="text" ng-model="gerritHostname"
									placeholder="Gerrit Server Hostname" />
							</div>
						</div>
					</form>
					<hr>
					<button class="btn btn-success" ng-click="saveGerritConfig()">
						<span class="glyphicon glyphicon-save"></span>  Save Changes
					</button>
				</div>

				<div class="col-sm-6">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>Name</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<tr ng-repeat="reviewer in reviewersToIgnore">
								<td>{{reviewer}}</td>
								<td><button type="button" class=" btn btn-danger"
										ng-click="removeReviewer(reviewer)">Remove</button></td>
							</tr>
							<tr>
								<td><input type="text" name="reviewer" class="form-control"
									placeholder="Enter New Reviewer To Ignore"
									ng-model="gerritReviewer"></td>
								<td><button type="button" class=" btn btn-primary"
										ng-click="addReviewer()">Add Reviewer</button></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<!-- Target Config -->
			<div class="col-sm-12">
				<div class="col-sm-6">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>Value</th>
								<th>Target/Limit (%)</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>No Peer Reviews</td>
								<td><input type="text" ng-model="noPeerReviewsTarget"
									placeholder="No Peer Reviews Target" /></td>
							</tr>
							<tr>
								<td>One Peer Review</td>
								<td><input type="text" ng-model="onePeerReviewTarget"
									placeholder="One Peer Reviews Target" /></td>
							</tr>
							<tr>
								<td>Two Peer Reviews</td>
								<td><input type="text" ng-model="twoPeerReviewTarget"
									placeholder="Two Peer Reviews Target" /></td>
							</tr>
							<tr>
								<td>Collaborative Development</td>
								<td><input type="text" ng-model="collaborativeReviewTarget"
									placeholder="Collaborative Reviews Target" /></td>
							</tr>
						</tbody>
					</table>
					<button class="btn btn-success" ng-click="saveTargetConfig()">
						<span class="glyphicon glyphicon-save"></span>  Save Changes
					</button>
				</div>
				<div class="col-sm-6"></div>
			</div>
		</div>
	</div>
</body>
</html>