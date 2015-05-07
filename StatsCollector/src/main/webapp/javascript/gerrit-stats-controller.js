function UpdateGerritConfig(data, $scope){
	$scope.gerritHostName = data.host;
	$scope.reviewersToIgnore = data.reviewersToIgnore;
}

function GerritStats($http, $scope, $log, $q, gerritAppConfig, Gerrit) {
	$scope.metrics = new Array();
	$scope.goals = gerritAppConfig.gerrit.goals;	
		
	Gerrit.hostname().then(function(response){
		UpdateGerritConfig(response.data, $scope);
	});
	
	Gerrit.metrics().then(function(response){
		$scope.noPeerReviewers = response.data.noPeerReviewCount;
		$scope.onePeerReviewer = response.data.onePeerReviewCount;
		$scope.twoPeerReviewers = response.data.twoPlusPeerReviewCount;
		$scope.collabrativeDevelopment = response.data.collabrativeDevelopmentCount;
		$scope.totalReviews = response.data.totalReviewsCount;
	});
	
	$scope.addReviewer = function(){
		$http.post('/gerrit/config/addReviewerToIgnore?reviewer='+$scope.gerritReviewer)
		.success(function(data){
			console.log(data);			
			UpdateGerritConfig(data, $scope);
		}).error(function(data){
			console.log(data);
		});
	}
	
	$scope.removeReviewer = function(reviewer){
		$http.post('/gerrit/config/removeReviewerToIgnore?reviewer='+reviewer)
		.success(function(data){
			console.log(data);			
			UpdateGerritConfig(data, $scope);
		}).error(function(data){
			console.log(data);
		});
	};
	
	//Form Data Setup
	$scope.changeHostName = function(){
		$http.post('/gerrit/config/changeHost?host='+$scope.gerritHostName)
		.success(function(data){
			console.log(data);
			UpdateGerritConfig(data, $scope);
		}).error(function(data){
			console.log(data);
		});
	};
	
};

angular.module('appGerritStats').controller('GerritCtrl', ['$http', '$scope', '$log', '$q', 'gerritAppConfig', 'Gerrit', GerritStats]);
