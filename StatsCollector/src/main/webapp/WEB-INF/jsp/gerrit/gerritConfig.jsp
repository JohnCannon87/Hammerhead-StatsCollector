<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<html ng-app="app">
<head>
<%@include file="../common/allPages.jsp"%>
<%@include file="../common/gerrit.jsp"%>
<%@include file="../common/sonar.jsp"%>
<%@include file="../common/charting.jsp"%>
<%@include file="../common/configPages.jsp"%>
</head>
<body ng-controller="GerritConfigCtrl">
	<%@include file="../common/navbar.jsp"%>
	<div class="container-fluid">
		<div class="col-sm-12">
			<!-- PAGE TITLE -->
			<div class="page-header col-mid-12">
				<h1>
					<span class="glyphicon glyphicon-cog"></span> Gerrit Config
					<button class="btn btn-primary" ng-click="downloadConfig()">
						<span class="glyphicon glyphicon-download"></span>  Download Config
					</button>
					<button class="btn btn-primary" ngf-select="" ng-model="files" ngf-multiple="true" class="ng-pristine ng-valid"><span class="glyphicon glyphicon-upload"></span>  Upload Config</button>						
					</button>
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
							<label class="col-sm-2 control-label">Project Display Name:</label>
							<div class="col-sm-10">
								<input type="text" ng-model="gerritProjectName"
									placeholder="Project Display Name" />
							</div>
						</div>
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
								<input type="password" ng-model="gerritPassword"
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
						<div class="form-group">
							<label class="col-sm-2 control-label">Host Port:</label>
							<div class="col-sm-10">
								<input type="number" ng-model="gerritHostPort"
									placeholder="Gerrit Server Host Port" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Topic Filter Regex:</label>
							<div class="col-sm-10">
								<input type="text" ng-model="gerritTopicRegex"
									placeholder="Topics to ignore as a regex (leave blank for none)" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Gerrit Thread Split Size:</label>
							<div class="col-sm-10">
								<input type="number" ng-model="gerritThreadSplitSize"
									placeholder="Thread size to split Gerrit requests" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Start Date Offset (Days):</label>
							<div class="col-sm-10">
								<input type="number" ng-model="gerritStartDateOffset"
									placeholder="Start Date Offset In Days" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">End Date Offset (Days):</label>
							<div class="col-sm-10">
								<input type="number" ng-model="gerritEndDateOffset"
									placeholder="End Date Offset In Days" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Project Include Regex:</label>
							<div class="col-sm-10">
								<input type="text" ng-model="gerritProjectRegex"
									placeholder="Projects to include as a regex" />
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
			</div>
		</div>
	</div>
</body>
</html>