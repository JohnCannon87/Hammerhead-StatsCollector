function ReloadSonarCache($scope, $http){
	$http.get('/sonar/stats/refreshCache').then(function(response){
		var alert = {};
		if(response.data){
			alert = {
					type: 'success',
					msg: 'Statistics Reloaded Successfully !'
			};
			$scope.alerts.push(alert);
		}else{
			alert = {
					type: 'danger',
					msg: 'Error Statistics Not Reloaded !'
			};
			$scope.alerts.push(alert);			
		}		
	});
}

function TestSonarConnection($scope, $http){
	$scope.sonarConnectionDetails = "Connecting ...";
	$scope.sonarConnectionResult = "";
	$http.get('/sonar/stats/testConnection').then(function(response){
		$scope.sonarConnectionDetails = response.data.connectionDetails;
		if(null === response.data.connectionResult){
			$scope.sonarConnectionResult = "Processing ...";
			$scope.sonarConnectionResult = response.data.errorResult;
		}else{
			$scope.sonarConnectionResult = "Processing ...";
			$scope.sonarConnectionResult = JSON.stringify(response.data.connectionResult);
		}				
	});
}

function SonarConfigCtrl($http, $scope, $log, $q, Sonar, Upload, getOIMConfig) {	
	var vm = this;
	
	$scope.alerts = [];
	
	$scope.CloseSonarAlert = function(index) {
		$scope.alerts.splice(index, 1);
	}
	
	vm.ReloadSonarCache = function(){
		return ReloadSonarCache($scope, $http);
	}
	
	$scope.TestSonarConnection = function(){
		return TestSonarConnection($scope, $http);
	}
	
	vm.downloadSonarConfig = downloadSonarConfig;
	
    vm.onSubmit = onSubmit;

    vm.model = {
    };
    
    vm.options = {
      formState: {
      }
    };
	
	$scope.$watch('files', function(files) {
		vm.formUpload = false;
		if (files != null) {
			for (var i = 0; i < files.length; i++) {
				vm.errorMsg = null;
				(function(file) {
					generateThumbAndUpload(file);
				})(files[i]);
			}
		}
	});
	
	function generateThumbAndUpload(file) {
		vm.errorMsg = null;
		uploadUsing$http(file);
	}
	
	function uploadUsing$http(file) {
		file.upload = Upload.upload({
			url: '/sonar/config/upload',
			method: 'POST',
			headers: {
				'Content-Type': file.type
			},
			fields: {username: vm.username},
			file: file,
			fileFormDataName: 'file'
		});
	
		file.upload.then(function(response) {
			file.result = response.data;
			UpdateSonarConfig(response.data, vm);
		}, function(response) {
			if (response.status > 0)
				vm.errorMsg = response.status + ': ' + response.data;
		});
	
		file.upload.progress(function(evt) {
			file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		});
	}
	
	$http.get('/sonar/config/info').then(function(response){
		vm.model = response.data;
		console.log(JSON.stringify(vm.fields));
		$http.get('/sonar/config/schema').then(function(response){
			vm.fields = getOIMConfig(vm.model, response.data.properties);
		});
	});
	    
 // function definition
    function onSubmit() {
    	saveSonarConfig();
    };
    
    function downloadSonarConfig(){
		$http.get('/sonar/config/info').then(function(response){
			//Download File Now...
			var file = new Blob([JSON.stringify(response.data)], {type: 'application/json'});
			saveAs(file, 'SonarConfig.json');
		});		
	};
	
	function saveSonarConfig(){
		var sonarConfig = vm.model;		
		$http.post('/sonar/config/changeConfig', sonarConfig)
		.success(function(data){
			console.log(data);
			UpdateSonarConfig(data, vm);
		}).error(function(data){
			console.log(data);
		});
	};

};

appSonarStatsModule.controller('SonarConfigCtrl', ['$http', '$scope', '$log', '$q', 'Sonar', 'Upload', 'getOIMConfig', SonarConfigCtrl]);

