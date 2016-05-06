var appStatsModule = angular.module('appStats', ['tc.chartjs', 'ui.bootstrap', 'ngFileUpload', 'ngRoute'])
.config(function ($routeProvider, $locationProvider){
	//routing DOESN'T work without html5Mode
	$locationProvider.html5Mode({
		  enabled: true,
		  requireBase: false
		});
});

function GetReviewRowClassTarget(value, target) {
	if (value === undefined) {
		return "list-group-item list-group-item";
	} else if (Number(value) > Number(target)) {
		return "list-group-item list-group-item-success";
	} else if (Number(value) == Number(target)) {
		return "list-group-item list-group-item-warning";
	} else {
		return "list-group-item list-group-item-danger";
	}
}

function GetReviewRowClassLimit(value, target) {
	if (value === undefined) {
		return "list-group-item list-group-item";
	} else if (Number(value) > Number(target)) {
		return "list-group-item list-group-item-danger";
	} else if (Number(value) == Number(target)) {
		return "list-group-item list-group-item-warning";
	} else {
		return "list-group-item list-group-item-success";
	}
}

function GetDimReviewRowClassTarget(value, target) {
	if (value === undefined) {
		return "list-group-item list-group-item";
	} else if (Number(value) > Number(target)) {
		return "list-group-item list-group-item-success-dim";
	} else if (Number(value) === Number(target)) {
		return "list-group-item list-group-item-warning-dim";
	} else {
		return "list-group-item list-group-item-danger-dim";
	}
}

function GetDimReviewRowClassLimit(value, target) {
	if (value === undefined) {
		return "list-group-item list-group-item";
	} else if (Number(value) > Number(target)) {
		return "list-group-item list-group-item-danger-dim";
	} else if (Number(value) === Number(target)) {
		return "list-group-item list-group-item-warning-dim";
	} else {
		return "list-group-item list-group-item-success-dim";
	}
}

function SetupChartOptions($scope) {
	Chart.types.Line.extend({
		name : "LineAlt",
		draw : function() {
			Chart.types.Line.prototype.draw.apply(this, arguments);

			var ctx = this.chart.ctx;
			var scale = this.scale;

			ctx.save();

			if (this.options.upwardsFill) {
				ctx.fillStyle = this.datasets[1].fillColor;
				ctx.beginPath();
				ctx.moveTo(scale.calculateX(0), scale.startPoint);
				this.datasets[1].points.forEach(function(point) {
					ctx.lineTo(scale.calculateX(0), scale
							.calculateY(point.value));
					ctx.lineTo(point.x, point.y);
					ctx.lineTo(point.x, scale.startPoint);
				});
				ctx.closePath();
				ctx.fill();
			}

			if (this.options.downwardsFill) {
				ctx.beginPath();
				ctx.fillStyle = this.datasets[1].fillColor;
				ctx.moveTo(scale.calculateX(0), scale.endPoint)
				this.datasets[1].points.forEach(function(point) {
					ctx.lineTo(scale.calculateX(0), scale
							.calculateY(point.value));
					ctx.lineTo(point.x, point.y);
					ctx.lineTo(point.x, scale.endPoint);
				})
				ctx.closePath();
				ctx.fill();
			}

			ctx.restore();
		}
	});
	
	$scope.gerritChartOptions = {

			// Sets the chart to be responsive
			responsive : true,

			// Boolean - Whether we should show a stroke on each segment
			segmentShowStroke : true,

			// String - The colour of each segment stroke
			segmentStrokeColor : 'rgba(255,255,255,0)',

			// Number - The width of each segment stroke
			segmentStrokeWidth : 2,

			// Number - The percentage of the chart that we cut out of the
			// middle
			percentageInnerCutout : 0, // This is 0 for Pie charts

			// Number - Amount of animation steps
			animationSteps : 100,

			// String - Animation easing effect
			// [easeInOutQuart, linear, easeOutBounce, easeInBack, easeInOutQuad,
			// easeOutQuart, easeOutQuad, easeInOutBounce, easeOutSine,
			// easeInOutCubic,
			// easeInExpo, easeInOutBack, easeInCirc, easeInOutElastic, easeOutBack,
			// easeInQuad, easeInOutExpo, easeInQuart, easeOutQuint, easeInOutCirc,
			// easeInSine, easeOutExpo, easeOutCirc, easeOutCubic, easeInQuint,
			// easeInElastic, easeInOutSine, easeInOutQuint, easeInBounce,
			// easeOutElastic, easeInCubic]
			animationEasing : 'easeOutBounce',

			// Boolean - Whether we animate the rotation of the Doughnut
			animateRotate : true,

			// Boolean - Whether we animate scaling the Doughnut from the
			// centre
			animateScale : true,

			// String - A legend template
			legendTemplate : '<ul class="tc-chart-js-legend"><% for (var i=0; i<segments.length; i++){%><li><span style="background-color:<%=segments[i].fillColor%>"></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>'

		};
}

