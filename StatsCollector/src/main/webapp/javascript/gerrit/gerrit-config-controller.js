function UpdateGerritConfig(data, vm){
	vm.model = data;
}

function GerritConfigCtrl($http, $scope, $log, $q, Gerrit, Upload, getOIMConfig) {
	var vm = this;
		
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
			UpdateGerritConfig(response.data, vm);
		}, function(response) {
			if (response.status > 0)
				$scope.errorMsg = response.status + ': ' + response.data;
		});
	
		file.upload.progress(function(evt) {
			file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		});
	}
	
	Gerrit.configInfo().then(function(response){
		UpdateGerritConfig(response.data, vm);
	});
	
	function downloadConfig(){
		$http.get('/sonar/config/info').then(function(response){
			//Download File Now...
			var file = new Blob([JSON.stringify(response.data)], {type: 'application/json'});
			saveAs(file, 'SonarConfig.json');
		});		
	};
	
	$scope.addReviewer = function(){
		$http.post('/gerrit/config/addReviewerToIgnore?reviewer='+$scope.gerritReviewer)
		.success(function(data){
			console.log(data);			
			UpdateGerritConfig(data, vm);
		}).error(function(data){
			console.log(data);
		});
	};
		
	$scope.removeReviewer = function(reviewer){
		$http.post('/gerrit/config/removeReviewerToIgnore?reviewer='+reviewer)
		.success(function(data){
			console.log(data);			
			UpdateGerritConfig(data, vm);
		}).error(function(data){
			console.log(data);
		});
	};
	
	function saveConfig(){
		saveGerritConfig();
	};
	
	function saveGerritConfig(){
		var gerritConfig = vm.model;		
		$http.post('/gerrit/config/changeConfig', gerritConfig)
		.success(function(data){
			console.log(data);
			UpdateGerritConfig(data, vm);
		}).error(function(data){
			console.log(data);
		});
	};
	
	$http.get('/gerrit/config/info').then(function(response){
		vm.model = response.data;
		$http.get('/gerrit/config/schema').then(function(response){
			vm.fields = getOIMConfig(vm.model, response.data.properties);
		});
	});
	
	vm.downloadConfig = downloadConfig;
	
    vm.onSubmit = onSubmit;

    vm.model = {
      awesome: true
    };
    
    vm.options = {
      formState: {
        awesomeIsForced: false
      }
    };
        
 // function definition
    function onSubmit() {
    	saveGerritConfig();
    	saveTargetConfig();
    };
	
};

appGerritStatsModule.controller('GerritConfigCtrl', ['$http', '$scope', '$log', '$q', 'Gerrit', 'Upload', 'getOIMConfig', GerritConfigCtrl]);