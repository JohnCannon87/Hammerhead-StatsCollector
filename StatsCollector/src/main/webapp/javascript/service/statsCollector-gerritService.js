/* global angular */

(function(){

    'use strict';

    angular
        .module('statsCollectorApp')
        .service('GerritService', ['$http', GerritService]);

        function GerritService($http) {
            return {
            	getConfig: getConfig,
            	saveConfig: saveConfig,
            	testConnection: testConnection,
            	refreshStats: refreshStats,
            	getConfigSchema: getConfigSchema,
            	getStats: getStats,
            	getReviewStats: getReviewStats
            };

            function getConfig(){
            	return $http.get('/gerrit/config/info');
            }

            function saveConfig(config){
            	return $http.post('/gerrit/config/changeConfig', config);
            }

            function getConfigSchema(){
            	return $http.post('/gerrit/config/schema');
            }

            function testConnection(){
            	return $http.get('/gerrit/review/testConnection');
            }

            function refreshStats(){
            	return $http.get('/gerrit/review/refreshCache');
            }

            function getStats(statsRequest){
            	var url = '/gerrit/review/'
            	if(statsRequest.gerritProjectRegex !== undefined
        				&& statsRequest.gerritStartDateOffset !== undefined
        				&& statsRequest.gerritEndDateOffset !== undefined
        				&& statsRequest.gerritProjectFilterOutRegex !== undefined){
        			url = url + statsRequest.gerritStatus + '/'
        			+ statsRequest.gerritProjectRegex + '/'
        			+ statsRequest.gerritProjectFilterOutRegex + '/'
        			+ statsRequest.gerritStartDateOffset + '/'
        			+ statsRequest.gerritEndDateOffset;
        		} else if (statsRequest.gerritProjectRegex !== undefined
        				&& statsRequest.gerritStartDateOffset !== undefined
        				&& statsRequest.gerritEndDateOffset !== undefined) {
        			url = url + statsRequest.gerritStatus + '/'
        					+ statsRequest.gerritProjectRegex + '/'
        					+ statsRequest.gerritStartDateOffset + '/'
        					+ statsRequest.gerritEndDateOffset;
        		} else if (statsRequest.gerritProjectRegex === undefined
        				&& statsRequest.gerritStartDateOffset !== undefined
        				&& statsRequest.gerritEndDateOffset !== undefined) {
        			url = url + statsRequest.gerritStatus + '/'
        					+ statsRequest.gerritStartDateOffset + '/'
        					+ statsRequest.gerritEndDateOffset;
        		} else if (statsRequest.gerritProjectRegex !== undefined
        				&& (statsRequest.gerritStartDateOffset !== undefined || statsRequest.gerritEndDateOffset !== undefined)) {
        			url = url + statsRequest.gerritStatus + '/'
        					+ statsRequest.gerritProjectRegex;
        		} else {
        			url = url + statsRequest.gerritStatus + '/all';
        		}
            	return $http.get(url);
            }

            function getReviewStats(statsRequest){
            	var url = '/gerrit/review/authors/'
            	if(statsRequest.gerritProjectRegex !== undefined
        				&& statsRequest.gerritStartDateOffset !== undefined
        				&& statsRequest.gerritEndDateOffset !== undefined
        				&& statsRequest.gerritProjectFilterOutRegex !== undefined){
        			url = url + statsRequest.gerritStatus + '/'
        			+ statsRequest.gerritProjectRegex + '/'
        			+ statsRequest.gerritProjectFilterOutRegex + '/'
        			+ statsRequest.gerritStartDateOffset + '/'
        			+ statsRequest.gerritEndDateOffset;
        		} else if (statsRequest.gerritProjectRegex !== undefined
        				&& statsRequest.gerritStartDateOffset !== undefined
        				&& statsRequest.gerritEndDateOffset !== undefined) {
        			url = url + statsRequest.gerritStatus + '/'
        					+ statsRequest.gerritProjectRegex + '/'
        					+ statsRequest.gerritStartDateOffset + '/'
        					+ statsRequest.gerritEndDateOffset;
        		} else if (statsRequest.gerritProjectRegex === undefined
        				&& statsRequest.gerritStartDateOffset !== undefined
        				&& statsRequest.gerritEndDateOffset !== undefined) {
        			url = url + statsRequest.gerritStatus + '/'
        					+ statsRequest.gerritStartDateOffset + '/'
        					+ statsRequest.gerritEndDateOffset;
        		} else if (statsRequest.gerritProjectRegex !== undefined
        				&& (statsRequest.gerritStartDateOffset !== undefined || statsRequest.gerritEndDateOffset !== undefined)) {
        			url = url + statsRequest.gerritStatus + '/'
        					+ $scope.gerritProjectRegex;
        		}
            	return $http.get(url);
            }
        }
})();