function UpdateGerritConfig(data, $scope, $location) {
	$scope.gerritUrl = "http://"+data.host+":"+data.hostPort;
	$scope.gerritHostname = data.host;
	$scope.gerritHostPort = data.hostPort;
	$scope.reviewersToIgnore = data.reviewersToIgnore;
	$scope.gerritUsername = data.username;
	$scope.gerritPassword = data.password;
	$scope.gerritTopicRegex = data.topicRegex;
	$scope.gerritThreadSplitSize = data.threadSplitSize;
	$scope.gerritStartDateOffset = data.startDateOffset;
	$scope.gerritEndDateOffset = data.endDateOffset;
	$scope.noPeerReviewsTarget = data.noPeerReviewTarget;
	$scope.onePeerReviewTarget = data.onePeerReviewTarget;
	$scope.twoPeerReviewTarget = data.twoPeerReviewTarget;
	$scope.collaborativeReviewTarget = data.collaborativeReviewTarget;
	$scope.gerritProjectName = data.projectName;
	$scope.gerritProjectRegex = data.projectRegex;
	if(data.projectFilterOutRegex === undefined){
		$scope.gerritProjectFilterOutRegex = "";		
	}else{
		$scope.gerritProjectFilterOutRegex = data.projectFilterOutRegex;		
	}
	$scope.showGerritHistory = data.showGerritHistory;
	$scope.showGerritPie = data.showGerritPie;
	$scope.configLoaded = true;

	// Override from URL Param For multiple projects.
	if (typeof $location !== "undefined") {
		if (typeof $location.search().projectRegex !== "undefined") {
			$scope.gerritProjectRegex = $location.search().projectRegex
		}if (typeof $location.search().projectFilterOutRegex !== "undefined") {
			$scope.gerritProjectFilterOutRegex = $location.search().projectFilterOutRegex
		}
		if (typeof $location.search().projectName !== "undefined") {
			$scope.gerritProjectName = $location.search().projectName
		}
	}
}

function UpdateSonarConfig(data, $scope, $location) {
	$scope.sonarUrl = "http://"+data.host+":"+data.hostPort;
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

	// Override from URL Param For multiple projects.
	if (typeof $location !== "undefined") {
		if (typeof $location.search().sonarProjectRegex !== "undefined") {
			$scope.sonarProjectRegex = $location.search().sonarProjectRegex
		}
	}

	if (typeof $location !== "undefined") {
		if (typeof $location.search().sonarFileTarget !== "undefined") {
			$scope.fileComplexityTarget = $location.search().sonarFileTarget
		}
	}

	if (typeof $location !== "undefined") {
		if (typeof $location.search().sonarFileTarget !== "undefined") {
			$scope.methodComplexityTarget = $location.search().sonarMethodTarget
		}
	}

	if (typeof $location !== "undefined") {
		if (typeof $location.search().sonarFileTarget !== "undefined") {
			$scope.testCoverageTarget = $location.search().sonarTestTarget
		}
	}

	if (typeof $location !== "undefined") {
		if (typeof $location.search().sonarFileTarget !== "undefined") {
			$scope.rulesComplianceTarget = $location.search().sonarRulesTarget
		}
	}

	return data;
}

