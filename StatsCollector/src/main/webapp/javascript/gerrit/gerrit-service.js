angular.module('appGerritStats').service('Gerrit', ['$http',
	 function($http) {
		return ({
			configInfo: configInfo
		});
		
		$http.defaults.withCredentials = false;
				
		function configInfo(){
			return $http.get('/gerrit/config/info');
		}
	}
]);