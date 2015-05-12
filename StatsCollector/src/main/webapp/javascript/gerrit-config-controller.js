function UpdateGerritConfig(data, $scope){
	$scope.gerritHostname = data.host;
	$scope.gerritHostPort = data.hostPort;
	$scope.reviewersToIgnore = data.reviewersToIgnore;
	$scope.gerritUsername = data.username;
	$scope.gerritPassword = data.password;
	$scope.gerritTopicRegex = data.topicRegex;
	$scope.gerritThreadSplitSize = data.threadSplitSize;
	$scope.gerritStartDateOffset = data.startDateOffset;
	$scope.gerritEndDateOffset = data.endDateOffset;
	$scope.gerritProjectRegex = data.projectRegex;
	$scope.noPeerReviewsTarget = data.noPeerReviewTarget;
	$scope.onePeerReviewTarget = data.onePeerReviewTarget;
	$scope.twoPeerReviewTarget = data.twoPeerReviewTarget;
	$scope.collaborativeReviewTarget = data.collaborativeReviewTarget;
}

function GerritConfig($http, $scope, $log, $q, gerritAppConfig, Gerrit, Upload) {
		
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
			url: '/gerrit/config/upload',
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
			UpdateGerritConfig(response.data, $scope);
		}, function(response) {
			if (response.status > 0)
				$scope.errorMsg = response.status + ': ' + response.data;
		});
	
		file.upload.progress(function(evt) {
			file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		});
	}
	
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
		$http.post('/gerrit/config/changeInfo?host='+$scope.gerritHostname
				+'&hostPort='+$scope.gerritHostPort
				+'&username='+$scope.gerritUsername
				+'&password='+$scope.gerritPassword
				+'&topicRegex='+$scope.gerritTopicRegex
				+'&threadSplitSize='+$scope.gerritThreadSplitSize
				+'&startDateOffset='+$scope.gerritStartDateOffset
				+'&endDateOffset='+$scope.gerritEndDateOffset
				+'&projectRegex='+$scope.gerritProjectRegex
				)
		.success(function(data){
			console.log(data);
			UpdateGerritConfig(data, $scope);
		}).error(function(data){
			console.log(data);
		});
	};
	
	$scope.saveTargetConfig = function(){
		$http.post('/gerrit/config/saveTargets?noPeerReviewTarget='+$scope.noPeerReviewsTarget
				+'&onePeerReviewTarget='+$scope.onePeerReviewTarget
				+'&twoPeerReviewTarget='+$scope.twoPeerReviewTarget
				+'&collaborativeDevelopmentTarget='+$scope.collaborativeReviewTarget)
		.success(function(data){
			console.log(data);
			UpdateGerritConfig(data, $scope);
		}).error(function(data){
			console.log(data);
		});
	}
	
};

appGerritStatsModule.controller('GerritConfigCtrl', ['$http', '$scope', '$log', '$q', 'gerritAppConfig', 'Gerrit', 'Upload', GerritConfig]);