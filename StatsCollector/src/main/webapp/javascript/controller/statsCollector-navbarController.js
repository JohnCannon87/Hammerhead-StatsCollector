/* global angular */
/* global moment */

(function() {

	'use strict';

	angular.module('statsCollectorApp').controller('NavbarCtrl', NavbarCtrl);

	NavbarCtrl.$inject = ['SonarService', 'GerritService', 'ModalPopupService', '$scope', '$rootScope', 'getOIMConfig'];

	function NavbarCtrl(SonarService, GerritService, ModalPopupService, $scope, $rootScope, getOIMConfig) {

		var vm = this;
		vm.alerts = [];

		//Assign Functions
		vm.error = error;
		vm.closeAlert = closeAlert;
		vm.showGerritConfig = showGerritConfig;
		vm.showSonarConfig = showSonarConfig;

		//Do Things
		getGerritConfig();
		getSonarConfig();
		$scope.$on('error-occured', listenForError);
		$scope.$on('notify-occured', listenForNotify);

		//Declare Functions
		function error(result){
			$rootScope.$broadcast('error-occured', { error: result});
		}

		function notify(msg, timeout, type){
			$rootScope.$broadcast('notify-occured', { notify: { statusText: msg}, timeout: timeout, type: type});
		}

		function getGerritConfig(){
			GerritService.getConfig().then(function(response){
				vm.gerritConfig = response.data;
				vm.gerritUrl = vm.gerritConfig.host+":"+vm.gerritConfig.hostPort;
				GerritService.getConfigSchema().then(function(response){
					vm.gerritConfigSchema = getOIMConfig(vm.gerritConfig ,response.data.properties);
				}, error);
			}, error);
		}

		function getSonarConfig(){
			SonarService.getConfig().then(function(response){
				vm.sonarConfig = response.data;
				vm.sonarUrl = vm.sonarConfig.host+":"+vm.sonarConfig.hostPort;
				SonarService.getConfigSchema().then(function(response){
					vm.sonarConfigSchema = getOIMConfig(vm.sonarConfig ,response.data.properties);
				}, error);
			}, error);
		}

		function listenForError(event, args){
			var msg = 'Sorry, Something went wrong !';
			if(args.error.status !== undefined && args.error.statusText !== undefined){
				msg = moment().format('YYYY-MM-DD, HH:mm:ss')+' - Error Code: ' + args.error.status + ', Error Message: ' + args.error.statusText;
			}else if(args.error.status !== undefined){
				msg = moment().format('YYYY-MM-DD, HH:mm:ss')+' - Error Code: ' + args.error.status + ', Error Message: ' + 'Sorry, Something went wrong !';
			}else if(args.error.statusText !== undefined){
				msg = moment().format('YYYY-MM-DD, HH:mm:ss')+' - Error Message: ' + args.error.statusText;
			}
			vm.alerts.push({
				msg: msg,
				type: 'danger',
				timeout: 5000
			});
		}

		function listenForNotify(event, args){
			var msg = moment().format('YYYY-MM-DD, HH:mm:ss')+' - ' + args.notify.statusText;
			vm.alerts.push({
				msg: msg,
				type: args.type,
				timeout: args.timeout
			});
		}

		function error(result){
			$rootScope.$broadcast('error-occured', { error: result});
		}

		function closeAlert(alert) {
			vm.alerts.splice(vm.alerts.indexOf(alert), 1);
		}

		function refreshGerritStats(modalOptions){
			GerritService.refreshStats().then(function(response){
				if(response.data){
					notify('Gerrit Stats Refreshed', 5000, 'success');
				}else{
					notify('Gerrit Stats Refresh Failed', 5000, 'danger');
				}
			});
		}

		function testGerritConfig(modalOptions){
			modalOptions.connectionResult = "Connecting ...";
			GerritService.testConnection().then(function(response){
				if(null === response.data.connectionResult){
					modalOptions.connectionResult = "Processing ...";
					modalOptions.connectionResult = response.data.errorResult;
				}else{
					modalOptions.connectionResult = "Processing ...";
					modalOptions.connectionResult = JSON.stringify(response.data.connectionResult);
				}
			});
		}

		function refreshSonarStats(modalOptions){
			SonarService.refreshStats().then(function(response){
				if(response.data){
					notify('Sonar Stats Refreshed', 5000, 'success');
				}else{
					notify('Sonar Stats Refresh Failed', 5000, 'danger');
				}
			});
		}

		function testSonarConfig(modalOptions){
			modalOptions.connectionResult = "Connecting ...";
			SonarService.testConnection().then(function(response){
				if(null === response.data.connectionResult){
					modalOptions.connectionResult = "Processing ...";
					modalOptions.connectionResult = response.data.errorResult;
				}else{
					modalOptions.connectionResult = "Processing ...";
					modalOptions.connectionResult = JSON.stringify(response.data.connectionResult);
				}
			});
		}

		function showGerritConfig(){
			vm.gerritConfig.actualPassword='';
			var modalDefaults = {
					backdrop : true,
					keyboard : true,
					modalFade : true,
					size: 'lg',
					templateUrl : '/fragments/ConfigModal.html'
				};
			var modalOptions = {
				closeButtonText : 'Close',
				headerText : 'Gerrit Config',
				config : vm.gerritConfig,
				schema : vm.gerritConfigSchema,
				saveConfig : GerritService.saveConfig,
				testConnection : testGerritConfig,
				refreshMetrics : refreshGerritStats
			};

			ModalPopupService.showModal(modalDefaults, modalOptions);
		}

		function showSonarConfig(){
			vm.sonarConfig.actualPassword='';
			var modalDefaults = {
					backdrop : true,
					keyboard : true,
					modalFade : true,
					size: 'lg',
					templateUrl : '/fragments/ConfigModal.html'
				};
			var modalOptions = {
				closeButtonText : 'Close',
				headerText : 'Sonar Config',
				config : vm.sonarConfig,
				schema : vm.sonarConfigSchema,
				saveConfig : SonarService.saveConfig,
				testConnection : testSonarConfig,
				refreshMetrics : refreshSonarStats
			};

			ModalPopupService.showModal(modalDefaults, modalOptions);
		}
	}
})();
