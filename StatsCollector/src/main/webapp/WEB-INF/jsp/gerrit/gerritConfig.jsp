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
	<link rel="stylesheet"
	href="/css/gerritConfig.css">

</head>
<body ng-controller="GerritCtrl">
<%@include file="../common/navbar.jsp" %>
	<div class="container-fluid">
		<div class="col-sm-12">
			<!-- PAGE TITLE -->
			<div class="page-header col-mid-12">
				<h1>
					<span class="glyphicon glyphicon-cog"></span> Gerrit Server Config
				</h1>
			</div>

			<div class="col-sm-6">
				<label class="control-label col-sm-2" for="hostname">Host
					Name:</label>
				<div class="col-sm-10">
					<input id="hostname" type="text" name="hostname" class="form-control"
						placeholder="Enter New Host Name" ng-model="gerritHostName" />
				</div>
				<div class="col-sm-12">
					<button type="button" class="btn btn-primary btn-block margin-top-30"
						ng-click="changeHostName()">Change</button>
				</div>
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
	</div>	
</body>
</html>