angular.module('appSonarStats').service('Sonar', ['$http',
	 function($http) {
		return ({
			configInfo: configInfo
		});
		
		$http.defaults.withCredentials = false;
				
		function configInfo(){
			return $http.get('/sonar/config/info');
		}
	}
]);