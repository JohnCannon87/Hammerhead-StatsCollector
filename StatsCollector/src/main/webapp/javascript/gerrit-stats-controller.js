function UpdateGerritConfig(data, $scope){
	$scope.gerritHostname = data.host;
	$scope.reviewersToIgnore = data.reviewersToIgnore;
	$scope.gerritUsername = data.username;
	$scope.gerritPassword = data.password;
	$scope.noPeerReviewsTarget = data.noPeerReviewTarget;
	$scope.onePeerReviewTarget = data.onePeerReviewTarget;
	$scope.twoPeerReviewTarget = data.twoPeerReviewTarget;
	$scope.collaborativeReviewTarget = data.collaborativeReviewTarget;
}

function GetReviewRowClassTarget(value, target){
	if(value === undefined){
		return "list-group-item list-group-item-info";	
	}else if(value > target){
		return "list-group-item list-group-item-success";
	}else if(value == target){
		return "list-group-item list-group-item-warning";
	}else{
		return "list-group-item list-group-item-danger";
	}
}

function GetReviewRowClassLimit(value, target){
	if(value === undefined){
		return "list-group-item list-group-item-info";	
	}else if(value > target){
		return "list-group-item list-group-item-danger";
	}else if(value == target){
		return "list-group-item list-group-item-warning";
	}else{
		return "list-group-item list-group-item-success";
	}
}

function GerritStats($http, $scope, $log, $q, gerritAppConfig, Gerrit) {
	$scope.metrics = new Array();
	$scope.goals = gerritAppConfig.gerrit.goals;	
			
	Gerrit.configInfo().then(function(response){
		UpdateGerritConfig(response.data, $scope);
	});
	
	Gerrit.metrics().then(function(response){
		$scope.noPeerReviewers = response.data.noPeerReviewCount;
		$scope.onePeerReviewer = response.data.onePeerReviewCount;
		$scope.twoPeerReviewers = response.data.twoPlusPeerReviewCount;
		$scope.collabrativeDevelopment = response.data.collabrativeDevelopmentCount;
		$scope.totalReviews = response.data.totalReviewsCount;
		$scope.noPeerPercentage = response.data.noPeerReviewPercentage;
		$scope.onePeerPercentage = response.data.onePeerReviewPercentage;
		$scope.twoPeerPercentage = response.data.twoPeerReviewPercentage;
		$scope.collaborativePercentage = response.data.collaborativeReviewPercentage;
		
		var data = [
		            {
		                value: response.data.noPeerReviewCount,
		                color:"#F7464A",
		                highlight: "#FF5A5E",
		                label: "No Peer Reviews"
		            },
		            {
		                value: response.data.onePeerReviewCount,
		                color: "#46BFBD",
		                highlight: "#5AD3D1",
		                label: "One Peer Review"
		            },
		            {
		                value: response.data.twoPlusPeerReviewCount,
		                color: "#0066FF",
		                highlight: "#0066FF",
		                label: "Two Peer Review"
		            },
		            {
		                value: response.data.collabrativeDevelopmentCount,
		                color: "#CC0099",
		                highlight: "#CC0099",
		                label: "Collaborative Development"
		            }
		            ];
		var ctx = document.getElementById("gerritPieChart").getContext("2d");
		var gerritPieChart = new Chart(ctx[0]).Pie(data, options);
	});		
	
	$scope.getNoPeerReviewRowClass = function(percentage){
		return GetReviewRowClassLimit(percentage, $scope.noPeerReviewsTarget);
	};
	
	$scope.getOnePeerReviewRowClass = function(percentage){
		return GetReviewRowClassTarget(percentage, $scope.onePeerReviewTarget);
	};

	$scope.getTwoPeerReviewRowClass = function(percentage){
		return GetReviewRowClassTarget(percentage, $scope.twoPeerReviewTarget);
	};

	$scope.getCollabrativeDevelopmentRowClass = function(percentage){
		return GetReviewRowClassTarget(percentage, $scope.collaborativeReviewTarget);
	};
	
};

angular.module('appGerritStats').controller('GerritStatsCtrl', ['$http', '$scope', '$log', '$q', 'gerritAppConfig', 'Gerrit', GerritStats]);
