angular.module('appGerritStats').service('Gerrit', ['$http', 'gerritAppConfig',
	 function($http, gerritAppConfig) {
		return ({
			configInfo: configInfo,
			metrics: metrics
		});
		
		$http.defaults.withCredentials = false;
				
		function configInfo(){
			return $http.get('/gerrit/config/info');
		}
		
		function metrics(gerritStatus) {
			return $http.get('/gerrit/review/'+gerritStatus+'/all');
		}
	}
]);