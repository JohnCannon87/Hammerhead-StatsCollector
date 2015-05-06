angular.module('appGerritStats').service('Gerrit', ['$http', 'gerritAppConfig',
	 function($http, gerritAppConfig) {
		return ({
			hostname: hostname,
			metrics: metrics
		});
		
		$http.defaults.withCredentials = false;
		
		function hostname(key, date){
			return $http.get(gerritAppConfig.gerrit.baseUrl + '/gerrit/config/info');
		}
		
		function metrics(key, date) {
			return $http.get(gerritAppConfig.gerrit.baseUrl + '/gerrit/review/open/all');
		}
	}
]);