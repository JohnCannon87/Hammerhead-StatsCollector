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

function GerritConfig($http, $scope, $log, $q, gerritAppConfig, Gerrit) {
		
	  $scope.toggleDropdown = function($event) {
	    $event.preventDefault();
	    $event.stopPropagation();
	    $scope.status.isopen = !$scope.status.isopen;
	  };
	
	Gerrit.configInfo().then(function(response){
		UpdateGerritConfig(response.data, $scope);
	});
	
	$scope.downloadConfig = function(){
		$http.get(gerritAppConfig.gerrit.baseUrl + '/gerrit/config/info').then(function(response){
			//Download File Now...
			var file = new Blob([JSON.stringify(response.data)], {type: 'application/json'});
			saveAs(file, 'GerritConfig.json');
		});
		
	};
	
	$scope.addReviewer = function(){
		$http.post('/gerrit/config/addReviewerToIgnore?reviewer='+$scope.gerritReviewer)
		.success(function(data){
			console.log(data);			
			UpdateGerritConfig(data, $scope);
		}).error(function(data){
			console.log(data);
		});
	};
		
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
	$scope.saveGerritConfig = function(){
		$http.post('/gerrit/config/changeInfo?host='+$scope.gerritHostname+'&username='+$scope.gerritUsername+'&password='+$scope.gerritPassword)
		.success(function(data){
			console.log(data);
			UpdateGerritConfig(data, $scope);
		}).error(function(data){
			console.log(data);
		});
	};
	
	$scope.saveTargetConfig = function(){
		$http.post('/gerrit/config/saveTargets?noPeerReviewTarget='+$scope.noPeerReviewsTarget+'&onePeerReviewTarget='+$scope.onePeerReviewTarget+'&twoPeerReviewTarget='+$scope.twoPeerReviewTarget+'&collaborativeDevelopmentTarget='+$scope.collaborativeReviewTarget)
		.success(function(data){
			console.log(data);
			UpdateGerritConfig(data, $scope);
		}).error(function(data){
			console.log(data);
		});
	}
	
};

appGerritStatsModule.controller('GerritConfigCtrl', ['$http', '$scope', '$log', '$q', 'gerritAppConfig', 'Gerrit', GerritConfig]);