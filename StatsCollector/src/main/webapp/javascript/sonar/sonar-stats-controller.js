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
	$scope.fillTargetArea = data.fillTargetArea;
	
	//Override from URL Param For multiple projects.
	if(typeof $location !== "undefined"){
		if(typeof $location.search().sonarProjectRegex !== "undefined"){
			$scope.sonarProjectRegex = $location.search().sonarProjectRegex
		}
	}
	
	if(typeof $location !== "undefined"){
		if(typeof $location.search().sonarFileTarget !== "undefined"){
			$scope.fileComplexityTarget = $location.search().sonarFileTarget
		}
	}
	
	if(typeof $location !== "undefined"){
		if(typeof $location.search().sonarFileTarget !== "undefined"){
			$scope.methodComplexityTarget = $location.search().sonarMethodTarget
		}
	}
	
	if(typeof $location !== "undefined"){
		if(typeof $location.search().sonarFileTarget !== "undefined"){
			$scope.testCoverageTarget = $location.search().sonarTestTarget
		}
	}
	
	if(typeof $location !== "undefined"){
		if(typeof $location.search().sonarFileTarget !== "undefined"){
			$scope.rulesComplianceTarget = $location.search().sonarRulesTarget
		}
	}
	
	return data;
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
		fileComplexityTarget.push($scope.fileComplexityTarget);
		methodComplexity.push(data[dates[d]].methodComplexity);
		methodComplexityTarget.push($scope.methodComplexityTarget);
		testCoverage.push(data[dates[d]].testCoverage);
		testCoverageTarget.push($scope.testCoverageTarget);
		rulesCompliance.push(data[dates[d]].rulesCompliance);
		rulesComplianceTarget.push($scope.rulesComplianceTarget);
	}
	
	
	var latestStatsKey = Object.keys(data).sort().reverse()[0];
	var latestStats = data[latestStatsKey];
	$scope.methodComplexity = latestStats.methodComplexity;
	$scope.fileComplexity = latestStats.fileComplexity;
	$scope.testCoverage = latestStats.testCoverage;
	$scope.rulesCompliance = latestStats.rulesCompliance;
	$scope.linesOfCode = latestStats.linesOfCode;
	$scope.lineChartOptionsUpwards = {
			animation : true,
		    scaleShowGridLines : true,
		    scaleGridLineColor : "rgba(0,0,0,.05)",
		    scaleGridLineWidth : 1,
		    scaleShowHorizontalLines: true,
		    scaleShowVerticalLines: true,
		    bezierCurve : true,
		    pointDot : true,
		    pointDotRadius : 4,
		    pointDotStrokeWidth : 1,
		    pointHitDetectionRadius : 20,
		    datasetStroke : true,
		    datasetStrokeWidth : 2,
		    datasetFill : false,
		    upwardsFill : $scope.fillTargetArea && true,
		    downwardsFill : false,
		    responsive : true		    
	};
	$scope.lineChartOptionsDownwards = {
			animation : true,
		    scaleShowGridLines : true,
		    scaleGridLineColor : "rgba(0,0,0,.05)",
		    scaleGridLineWidth : 1,
		    scaleShowHorizontalLines: true,
		    scaleShowVerticalLines: true,
		    bezierCurve : true,
		    pointDot : true,
		    pointDotRadius : 4,
		    pointDotStrokeWidth : 1,
		    pointHitDetectionRadius : 20,
		    datasetStroke : true,
		    datasetStrokeWidth : 2,
		    datasetFill : false,
		    upwardsFill : false,
		    downwardsFill : $scope.fillTargetArea && true,
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
			            fillColor: "rgba(255,0,0,0.1)",
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
					            fillColor: "rgba(255,0,0,0.1)",
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
					            fillColor: "rgba(255,0,0,0.1)",
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
					            fillColor: "rgba(255,0,0,0.1)",
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
	
	Chart.types.Line.extend({
        name: "LineAlt",
        draw: function () {
            Chart.types.Line.prototype.draw.apply(this, arguments);

            var ctx = this.chart.ctx;
            var scale = this.scale;

            ctx.save();

            if(this.options.upwardsFill){
	            ctx.fillStyle = this.datasets[1].fillColor;
	            ctx.beginPath();
	            ctx.moveTo(scale.calculateX(0), scale.startPoint);	            
	            this.datasets[1].points.forEach(function (point) {
	                ctx.lineTo(scale.calculateX(0), scale.calculateY(point.value));
	                ctx.lineTo(point.x, point.y);
	                ctx.lineTo(point.x, scale.startPoint);
	            });
	            ctx.closePath();
	            ctx.fill();
        	}
			
            if(this.options.downwardsFill){
	            ctx.beginPath();
	            ctx.fillStyle = this.datasets[1].fillColor;
	            ctx.moveTo(scale.calculateX(0), scale.endPoint)
	            this.datasets[1].points.forEach(function (point) {
	                ctx.lineTo(scale.calculateX(0), scale.calculateY(point.value));
	                ctx.lineTo(point.x, point.y);
	                ctx.lineTo(point.x, scale.endPoint);
	            })
				ctx.closePath();
				ctx.fill();
        	}

            ctx.restore();
        }
    });

	
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