function UpdateSonarConfig(data, $scope){
	$scope.sonarHostname = data.host;
	$scope.sonarHostPort = data.hostPort;
	$scope.sonarUsername = data.username;
	$scope.sonarPassword = data.password;
	$scope.sonarProjectRegex = data.projectRegex;
	$scope.methodComplexityTarget = data.methodComplexityTarget;
	$scope.fileComplexityTarget = data.fileComplexityTarget;
	$scope.testCoverageTarget = data.testCoverageTarget;
	$scope.rulesComplianceTarget = data.rulesComplianceTarget;
}

function UpdateSonarStats(data, $scope){
	$scope.methodComplexity = data.methodComplexity;
	$scope.fileComplexity = data.fileComplexity;
	$scope.testCoverage = data.testCoverage;
	$scope.rulesCompliance = data.rulesCompliance;
	$scope.linesOfCode = data.linesOfCode;
}

function SonarStats($http, $scope, $timeout, $log, $q, Sonar) {
	
	Sonar.configInfo().then(function(response){
		UpdateSonarConfig(response.data, $scope);
	});
	
	$http.get('/sonar/stats/allStatistics').then(function(response){UpdateSonarStats(response.data, $scope)});
}

appGerritStatsModule.controller('SonarStatsCtrl', [ '$http', '$scope', '$timeout', '$log',
                                             		'$q', 'Sonar', SonarStats ]);