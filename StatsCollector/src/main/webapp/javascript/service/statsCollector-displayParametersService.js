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

            function getParams(){
            	return $http.get('/parameters/list');
            }

            function saveParams(parameters){
            	return $http.post('/parameters/save', parameters);
            }
        }
})();