function UpdateSonarConfig(data, $scope, $location){
	$scope.sonarHostname = data.host;
	$scope.sonarHostPort = data.hostPort;
	$scope.sonarUsername = data.username;
	$scope.sonarPassword = data.password;
	$scope.sonarProjectRegex = data.projectRegex;
	$scope.methodComplexityTarget = data.methodComplexityTarget;
	$scope.fileComplexityTarget = data.fileComplexityTarget;
	$scope.testCoverageTarget = data.testCoverageTarget;
	$scope.rulesComplianceTarget = data.rulesComplianceTarget;
	
	//Override from URL Param For multiple projects.
	if(typeof $location !== "undefined"){
		if(typeof $location.search().sonarProjectRegex !== "undefined"){
			$scope.sonarProjectRegex = $location.search().sonarProjectRegex
		}
	}
}

function UpdateSonarStats(data, $scope){
	var dates = Object.keys(data).sort();
	var months = [];
	var fileComplexity = [];
	var fileComplexityTarget = [];
	var methodComplexity = [];
	var methodComplexityTarget = [];
	var testCoverage = [];
	var testCoverageTarget = [];
	var rulesCompliance = [];
	var rulesComplianceTarget = [];
	
	for (d in dates){
		months.push(moment(dates[d]).format('MMMM'));
		fileComplexity.push(data[dates[d]].fileComplexity);
		fileComplexityTarget.push(data[dates[d]].fileComplexityTarget);
		methodComplexity.push(data[dates[d]].methodComplexity);
		methodComplexityTarget.push(data[dates[d]].methodComplexityTarget);
		testCoverage.push(data[dates[d]].testCoverage);
		testCoverageTarget.push(data[dates[d]].testCoverageTarget);
		rulesCompliance.push(data[dates[d]].rulesCompliance);
		rulesComplianceTarget.push(data[dates[d]].rulesComplianceTarget);
	}
	
	
	var latestStatsKey = Object.keys(data).sort().reverse()[0];
	var latestStats = data[latestStatsKey];
	$scope.methodComplexity = latestStats.methodComplexity;
	$scope.fileComplexity = latestStats.fileComplexity;
	$scope.testCoverage = latestStats.testCoverage;
	$scope.rulesCompliance = latestStats.rulesCompliance;
	$scope.linesOfCode = latestStats.linesOfCode;
	$scope.lineChartOptions = {
			animation : true,
		    scaleShowGridLines : true,
		    scaleGridLineColor : "rgba(0,0,0,.05)",
		    scaleGridLineWidth : 1,
		    scaleShowHorizontalLines: true,
		    scaleShowVerticalLines: true,
		    bezierCurve : false,
		    pointDot : true,
		    pointDotRadius : 4,
		    pointDotStrokeWidth : 1,
		    pointHitDetectionRadius : 20,
		    datasetStroke : true,
		    datasetStrokeWidth : 2,
		    datasetFill : false,
		    responsive : true		    
	};
	var fileComplexityChartData = {
	   datasets:[
	             {
	            	 label: "File Complexity",
			            fillColor: "rgba(151,187,205,0.2)",
			            strokeColor: "rgba(151,187,205,1)",
			            pointColor: "rgba(151,187,205,1)",
			            pointStrokeColor: "#fff",
			            pointHighlightFill: "#fff",
			            pointHighlightStroke: "rgba(151,187,205,1)"
	             },
	             {
	            	 label: "File Complexity Target",
			            fillColor: "rgba(255,0,0,0.3)",
			            strokeColor: "rgba(255,0,0,0.3)",
			            pointColor: "rgba(255,0,0,0.3)",
			            pointStrokeColor: "#fff",
			            pointHighlightFill: "#fff",
			            pointHighlightStroke: "rgba(151,187,205,1)"
	             }
	             ]
	};
	var methodComplexityChartData = {
			   datasets:[
			             {
			            	 label: "Method Complexity",
					            fillColor: "rgba(151,187,205,0.2)",
					            strokeColor: "rgba(151,187,205,1)",
					            pointColor: "rgba(151,187,205,1)",
					            pointStrokeColor: "#fff",
					            pointHighlightFill: "#fff",
					            pointHighlightStroke: "rgba(151,187,205,1)"
			             },
			             {
			            	 label: "Method Complexity Target",
					            fillColor: "rgba(255,0,0,0.3)",
					            strokeColor: "rgba(255,0,0,0.3)",
					            pointColor: "rgba(255,0,0,0.3)",
					            pointStrokeColor: "#fff",
					            pointHighlightFill: "#fff",
					            pointHighlightStroke: "rgba(151,187,205,1)"
			             }
			             ]
			};
	var testCoverageChartData = {
			   datasets:[
			             {
			            	 label: "Test Coverage",
					            fillColor: "rgba(151,187,205,0.2)",
					            strokeColor: "rgba(151,187,205,1)",
					            pointColor: "rgba(151,187,205,1)",
					            pointStrokeColor: "#fff",
					            pointHighlightFill: "#fff",
					            pointHighlightStroke: "rgba(151,187,205,1)"
			             },
			             {
			            	 label: "Test Coverage Target",
					            fillColor: "rgba(255,0,0,0.3)",
					            strokeColor: "rgba(255,0,0,0.3)",
					            pointColor: "rgba(255,0,0,0.3)",
					            pointStrokeColor: "#fff",
					            pointHighlightFill: "#fff",
					            pointHighlightStroke: "rgba(151,187,205,1)"
			             }
			             ]
			};
	var rulesComplianceChartData = {
			   datasets:[
			             {
			            	 label: "Rules Compliance",
					            fillColor: "rgba(151,187,205,0.2)",
					            strokeColor: "rgba(151,187,205,1)",
					            pointColor: "rgba(151,187,205,1)",
					            pointStrokeColor: "#fff",
					            pointHighlightFill: "#fff",
					            pointHighlightStroke: "rgba(151,187,205,1)"
			             },
			             {
			            	 label: "Rules Compliance Target",
					            fillColor: "rgba(255,0,0,0.3)",
					            strokeColor: "rgba(255,0,0,0.3)",
					            pointColor: "rgba(255,0,0,0.3)",
					            pointStrokeColor: "#fff",
					            pointHighlightFill: "#fff",
					            pointHighlightStroke: "rgba(151,187,205,1)"
			             }
			             ]
			};
	fileComplexityChartData.labels = months;
	fileComplexityChartData.datasets[0].data = fileComplexity;
	fileComplexityChartData.datasets[1].data = fileComplexityTarget;
	methodComplexityChartData.labels = months;
	methodComplexityChartData.datasets[0].data = methodComplexity;
	methodComplexityChartData.datasets[1].data = methodComplexityTarget;
	testCoverageChartData.labels = months;
	testCoverageChartData.datasets[0].data = testCoverage;
	testCoverageChartData.datasets[1].data = testCoverageTarget;
	rulesComplianceChartData.labels = months;
	rulesComplianceChartData.datasets[0].data = rulesCompliance;
	rulesComplianceChartData.datasets[1].data = rulesComplianceTarget;
	$scope.fileComplexityChartData = fileComplexityChartData;
	$scope.methodComplexityChartData = methodComplexityChartData;
	$scope.testCoverageChartData = testCoverageChartData;
	$scope.rulesComplianceChartData = rulesComplianceChartData;
	
}