function GetGerritAuthorAndReviewerStats($http, $scope) {
	if ($scope.configLoaded == true) {
		if($scope.gerritProjectRegex !== undefined
				&& $scope.gerritStartDateOffset !== undefined
				&& $scope.gerritEndDateOffset !== undefined
				&& $scope.gerritProjectFilterOutRegex !== undefined){
			url = '/gerrit/review/authors/' + $scope.gerritStatus + '/'
			+ $scope.gerritProjectRegex + '/'
			+ $scope.gerritProjectFilterOutRegex + '/'
			+ $scope.gerritStartDateOffset + '/'
			+ $scope.gerritEndDateOffset;
		} else if ($scope.gerritProjectRegex !== undefined
				&& $scope.gerritStartDateOffset !== undefined
				&& $scope.gerritEndDateOffset !== undefined) {
			url = '/gerrit/review/authors/' + $scope.gerritStatus + '/'
					+ $scope.gerritProjectRegex + '/'
					+ $scope.gerritStartDateOffset + '/'
					+ $scope.gerritEndDateOffset;
		} else if ($scope.gerritProjectRegex === undefined
				&& $scope.gerritStartDateOffset !== undefined
				&& $scope.gerritEndDateOffset !== undefined) {
			url = '/gerrit/review/authors/' + $scope.gerritStatus + '/'
					+ $scope.gerritStartDateOffset + '/'
					+ $scope.gerritEndDateOffset;
		} else if ($scope.gerritProjectRegex !== undefined
				&& ($scope.gerritStartDateOffset !== undefined || $scope.gerritEndDateOffset !== undefined)) {
			url = '/gerrit/review/authors/' + $scope.gerritStatus + '/'
					+ $scope.gerritProjectRegex;
		}
		$http.get(url).then(function(response) {
			$scope.authors = response.data.authorsCountList;
			$scope.reviewers = response.data.reviewersCountList;
		});
	}
}

