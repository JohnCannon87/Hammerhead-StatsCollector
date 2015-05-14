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

function SonarConfig($http, $scope, $log, $q, Sonar, Upload) {
		
	$scope.$watch('files', function(files) {
		$scope.formUpload = false;
		if (files != null) {
			for (var i = 0; i < files.length; i++) {
				$scope.errorMsg = null;
				(function(file) {
					generateThumbAndUpload(file);
				})(files[i]);
			}
		}
	});
	
	function generateThumbAndUpload(file) {
		$scope.errorMsg = null;
		uploadUsing$http(file);
	}
	
	function uploadUsing$http(file) {
		file.upload = Upload.upload({
			url: '/sonar/config/upload',
			method: 'POST',
			headers: {
				'Content-Type': file.type
			},
			fields: {username: $scope.username},
			file: file,
			fileFormDataName: 'file'
		});
	
		file.upload.then(function(response) {
			file.result = response.data;
			UpdateSonarConfig(response.data, $scope);
		}, function(response) {
			if (response.status > 0)
				$scope.errorMsg = response.status + ': ' + response.data;
		});
	
		file.upload.progress(function(evt) {
			file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		});
	}
	
	Sonar.configInfo().then(function(response){
		UpdateSonarConfig(response.data, $scope);
	});
	
	$scope.downloadConfig = function(){
		$http.get('/sonar/config/info').then(function(response){
			//Download File Now...
			var file = new Blob([JSON.stringify(response.data)], {type: 'application/json'});
			saveAs(file, 'SonarConfig.json');
		});		
	};
		
	//Form Data Setup
	$scope.saveSonarConfig = function(){
		var sonarConfig =
						{
							"username": $scope.sonarUsername,
							"password": $scope.sonarPassword,
							"host": $scope.sonarHostname,
							"hostPort": $scope.sonarHostPort,
							"projectRegex": $scope.sonarProjectRegex,
							"methodComplexityTarget": $scope.methodComplexityTarget,
							"fileComplexityTarget": $scope.fileComplexityTarget,
							"testCoverageTarget": $scope.testCoverageTarget,
							"rulesComplianceTarget": $scope.rulesComplianceTarget								
						}
		
		$http.post('/sonar/config/changeConfig', sonarConfig)
		.success(function(data){
			console.log(data);
			UpdateSonarConfig(data, $scope);
		}).error(function(data){
			console.log(data);
		});
	};
	
	$scope.saveTargetConfig = function(){
		$http.post('/sonar/config/saveTargets?noPeerReviewTarget='+$scope.noPeerReviewsTarget
				+'&onePeerReviewTarget='+$scope.onePeerReviewTarget
				+'&twoPeerReviewTarget='+$scope.twoPeerReviewTarget
				+'&collaborativeDevelopmentTarget='+$scope.collaborativeReviewTarget)
		.success(function(data){
			console.log(data);
			UpdateSonarConfig(data, $scope);
		}).error(function(data){
			console.log(data);
		});
	}
	
};

appSonarStatsModule.controller('SonarConfigCtrl', ['$http', '$scope', '$log', '$q', 'Sonar', 'Upload', SonarConfig]);