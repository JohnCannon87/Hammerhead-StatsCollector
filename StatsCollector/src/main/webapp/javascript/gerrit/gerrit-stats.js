var appGerritStatsModule = angular.module('appGerritStats', ['tc.chartjs', 'ui.bootstrap', 'ngFileUpload', 'ngRoute'])
.config(function ($routeProvider, $locationProvider){
	//routing DOESN'T work without html5Mode
	$locationProvider.html5Mode({
		  enabled: true,
		  requireBase: false
		});
});