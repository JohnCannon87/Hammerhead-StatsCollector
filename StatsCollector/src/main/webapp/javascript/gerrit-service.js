angular.module('appGerritStats').service('Gerrit', ['$http', 'gerritAppConfig',
	 function($http, gerritAppConfig) {
		return ({
			configInfo: configInfo,
			metrics: metrics
		});
		
		$http.defaults.withCredentials = false;
		
		function configInfo(){
			return $http.get(gerritAppConfig.gerrit.baseUrl + '/gerrit/config/info');
		}
		
		function metrics(gerritStatus) {
			return $http.get(gerritAppConfig.gerrit.baseUrl + '/gerrit/review/'+gerritStatus+'/all');
		}
	}
]);