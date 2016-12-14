/* global angular */
/* Based (i.e. ripped off) from tutorial on http://weblogs.asp.net/dwahlin/building-an-angularjs-modal-service*/

(function() {

	'use strict';

	angular.module('statsCollectorApp').service('ModalPopupService',
			[ '$http', '$uibModal', ModalPopupService ]);

	function ModalPopupService($http, $uibModal) {
		return {
			showModal : showModal
		};

		function showModalPopup(customModalDefaults, customModalOptions) {
			var modalDefaults = {
					backdrop : true,
					keyboard : true,
					modalFade : true,
					templateUrl : 'fragments/modals/Modal.html'
				};

			var modalOptions = {
					closeButtonText : 'Close',
					actionButtonText : 'OK',
					headerText : 'Proceed?',
					bodyText : 'Perform this action?',
					okDisabled : true
				};

			// Create temp objects to work with since we're in a singleton
			// service
			var tempModalDefaults = {};
			var tempModalOptions = {};

			// Map angular-ui modal custom defaults to modal defaults defined in
			// service
			angular.extend(tempModalDefaults, modalDefaults,
					customModalDefaults);

			// Map modal.html $scope custom properties to defaults defined in
			// service
			angular.extend(tempModalOptions, modalOptions, customModalOptions);

			if (!tempModalDefaults.controller) {
				tempModalDefaults.controller = function($scope, $uibModalInstance) {
					$scope.modalOptions = tempModalOptions;
					$scope.modalOptions.ok = function() {
						$uibModalInstance.close(true);
					};
					$scope.modalOptions.close = function() {
						$uibModalInstance.close(false);
					};
				};
			}

			return $uibModal.open(tempModalDefaults).result;
		}

		function showModal(customModalDefaults, customModalOptions) {
			if (!customModalDefaults){
				customModalDefaults = {};
			}
			customModalDefaults.backdrop = 'static';
			return showModalPopup(customModalDefaults, customModalOptions);
		}

	}
})();