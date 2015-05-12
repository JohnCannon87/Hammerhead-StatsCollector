angular.module('appGerritStats').service('Gerrit', ['$http', 'gerritAppConfig',
	 function($http, gerritAppConfig) {
		return ({
			configInfo: configInfo
		});
		
		$http.defaults.withCredentials = false;
				
		function configInfo(){
			return $http.get('/gerrit/config/info');
		}
	}
]);