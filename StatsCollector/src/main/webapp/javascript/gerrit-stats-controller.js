angular.module('appGerritStats').controller('GerritCtrl', ['$scope', '$log', '$q', 'gerritAppConfig', 'Gerrit',
    function($scope, $log, $q, gerritAppConfig, Gerrit) {
	$scope.metrics = new Array();
	$scope.goals = gerritAppConfig.gerrit.goals;	
	
	Gerrit.hostname().then(function(response){
		$scope.gerritHostName = response.data.host;
	});
	
	Gerrit.metrics().then(function(response){
		$scope.noPeerReviewers = response.data.noPeerReviewCount;
		$scope.onePeerReviewer = response.data.onePeerReviewCount;
		$scope.twoPeerReviewers = response.data.twoPlusPeerReviewCount;
		$scope.collabrativeDevelopment = response.data.collabrativeDevelopmentCount;
		$scope.totalReviews = response.data.totalReviewsCount;
	});
}]);