function UpdateGerritConfig(data, $scope) {
	$scope.gerritHostname = data.host;
	$scope.reviewersToIgnore = data.reviewersToIgnore;
	$scope.gerritUsername = data.username;
	$scope.gerritPassword = data.password;
	$scope.noPeerReviewsTarget = data.noPeerReviewTarget;
	$scope.onePeerReviewTarget = data.onePeerReviewTarget;
	$scope.twoPeerReviewTarget = data.twoPeerReviewTarget;
	$scope.collaborativeReviewTarget = data.collaborativeReviewTarget;
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

function GerritStats($http, $scope, $log, $q, gerritAppConfig, Gerrit) {
	$scope.metrics = new Array();
	$scope.goals = gerritAppConfig.gerrit.goals;

	$scope.gerritChartOptions = {


		      // Sets the chart to be responsive
		      responsive: false,

		      //Boolean - Whether we should show a stroke on each segment
		      segmentShowStroke : true,

		      //String - The colour of each segment stroke
		      segmentStrokeColor : '#fff',

		      //Number - The width of each segment stroke
		      segmentStrokeWidth : 2,

		      //Number - The percentage of the chart that we cut out of the middle
		      percentageInnerCutout : 0, // This is 0 for Pie charts

		      //Number - Amount of animation steps
		      animationSteps : 100,

		      //String - Animation easing effect
		      animationEasing : 'easeOutBounce',

		      //Boolean - Whether we animate the rotation of the Doughnut
		      animateRotate : true,

		      //Boolean - Whether we animate scaling the Doughnut from the centre
		      animateScale : false,

		      //String - A legend template
		      legendTemplate : '<ul class="tc-chart-js-legend"><% for (var i=0; i<segments.length; i++){%><li><span style="background-color:<%=segments[i].fillColor%>"></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>'

	};

	Gerrit.configInfo().then(function(response) {
		UpdateGerritConfig(response.data, $scope);
	});

	Gerrit
			.metrics()
			.then(
					function(response) {
						$scope.noPeerReviewers = response.data.noPeerReviewCount;
						$scope.onePeerReviewer = response.data.onePeerReviewCount;
						$scope.twoPeerReviewers = response.data.twoPlusPeerReviewCount;
						$scope.collabrativeDevelopment = response.data.collabrativeDevelopmentCount;
						$scope.totalReviews = response.data.totalReviewsCount;
						$scope.noPeerPercentage = response.data.noPeerReviewPercentage;
						$scope.onePeerPercentage = response.data.onePeerReviewPercentage;
						$scope.twoPeerPercentage = response.data.twoPeerReviewPercentage;
						$scope.collaborativePercentage = response.data.collaborativeReviewPercentage;

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
					});

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

appGerritStatsModule.controller('GerritStatsCtrl', [ '$http', '$scope', '$log',
		'$q', 'gerritAppConfig', 'Gerrit', GerritStats ]);
