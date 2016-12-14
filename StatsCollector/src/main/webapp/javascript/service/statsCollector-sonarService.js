/* global angular */

(function(){

    'use strict';

    angular
        .module('statsCollectorApp')
        .service('SonarService', ['$http', SonarService]);

        function SonarService($http) {
            return {
            	getConfig: getConfig,
            	saveConfig: saveConfig,
            	testConnection: testConnection,
            	refreshStats: refreshStats,
            	getConfigSchema: getConfigSchema,
            	getStats: getStats
            };

            function getConfig(){
            	return $http.get('/sonar/config/info');
            }

            function saveConfig(config){
            	return $http.post('/sonar/config/changeConfig', config);
            }

            function getConfigSchema(){
            	return $http.post('/sonar/config/schema');
            }

            function testConnection(){
            	return $http.get('/sonar/stats/testConnection');
            }

            function refreshStats(){
            	return $http.get('/sonar/stats/refreshCache');
            }

            function getStats(statsRequest){
            	return $http.get('/sonar/stats/' + statsRequest.sonarProjectRegex + '/all');
            }
        }
})();