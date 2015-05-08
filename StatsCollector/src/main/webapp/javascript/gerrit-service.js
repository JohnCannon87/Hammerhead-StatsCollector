angular.module('appGerritStats').service('Gerrit', ['$http', 'gerritAppConfig',
	 function($http, gerritAppConfig) {
		return ({
			configInfo: configInfo,
			metrics: metrics
		});
		
		$http.defaults.withCredentials = false;
		
		function configInfo(key, date){
			return $http.get(gerritAppConfig.gerrit.baseUrl + '/gerrit/config/info');
		}
		
		function metrics(key, date) {
			return $http.get(gerritAppConfig.gerrit.baseUrl + '/gerrit/review/open/all');
		}
	}
]);