function GetReviewRowClassTarget(value, target) {
	if (value === undefined) {
		return "list-group-item list-group-item-info";
	} else if (value > target) {
		return "list-group-item list-group-item-success";
	} else if (value == target) {
		return "list-group-item list-group-item-warning";
	} else {
		return "list-group-item list-group-item-danger";
	}
}

function GetReviewRowClassLimit(value, target) {
	if (value === undefined) {
		return "list-group-item list-group-item-info";
	} else if (value > target) {
		return "list-group-item list-group-item-danger";
	} else if (value == target) {
		return "list-group-item list-group-item-warning";
	} else {
		return "list-group-item list-group-item-success";
	}
}

function SonarStats($http, $scope, $timeout, $location, $log, $q, Sonar) {
	
	Sonar.configInfo().then(function(response){
		UpdateSonarConfig(response.data, $scope, $location);
	});
	
	$scope.manuallyRefreshSonarData = function(){
		$http.get('/sonar/stats/refreshCache').then(function(){$http.get('/sonar/stats/statistics/'+$scope.sonarProjectRegex+'/all').then(function(response){UpdateSonarStats(response.data, $scope);})});		
	}
	
	$scope.manuallyRefreshSonarData();
	
	$scope.getFileComplexityClass = function(value){
		return GetReviewRowClassLimit(value, $scope.fileComplexityTarget);		
	};
	
	$scope.getTestCoverageClass = function(value){
		return GetReviewRowClassTarget(value, $scope.testCoverageTarget);		
	};
	
	$scope.getMethodComplexityClass = function(value){
		return GetReviewRowClassLimit(value, $scope.methodComplexityTarget);		
	};
	
	$scope.getRulesComplianceClass = function(value){
		return GetReviewRowClassTarget(value, $scope.rulesComplianceTarget);		
	};
}

appGerritStatsModule.controller('SonarStatsCtrl', [ '$http', '$scope', '$timeout', '$location', '$log',
                                             		'$q', 'Sonar', SonarStats ]);