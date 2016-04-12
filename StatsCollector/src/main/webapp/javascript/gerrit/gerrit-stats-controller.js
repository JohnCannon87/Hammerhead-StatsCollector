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
	if($scope.configLoaded == true){
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
					$scope.onePeerReviewer = response.data.onePeerReviewCount;
					$scope.twoPeerReviewers = response.data.twoPlusPeerReviewCount;
					$scope.collabrativeDevelopments = response.data.collabrativeDevelopmentCount;
					$scope.totalReviews = response.data.totalReviewsCount;
					$scope.noPeerPercentage = response.data.noPeerReviewPercentage;
					$scope.onePeerPercentage = response.data.onePeerReviewPercentage;
					$scope.twoPeerPercentage = response.data.twoPeerReviewPercentage;
					$scope.collaborativePercentage = response.data.collaborativeReviewPercentage;
					$scope.noPeerReviews = response.data.noPeerReviewList;
					$scope.onePeerReviews = response.data.onePeerReviewList;
					$scope.twoPeerReviews = response.data.twoPlusPeerReviewList;
					$scope.collabrativeDevelopment = response.data.collabrativeDevelopmentList;
					
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

function GerritStats($http, $scope, $timeout, $location, $log, $q, Gerrit) {
	$scope.metrics = new Array();
	$scope.gerritStatus = 'merged';
	if($scope.configLoaded === undefined){
		$scope.configLoaded = false;
	}
	
	$scope.gerritChartOptions = {

		      // Sets the chart to be responsive
		      responsive: true,

		      // Boolean - Whether we should show a stroke on each segment
		      segmentShowStroke : true,

		      // String - The colour of each segment stroke
		      segmentStrokeColor : '#fff',

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
		return GetReviewRowClassLimit(percentage, $scope.noPeerReviewsTarget);
	};

	$scope.getOnePeerReviewRowClass = function(percentage) {
		return GetReviewRowClassTarget(percentage, $scope.onePeerReviewTarget);
	};

	$scope.getTwoPeerReviewRowClass = function(percentage) {
		return GetReviewRowClassTarget(percentage, $scope.twoPeerReviewTarget);
	};

	$scope.getCollabrativeDevelopmentRowClass = function(percentage) {
		return GetReviewRowClassTarget(percentage,
				$scope.collaborativeReviewTarget);
	};	
	
};

appGerritStatsModule.controller('GerritStatsCtrl', [ '$http', '$scope', '$timeout', '$location',
		'$q', '$log', 'Gerrit', GerritStats ]);