function GetGerritStats($http, $scope) {
	if ($scope.configLoaded) {
		if($scope.gerritProjectRegex !== undefined
				&& $scope.gerritStartDateOffset !== undefined
				&& $scope.gerritEndDateOffset !== undefined
				&& $scope.gerritProjectFilterOutRegex !== undefined){
			url = '/gerrit/review/' + $scope.gerritStatus + '/'
			+ $scope.gerritProjectRegex + '/'
			+ $scope.gerritProjectFilterOutRegex + '/'
			+ $scope.gerritStartDateOffset + '/'
			+ $scope.gerritEndDateOffset;
		} else if ($scope.gerritProjectRegex !== undefined
				&& $scope.gerritStartDateOffset !== undefined
				&& $scope.gerritEndDateOffset !== undefined) {
			url = '/gerrit/review/' + $scope.gerritStatus + '/'
					+ $scope.gerritProjectRegex + '/'
					+ $scope.gerritStartDateOffset + '/'
					+ $scope.gerritEndDateOffset;
		} else if ($scope.gerritProjectRegex === undefined
				&& $scope.gerritStartDateOffset !== undefined
				&& $scope.gerritEndDateOffset !== undefined) {
			url = '/gerrit/review/' + $scope.gerritStatus + '/'
					+ $scope.gerritStartDateOffset + '/'
					+ $scope.gerritEndDateOffset;
		} else if ($scope.gerritProjectRegex !== undefined
				&& ($scope.gerritStartDateOffset !== undefined || $scope.gerritEndDateOffset !== undefined)) {
			url = '/gerrit/review/' + $scope.gerritStatus + '/'
					+ $scope.gerritProjectRegex;
		} else {
			url = '/gerrit/review/' + $scope.gerritStatus + '/all';
		}
		$http
				.get(url)
				.then(
						function(response) {
							$scope.showGerritData = true;
							if (response.data.noPeerReviewList.length == 0
									&& response.data.onePeerReviewList.length == 0
									&& response.data.twoPlusPeerReviewList.length == 0
									&& response.data.collabrativeDevelopmentList.length == 0) {
								$scope.showGerritData = false;
							}
							$scope.noPeerReviewers = response.data.noPeerReviewCount;
							$scope.onePeerReviewers = response.data.onePeerReviewCount;
							$scope.twoPeerReviewers = response.data.twoPlusPeerReviewCount;
							$scope.collabrativeDevelopments = response.data.collabrativeDevelopmentCount;
							$scope.totalReviews = response.data.totalReviewsCount;
							$scope.noPeerPercentage = response.data.noPeerReviewPercentage;
							$scope.onePeerPercentage = response.data.onePeerReviewPercentage;
							$scope.twoPeerPercentage = response.data.twoPlusPeerReviewPercentage;
							$scope.collaborativePercentage = response.data.collabrativeDevelopmentPercentage;
							$scope.noPeerReviews = response.data.noPeerReviewList;
							$scope.onePeerReviews = response.data.onePeerReviewList;
							$scope.twoPeerReviews = response.data.twoPlusPeerReviewList;
							$scope.collabrativeDevelopment = response.data.collabrativeDevelopmentList;

							var changeCountHistory = response.data.changeCountHistory;
							var days = [];
							var noPeerReviewCount = [];
							var actualReviewCount = [];
							var dates = Object.keys(changeCountHistory).sort();
							var maxCount = 0;
							for (d in dates) {
								days.push(moment(dates[d]).format('MM-DD'));
								noPeerReviewCount
										.push(changeCountHistory[dates[d]].noPeerReviewCount);
								if (maxCount < changeCountHistory[dates[d]].noPeerReviewCount) {
									maxCount = changeCountHistory[dates[d]].noPeerReviewCount;
								}
								totalActualReviewCount = changeCountHistory[dates[d]].onePeerReviewCount + changeCountHistory[dates[d]].twoPeerReviewCount + changeCountHistory[dates[d]].collaborativeDevelopmentCount;
								if(maxCount < totalActualReviewCount){
									maxCount = totalActualReviewCount;
								}
								actualReviewCount.push(totalActualReviewCount);
							}

							var noFillColor = "#CC0000";
							var noStrokeColor = "#CC0000";
							var noPointColor = "#CC0000";
							var noPointStrokeColor = "#CC0000";
							var noPointHighlightFill = "#CC0000";
							var noPointHighlightStroke = "#CC0000";
							var oneFillColor = "#FFFFFF";
							var oneStrokeColor = "#FFFFFF";
							var onePointColor = "#FFFFFF";
							var onePointStrokeColor = "#FFFFFF";
							var onePointHighlightFill = "#FFFFFF";
							var onePointHighlightStroke = "#FFFFFF";

							var scaleWidth = 2;
							if(maxCount > 20){
								scaleWidth = 5;
							}
							
							$scope.lineChartOptionsUpwards = {
								scaleOverride : true,
								scaleStartValue : 0,
								scaleSteps : (maxCount / scaleWidth) + 1,
								scaleStepWidth : scaleWidth,
								scaleLabel : function(object) {
									return "  " + object.value;
								},
								animation : true,
								scaleFontColor : "#c8c8c8",
								scaleShowGridLines : true,
								scaleGridLineColor : "rgba(255,255,255,.1)",
								scaleGridLineWidth : 1,
								scaleShowHorizontalLines : true,
								scaleShowVerticalLines : true,
								bezierCurve : true,
								pointDot : true,
								pointDotRadius : 2,
								pointDotStrokeWidth : 1,
								pointHitDetectionRadius : 15,
								datasetStroke : true,
								datasetStrokeWidth : 2,
								datasetFill : false,
								responsive : true
							};

							var gerritHistoryChartData = {
								datasets : [
										{
											label : "Actual Review Count",
											fillColor : oneFillColor,
											strokeColor : oneStrokeColor,
											pointColor : onePointColor,
											pointStrokeColor : onePointStrokeColor,
											pointHighlightFill : onePointHighlightFill,
											pointHighlightStroke : onePointHighlightStroke
										},
										{
											label : "No Peer Review",
											fillColor : noFillColor,
											strokeColor : noStrokeColor,
											pointColor : noPointColor,
											pointStrokeColor : noPointStrokeColor,
											pointHighlightFill : noPointHighlightFill,
											pointHighlightStroke : noPointHighlightStroke
										} ]
							};

							gerritHistoryChartData.labels = days;
							gerritHistoryChartData.datasets[0].data = actualReviewCount;
							gerritHistoryChartData.datasets[1].data = noPeerReviewCount;
							$scope.gerritHistoryChartData = gerritHistoryChartData;

							if (!response.data.error) {
								$scope.gerritChartData = [
										{
											value : response.data.noPeerReviewCount,
											color : "#CC0000",
											highlight : "#CC0000",
											label : "No Peer Reviews"
										},
										{
											value : response.data.onePeerReviewCount,
											color : "#009933",
											highlight : "#009933",
											label : "One Peer Review"
										},
										{
											value : response.data.twoPlusPeerReviewCount,
											color : "#0099FF",
											highlight : "#0099FF",
											label : "Two Peer Review"
										},
										{
											value : response.data.collabrativeDevelopmentCount,
											color : "#6600FF",
											highlight : "#6600FF",
											label : "Collaborative Development"
										} ];
							}
						});
	}
}

