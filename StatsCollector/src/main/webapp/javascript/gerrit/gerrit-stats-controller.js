function GetGerritStatsClass($rootScope){
	if($rootScope.showSonarStats == null){
		$rootScope.showSonarStats = false;
	}
	if($rootScope.showSonarStats){
		return "col-sm-12 well";		
	}else{
		return "col-sm-3 well";
	}
}

function GetAvatarClass(avatarUrl){
	if(typeof avatarUrl === "undefined"){
		return "hiddenImg";
	}else{
		return "img-circle";
	}
}

function UpdateGerritConfig(data, $scope, $location) {
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
	$scope.configLoaded = true;
	$scope.gerritProjectName = data.projectName;
	$scope.gerritProjectRegex = data.projectRegex;
	$scope.showGerritHistory = data.showGerritHistory;
	$scope.showGerritPie = data.showGerritPie;
	
	//Override from URL Param For multiple projects.
	if(typeof $location !== "undefined"){
		if(typeof $location.search().projectRegex !== "undefined"){
			$scope.gerritProjectRegex = $location.search().projectRegex
		}
		if(typeof $location.search().projectName !== "undefined"){
			$scope.gerritProjectName = $location.search().projectName
		}
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

function GetGerritAuthorAndReviewerStats($http, $scope, $timeout){
	if($scope.configLoaded == true){
		if($scope.gerritProjectRegex !== undefined && $scope.gerritStartDateOffset !== undefined && $scope.gerritEndDateOffset !== undefined){
			url = '/gerrit/review/authors/'+$scope.gerritStatus+'/'+$scope.gerritProjectRegex+'/'+$scope.gerritStartDateOffset+'/'+$scope.gerritEndDateOffset;
		}else if($scope.gerritProjectRegex === undefined && $scope.gerritStartDateOffset !== undefined && $scope.gerritEndDateOffset !== undefined){
			url = '/gerrit/review/authors/'+$scope.gerritStatus+'/'+$scope.gerritStartDateOffset+'/'+$scope.gerritEndDateOffset;
		}else if($scope.gerritProjectRegex !== undefined && ($scope.gerritStartDateOffset !== undefined || $scope.gerritEndDateOffset !== undefined)){
			url = '/gerrit/review/authors/'+$scope.gerritStatus+'/'+$scope.gerritProjectRegex;
		}		
		$http.get(url)
		.then(
				function(response) {
				$scope.authors = response.data.authorsCountList;
				$scope.reviewers = response.data.reviewersCountList;
				});
	}
}

function GetGerritStats($http, $scope, $timeout){	
	if($scope.configLoaded){
		if($scope.gerritProjectRegex !== undefined && $scope.gerritStartDateOffset !== undefined && $scope.gerritEndDateOffset !== undefined){
			url = '/gerrit/review/'+$scope.gerritStatus+'/'+$scope.gerritProjectRegex+'/'+$scope.gerritStartDateOffset+'/'+$scope.gerritEndDateOffset;
		}else if($scope.gerritProjectRegex === undefined && $scope.gerritStartDateOffset !== undefined && $scope.gerritEndDateOffset !== undefined){
			url = '/gerrit/review/'+$scope.gerritStatus+'/'+$scope.gerritStartDateOffset+'/'+$scope.gerritEndDateOffset;
		}else if($scope.gerritProjectRegex !== undefined && ($scope.gerritStartDateOffset !== undefined || $scope.gerritEndDateOffset !== undefined)){
			url = '/gerrit/review/'+$scope.gerritStatus+'/'+$scope.gerritProjectRegex;
		}else{
			url = '/gerrit/review/'+$scope.gerritStatus+'/all';
		}
		$http.get(url)
		.then(
				function(response) {
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
					var days =[];
					var noPeerReviewCount = [];
					var onePeerReviewCount = [];
					var twoPeerReviewCount = [];
					var collaborativeDevelopmentCount = [];
					var dates = Object.keys(changeCountHistory).sort();					
					var maxCount = 0;
					for (d in dates){
						days.push(moment(dates[d]).format('MM-DD'));
						noPeerReviewCount.push(changeCountHistory[dates[d]].noPeerReviewCount);
						if(maxCount < changeCountHistory[dates[d]].noPeerReviewCount){
							maxCount = changeCountHistory[dates[d]].noPeerReviewCount;
						}
						onePeerReviewCount.push(changeCountHistory[dates[d]].onePeerReviewCount);
						if(maxCount < changeCountHistory[dates[d]].onePeerReviewCount){
							maxCount = changeCountHistory[dates[d]].onePeerReviewCount;
						}
						twoPeerReviewCount.push(changeCountHistory[dates[d]].twoPeerReviewCount);
						if(maxCount < changeCountHistory[dates[d]].twoPeerReviewCount){
							maxCount = changeCountHistory[dates[d]].twoPeerReviewCount;
						}
						collaborativeDevelopmentCount.push(changeCountHistory[dates[d]].collaborativeDevelopmentCount);		
						if(maxCount < changeCountHistory[dates[d]].collaborativeDevelopmentCount){
							maxCount = changeCountHistory[dates[d]].collaborativeDevelopmentCount;
						}				
					}

					var noFillColor = "#CC0000";
					var noStrokeColor = "#CC0000";
					var noPointColor = "#CC0000";
					var noPointStrokeColor = "#CC0000";
					var noPointHighlightFill = "#CC0000";
					var noPointHighlightStroke = "#CC0000";
					var oneFillColor = "#009933";
					var oneStrokeColor = "#009933";
					var onePointColor = "#009933";
					var onePointStrokeColor = "#009933";
					var onePointHighlightFill = "#009933";
					var onePointHighlightStroke = "#009933";
					
					$scope.lineChartOptionsUpwards = {
							scaleOverride: true,
							scaleStartValue: 0,
							scaleSteps: (maxCount/2)+1,
							scaleStepWidth: 2,
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
							datasets : [ {
								label : "One Peer Review",
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
							}]
						};
					
					gerritHistoryChartData.labels = days;
					gerritHistoryChartData.datasets[0].data = onePeerReviewCount;
					gerritHistoryChartData.datasets[1].data = noPeerReviewCount;
					$scope.gerritHistoryChartData = gerritHistoryChartData;
					
					if (response.data.error){
						$scope.gerritStatsStatus = { type: 'danger', msg: response.data.status, show: true};
						$timeout(function() {$scope.closeAlert()}, 15000);
					}else{
						$scope.gerritStatsStatus = { type: 'success', msg: response.data.status, show: true};
						$timeout(function() {$scope.closeAlert()}, 15000);
					}
					
					if (!response.data.error){
						$scope.gerritChartData = [ 
		                {
							value : response.data.noPeerReviewCount,
							color : "#CC0000",
							highlight : "#CC0000",
							label : "No Peer Reviews"
						}, {
							value : response.data.onePeerReviewCount,
							color : "#009933",
							highlight : "#009933",
							label : "One Peer Review"
						}, {
							value : response.data.twoPlusPeerReviewCount,
							color : "#0099FF",
							highlight : "#0099FF",
							label : "Two Peer Review"
						}, {
							value : response.data.collabrativeDevelopmentCount,
							color : "#6600FF",
							highlight : "#6600FF",
							label : "Collaborative Development"
						} ];
					}
				});
	}
}

function GerritStats($http, $scope, $rootScope, $timeout, $location, $log, $q, Gerrit) {
	$scope.metrics = [];
	$scope.gerritStatus = 'merged';
	if($scope.configLoaded === undefined){
		$scope.configLoaded = false;
	}
	
	$scope.GetAvatarClass = GetAvatarClass;
	
	$scope.gerritChartOptions = {

		      // Sets the chart to be responsive
		      responsive: true,

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
		      //  easeOutQuart, easeOutQuad, easeInOutBounce, easeOutSine, easeInOutCubic,
		      //  easeInExpo, easeInOutBack, easeInCirc, easeInOutElastic, easeOutBack,
		      //  easeInQuad, easeInOutExpo, easeInQuart, easeOutQuint, easeInOutCirc,
		      //  easeInSine, easeOutExpo, easeOutCirc, easeOutCubic, easeInQuint,
		      //  easeInElastic, easeInOutSine, easeInOutQuint, easeInBounce,
		      //  easeOutElastic, easeInCubic]
		      animationEasing : 'easeOutBounce',

		      // Boolean - Whether we animate the rotation of the Doughnut
		      animateRotate : true,

		      // Boolean - Whether we animate scaling the Doughnut from the
				// centre
		      animateScale : true,

		      // String - A legend template
		      legendTemplate : '<ul class="tc-chart-js-legend"><% for (var i=0; i<segments.length; i++){%><li><span style="background-color:<%=segments[i].fillColor%>"></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>'
		      
	};

	Gerrit.configInfo().then(function(response) {
		UpdateGerritConfig(response.data, $scope, $location);
	});
	
	$scope.closeAlert = function() {
		$scope.gerritStatsStatus.show = false;
	}
	
	$scope.$watch('gerritStatus', function(){
		GetGerritStats($http, $scope, $timeout);
		GetGerritAuthorAndReviewerStats($http, $scope, $timeout);
		}, true);	
	$scope.$watch('configLoaded', function(){
		GetGerritStats($http, $scope, $timeout);
		GetGerritAuthorAndReviewerStats($http, $scope, $timeout);
		}, true);
		
	$scope.manuallyRefreshGerritData = function(){
		$http.get('/gerrit/review/refreshCache').then(function(){GetGerritStats($http, $scope, $timeout);});		
		GetGerritAuthorAndReviewerStats($http, $scope, $timeout);
	}
	
	$scope.changeGerritStatus = function(gerritStatus){
		$scope.gerritStatus = gerritStatus;
	};
	
	$scope.getNoPeerReviewRowClass = function(percentage) {
		return GetDimReviewRowClassLimit(percentage, $scope.noPeerReviewsTarget);
	};

	$scope.getOnePeerReviewRowClass = function(percentage, percentage1, percentage2) {
		return GetDimReviewRowClassTarget(percentage+percentage1+percentage2, $scope.onePeerReviewTarget);
	};

	$scope.getTwoPeerReviewRowClass = function(percentage, percentage1) {
		return GetDimReviewRowClassTarget(percentage+percentage1, $scope.twoPeerReviewTarget);
	};

	$scope.getCollabrativeDevelopmentRowClass = function(percentage) {
		return GetDimReviewRowClassTarget(percentage,
				$scope.collaborativeReviewTarget);
	};	
	
	$rootScope.GetGerritStatsClass = function(){
		return GetGerritStatsClass($rootScope);
	};
	
};

appGerritStatsModule.controller('GerritStatsCtrl', [ '$http', '$scope', '$rootScope', '$timeout', '$location',
		'$q', '$log', 'Gerrit', GerritStats ]);
