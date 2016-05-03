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
<body ng-controller="SonarConfigCtrl as vm">
	<c:set var="imagePath" value="../../images"/>
	<%@include file="../common/navbar.jsp"%>
	<div class="container-fluid">
		<div class="col-sm-12">
			<!-- PAGE TITLE -->
			<div class="page-header col-mid-12">
				<h1>
					<span class="fa fa-cogs"></span> Sonar Config
					<button class="btn btn-primary" ng-click="vm.downloadSonarConfig()">
						<span class="fa fa-download"></span>  Download
						Config
					</button>
					<button class="btn btn-primary" ngf-select="" ng-model="files"
						ngf-multiple="true" class="ng-pristine ng-valid">
						<span class="fa fa-upload"></span>  Upload Config
					</button>
				</h1>
					<button class="btn btn-primary" ng-click="vm.ReloadSonarCache()">
						<span class="fa fa-refresh"></span>  Reload Statistics
					</button>
					<alert ng-repeat="alert in alerts" type="{{alert.type}}" close="CloseSonarAlert($index)">{{alert.msg}}</alert>
			</div>

			<div class="page-header col-mid-12">
				<h3>
					<span class="fa fa-cogs"></span> Server Config
				</h3>
			</div>
			<div class="col-sm-12">
				<div class="col-sm-6">
					<form ng-submit="vm.onSubmit()" name="vm.form" novalidate>
				        <formly-form model="vm.model" fields="vm.fields" options="vm.options" form="vm.form">
				          <button type="submit" class="btn btn-success submit-button" ng-disabled="vm.form.$invalid"><span class="fa fa-save"></span>  Save Changes</button>
				        </formly-form>
			      	</form>
				</div>
				<div class="col-sm-6">
		            <button class="btn btn-primary" ng-click="TestSonarConnection()"><span class="fa fa-server"></span>  Test Sonar Connection (Save Changes First)</button>
		            <br/>
					<label for="comment">Connection Details:</label>
					<input ng-model="sonarConnectionDetails" class="form-control" id="comment"></input>
					<label for="comment">Test Result:</label>
					<textarea ng-model="sonarConnectionResult" class="form-control" rows="40" id="comment"></textarea>        
				</div>
			</div>
		</div>
	</div>
	<%@include file="../common/footer.jspf"%>
</body>
</html>