function GetSonarStats($http, $scope){
	$http.get('/sonar/stats/statistics/' + $scope.sonarProjectRegex + '/all').then(
			function(response) {
				UpdateSonarStats(response.data, $scope);
			})
}

function UpdateSonarStats(data, $scope) {
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

	$scope.showSonarData = true;
	if(dates.length == 0){
		$scope.showSonarData = false;
	}
	
	for (d in dates) {
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
		scaleLabel : function(object) {
			return "  " + object.value;
		},
		animation : true,
		scaleFontColor : "#c8c8c8",
		scaleShowGridLines : true,
		scaleGridLineColor : "rgba(255,255,255,.1)",
		scaleGridLineWidth : 1,
		scaleShowHorizontalLines : true,
		scaleShowVerticalLines : true,
		bezierCurve : true,
		pointDot : true,
		pointDotRadius : 2,
		pointDotStrokeWidth : 1,
		pointHitDetectionRadius : 15,
		datasetStroke : true,
		datasetStrokeWidth : 2,
		datasetFill : false,
		upwardsFill : $scope.fillTargetArea && true,
		downwardsFill : false,
		responsive : true
	};
	$scope.lineChartOptionsDownwards = {
		scaleLabel : function(object) {
			return "  " + object.value;
		},
		animation : true,
		scaleFontColor : "#c8c8c8",
		scaleShowGridLines : true,
		scaleGridLineColor : "rgba(255,255,255,.1)",
		scaleGridLineWidth : 1,
		scaleShowHorizontalLines : true,
		scaleShowVerticalLines : true,
		bezierCurve : true,
		pointDot : true,
		pointDotRadius : 2,
		pointDotStrokeWidth : 1,
		pointHitDetectionRadius : 15,
		datasetStroke : true,
		datasetStrokeWidth : 2,
		datasetFill : false,
		upwardsFill : false,
		downwardsFill : $scope.fillTargetArea && true,
		responsive : true
	};

	var scoreFillColor = "#fff";
	var scoreStrokeColor = "#fff";
	var scorePointColor = "#fff";
	var scorePointStrokeColor = "#fff";
	var scorePointHighlightFill = "#fff";
	var scorePointHighlightStroke = "#fff";
	var targetFillColor = "rgba(204,0,0,0.3)";
	var targetStrokeColor = "#CC0000";
	var targetPointColor = "#CC0000";
	var targetPointStrokeColor = "rgba(204,0,0,1)";
	var targetPointHighlightFill = "rgba(204,0,0,1)";
	var targetPointHighlightStroke = "rgba(204,0,0,1)";

	var fileComplexityChartData = {
		datasets : [ {
			label : "File Complexity",
			fillColor : scoreFillColor,
			strokeColor : scoreStrokeColor,
			pointColor : scorePointColor,
			pointStrokeColor : scorePointStrokeColor,
			pointHighlightFill : scorePointHighlightFill,
			pointHighlightStroke : scorePointHighlightStroke
		}, {
			label : "File Complexity Target",
			fillColor : targetFillColor,
			strokeColor : targetStrokeColor,
			pointColor : targetPointColor,
			pointStrokeColor : targetPointStrokeColor,
			pointHighlightFill : targetPointHighlightFill,
			pointHighlightStroke : targetPointHighlightStroke
		} ]
	};
	var methodComplexityChartData = {
		datasets : [ {
			label : "Method Complexity",
			fillColor : scoreFillColor,
			strokeColor : scoreStrokeColor,
			pointColor : scorePointColor,
			pointStrokeColor : scorePointStrokeColor,
			pointHighlightFill : scorePointHighlightFill,
			pointHighlightStroke : scorePointHighlightStroke
		}, {
			label : "Method Complexity Target",
			fillColor : targetFillColor,
			strokeColor : targetStrokeColor,
			pointColor : targetPointColor,
			pointStrokeColor : targetPointStrokeColor,
			pointHighlightFill : targetPointHighlightFill,
			pointHighlightStroke : targetPointHighlightStroke
		} ]
	};
	var testCoverageChartData = {
		datasets : [ {
			label : "Test Coverage",
			fillColor : scoreFillColor,
			strokeColor : scoreStrokeColor,
			pointColor : scorePointColor,
			pointStrokeColor : scorePointStrokeColor,
			pointHighlightFill : scorePointHighlightFill,
			pointHighlightStroke : scorePointHighlightStroke
		}, {
			label : "Test Coverage Target",
			fillColor : targetFillColor,
			strokeColor : targetStrokeColor,
			pointColor : targetPointColor,
			pointStrokeColor : targetPointStrokeColor,
			pointHighlightFill : targetPointHighlightFill,
			pointHighlightStroke : targetPointHighlightStroke
		} ]
	};
	var rulesComplianceChartData = {
		datasets : [ {
			label : "Rules Compliance",
			fillColor : scoreFillColor,
			strokeColor : scoreStrokeColor,
			pointColor : scorePointColor,
			pointStrokeColor : scorePointStrokeColor,
			pointHighlightFill : scorePointHighlightFill,
			pointHighlightStroke : scorePointHighlightStroke
		}, {
			label : "Rules Compliance Target",
			fillColor : targetFillColor,
			strokeColor : targetStrokeColor,
			pointColor : targetPointColor,
			pointStrokeColor : targetPointStrokeColor,
			pointHighlightFill : targetPointHighlightFill,
			pointHighlightStroke : targetPointHighlightStroke
		} ]
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

function GetAvatarClass(avatarUrl) {
	if (typeof avatarUrl === "undefined") {
		return "hidden";
	} else {
		return "img-circle";
	}
}

function StatsCtrl($http, $scope, $rootScope, $location, $log, $q, $interval, Gerrit, Sonar){
	//Timers
	$interval(function(){
		GetSonarStats($http, $scope);
	}, 5000);
	$interval(function(){
		GetGerritAuthorAndReviewerStats($http, $scope);
		GetGerritStats($http, $scope);
	}, 5000);
	
	//Initial Values
	$scope.showGerritData = true;
	$scope.showSonarData = true;
	$scope.metrics = [];
	$scope.gerritStatus = 'merged';
	if ($scope.configLoaded === undefined) {
		$scope.configLoaded = false;
	}
	SetupChartOptions($scope);
	
	//Functions
	$scope.GetAvatarClass = GetAvatarClass;
	$scope.getNoPeerReviewRowClass = function(percentage) {
		return GetDimReviewRowClassLimit(percentage, $scope.noPeerReviewsTarget);
	};
	$scope.getOnePeerReviewRowClass = function(percentage, percentage1,
			percentage2) {
		return GetDimReviewRowClassTarget(percentage + percentage1
				+ percentage2, $scope.onePeerReviewTarget);
	};
	$scope.getTwoPeerReviewRowClass = function(percentage, percentage1) {
		return GetDimReviewRowClassTarget(percentage + percentage1,
				$scope.twoPeerReviewTarget);
	};
	$scope.getCollabrativeDevelopmentRowClass = function(percentage) {
		return GetDimReviewRowClassTarget(percentage,
				$scope.collaborativeReviewTarget);
	};
	$scope.getFileComplexityClass = function(value) {
		return GetReviewRowClassLimit(value, $scope.fileComplexityTarget);
	};
	$scope.getTestCoverageClass = function(value) {
		return GetReviewRowClassTarget(value, $scope.testCoverageTarget);
	};
	$scope.getMethodComplexityClass = function(value) {
		return GetReviewRowClassLimit(value, $scope.methodComplexityTarget);
	};
	$scope.getRulesComplianceClass = function(value) {
		return GetReviewRowClassTarget(value, $scope.rulesComplianceTarget);
	};		
	$scope.GetSonarStatsClass = function(){
		return GetSonarStatsClass($scope);
	};
	$scope.GetGerritStatsClass = function() {
		return GetGerritStatsClass($scope);
	};
	
	//Watches
	$scope.$watch('configLoaded', function() {
		GetGerritStats($http, $scope);
		GetGerritAuthorAndReviewerStats($http, $scope);
	}, true);
	
	//Initial Calls
	Gerrit.configInfo().then(function(response) {
		UpdateGerritConfig(response.data, $scope, $location);
		GetGerritStats($http, $scope);
		GetGerritAuthorAndReviewerStats($http, $scope);
	});
	Sonar.configInfo().then(function(response) {
		UpdateSonarConfig(response.data, $scope, $location);
		GetSonarStats($http, $scope);
	});
}

appStatsModule.controller('StatsCtrl', [ '$http', '$scope', '$rootScope', '$location', '$log', '$q', '$interval', 'Gerrit', 'Sonar', StatsCtrl ]);