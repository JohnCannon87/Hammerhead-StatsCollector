<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html ng-app="app">
<head>
<%@include file="../common/allPages.jsp"%>
<%@include file="../common/gerrit.jsp"%>
<%@include file="../common/sonar.jsp"%>
<%@include file="../common/charting.jsp"%>
<%@include file="../common/configPages.jsp"%>
</head>
<body ng-controller="SonarConfigCtrl">
	<%@include file="../common/navbar.jsp"%>
	<div class="container-fluid">
		<div class="col-sm-12">
			<!-- PAGE TITLE -->
			<div class="page-header col-mid-12">
				<h1>
					<span class="fa fa-cogs"></span> Sonar Config
					<button class="btn btn-primary" ng-click="downloadConfig()">
						<span class="fa fa-download"></span>  Download
						Config
					</button>
					<button class="btn btn-primary" ngf-select="" ng-model="files"
						ngf-multiple="true" class="ng-pristine ng-valid">
						<span class="fa fa-upload"></span>  Upload Config
					</button>
					</button>
				</h1>
			</div>

			<div class="page-header col-mid-12">
				<h3>
					<span class="fa fa-cogs"></span> Server Config
				</h3>
			</div>
			<div class="col-sm-12">
				<div class="col-sm-6">
					<form class="form-horizontal">
						<div class="form-group">
							<label class="col-sm-2 control-label">Username:</label>
							<div class="col-sm-10">
								<input type="text" ng-model="sonarUsername"
									placeholder="Sonar Server Username" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Password:</label>
							<div class="col-sm-10">
								<input type="password" ng-model="sonarPassword"
									placeholder="Sonar Server Password" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Hostname:</label>
							<div class="col-sm-10">
								<input type="text" ng-model="sonarHostname"
									placeholder="Sonar Server Hostname" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Host Port:</label>
							<div class="col-sm-10">
								<input type="number" ng-model="sonarHostPort"
									placeholder="Sonar Server Host Port" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Project Include
								Regex:</label>
							<div class="col-sm-10">
								<input type="text" ng-model="sonarProjectRegex"
									placeholder="Projects to include as a regex" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Fill Target Area:</label>
							<div class="col-sm-10">
								<input type="checkbox" ng-model="fillTargetArea"/>
							</div>
						</div>
					</form>
					<hr>
					<table class="table table-striped">
						<thead>
							<tr>
								<th>Value</th>
								<th>Target/Limit (%)</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>Method Complexity Target</td>
								<td><input type="text" ng-model="methodComplexityTarget"
									placeholder="Method Complexity Target" /></td>
							</tr>
							<tr>
								<td>File Complexity Target</td>
								<td><input type="text" ng-model="fileComplexityTarget"
									placeholder="File Complexity Target" /></td>
							</tr>
							<tr>
								<td>Test Coverage Target</td>
								<td><input type="text" ng-model="testCoverageTarget"
									placeholder="Test Target" /></td>
							</tr>
							<tr>
								<td>Rules Compliance Target</td>
								<td><input type="text" ng-model="rulesComplianceTarget"
									placeholder="Rules Compliance Target" /></td>
							</tr>
						</tbody>
					</table>
					<button class="btn btn-success" ng-click="saveSonarConfig()">
						<span class="fa fa-save"></span>  Save Changes
					</button>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../common/footer.jspf"%>
</body>
</html>