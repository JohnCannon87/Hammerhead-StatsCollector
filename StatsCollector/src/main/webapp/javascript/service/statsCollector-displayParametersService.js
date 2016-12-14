/* global angular */

(function(){

    'use strict';

    angular
        .module('statsCollectorApp')
        .service('DisplayParametersService', ['$http', DisplayParametersService]);

        function DisplayParametersService($http) {
            return {
            	getParams: getParams,
            	saveParams: saveParams
            };

            function getParams(projectName){
            	return $http.get('/parameters/'+projectName);
            }

            function saveParams(parameters){
            	return $http.post('/parameters/save', parameters);
            }
        }
})();