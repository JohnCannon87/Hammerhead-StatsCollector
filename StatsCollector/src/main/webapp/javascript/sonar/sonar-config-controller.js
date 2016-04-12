function SonarConfigCtrl($http, $scope, $log, $q, Sonar, Upload, getOIMConfig) {	
	var vm = this;
	
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
    	saveSonarConfig();
    };
    
    function downloadConfig(){
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

