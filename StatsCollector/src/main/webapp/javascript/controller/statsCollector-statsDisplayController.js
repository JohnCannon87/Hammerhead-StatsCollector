/* global angular */
/* global moment */

(function() {

	'use strict';

	angular.module('statsCollectorApp').controller('StatsDisplayCtrl', StatsDisplayCtrl);

	StatsDisplayCtrl.$inject = ['SonarService', 'GerritService', 'ModalPopupService', 'DisplayParametersService', '$interval', '$location', '$scope', '$rootScope'];

	function StatsDisplayCtrl(SonarService, GerritService, ModalPopupService, DisplayParametersService, $interval, $location, $scope, $rootScope) {

		var vm = this;

		//Assign Functions

		vm.getAvatarClass = getAvatarClass;
		vm.editSonarMetricParameters = editSonarMetricParameters;
		vm.showOff = showOff;
		vm.showDisplayConfig = showDisplayConfig;
		vm.showDisplayConfig = showDisplayConfig;
		vm.redraw = redraw;

		//Do Things
		initialSetup();
		setupWatches();
		setupProjectName();
		setupCustomChartType();
		setupGerritChartSettings();
		setupSonarChartSettings();
		vm.sonarChart1 = {};
		vm.sonarChart2 = {};
		vm.sonarChart3 = {};
		vm.sonarChart4 = {};
		vm.extraChart1 = {};
		vm.extraChart2 = {};
		vm.extraChart3 = {};

		//Kick Off Timers, this should be done last out of the "Do Things" section so that all setup is completed beforehand.
		$interval(getGerritStats, 120000);//Every 2 Minutes
		$interval(getSonarStats, 300000);//Every 5 Minutes

		//Declare Functions
		function error(result){
			$rootScope.$broadcast('error-occured', { error: result});
		}

		function notify(msg, timeout, type){
			$rootScope.$broadcast('notify-occured', { notify: { statusText: msg}, timeout: timeout, type: type});
		}

		function setupWatches(){
			//Watches
			$scope.$watch(function (){
				return vm.selectedProject;
			},
			function(newValue, oldValue){
				var sameParam = false;
				if(newValue !== undefined && oldValue !== undefined){
					sameParam = newValue.gerritDisplayParameters.projectName === oldValue.gerritDisplayParameters.projectName;
				}
				if(!sameParam && newValue !== oldValue && vm.selectedProject !== undefined){
					vm.sonarStats = undefined;
					vm.reviewStats = undefined;
					vm.projectName = vm.selectedProject.gerritDisplayParameters.projectName;
				}
			});
			$scope.$watch(function (){
				return vm.projectName;
			},
			function (){
				setupProjectName();
				getParameters();
			});
			$scope.$watch(function (){
				return vm.extraMetric1;
			},
			function (newValue){
				updateSonarChart(newValue, vm.extraChart1);
			});
			$scope.$watch(function (){
				return vm.extraMetric2;
			},
			function (newValue){
				updateSonarChart(newValue, vm.extraChart2);
			});
			$scope.$watch(function (){
				return vm.extraMetric3;
			},
			function (newValue){
				updateSonarChart(newValue, vm.extraChart3);
			});

			$scope.$watch(function (){
				return vm.selectedMetric1;
			},
			function (newValue){
				updateSonarChart(newValue, vm.sonarChart1);
			});

			$scope.$watch(function (){
				return vm.parameters;
			},
			function (){
				updateSonarChart(vm.selectedMetric1, vm.sonarChart1);
				updateSonarChart(vm.selectedMetric2, vm.sonarChart2);
				updateSonarChart(vm.selectedMetric3, vm.sonarChart3);
				updateSonarChart(vm.selectedMetric4, vm.sonarChart4);
			});

			$scope.$watch(function (){
				return vm.selectedMetric2;
			},
			function (newValue){
				updateSonarChart(newValue, vm.sonarChart2);
			});
			$scope.$watch(function (){
				return vm.selectedMetric3;
			},
			function (newValue){
				updateSonarChart(newValue, vm.sonarChart3);
			});
			$scope.$watch(function (){
				return vm.selectedMetric4;
			},
			function (newValue){
				updateSonarChart(newValue, vm.sonarChart4);
			});
			$scope.$watchCollection(function (){
				return [vm.parameters, vm.gerritConfig];
			},
			function (){
				if(vm.parameters !== undefined && vm.gerritConfig !== undefined){
					getGerritStats();
				}
			});
			$scope.$watchCollection(function (){
				return [vm.parameters, vm.sonarConfig];
			},
			function (){
				if(vm.parameters !== undefined && vm.sonarConfig !== undefined){
					getSonarStats();
				}
			});
		}

		function initialSetup(){
			getGerritConfig();
			getSonarConfig();
		}

		function redraw(){
			updateSonarChart(vm.selectedMetric1, vm.sonarChart1);
			updateSonarChart(vm.selectedMetric2, vm.sonarChart2);
			updateSonarChart(vm.selectedMetric3, vm.sonarChart3);
			updateSonarChart(vm.selectedMetric4, vm.sonarChart4);
			updateSonarChart(vm.extraMetric1, vm.extraChart1);
			updateSonarChart(vm.extraMetric2, vm.extraChart2);
			updateSonarChart(vm.extraMetric3, vm.extraChart3);
		}

		function showDisplayConfig(){
			var modalDefaults = {
					backdrop : true,
					keyboard : true,
					modalFade : true,
					size: 'lg',
					templateUrl : '/fragments/DisplayConfigModal.html'
				};
			var modalOptions = {
				closeButtonText : 'Cancel',
				saveButtonText : 'Save',
				headerText : 'Display Config',
				displayParameters : vm.parameters,
				squishedProjects : vm.sonarStats.projectsSquished
			};
			modalOptions.displayParameters.sonarDisplayParameters.defaultMetric1 = vm.selectedMetric1.name;
			modalOptions.displayParameters.sonarDisplayParameters.defaultMetric2 = vm.selectedMetric2.name;
			modalOptions.displayParameters.sonarDisplayParameters.defaultMetric3 = vm.selectedMetric3.name;
			modalOptions.displayParameters.sonarDisplayParameters.defaultMetric4 = vm.selectedMetric4.name;
			if(vm.extraMetric1 !== undefined){
				modalOptions.displayParameters.sonarDisplayParameters.defaultExtraMetric1 = vm.extraMetric1.name;
			}
			if(vm.extraMetric2 !== undefined){
				modalOptions.displayParameters.sonarDisplayParameters.defaultExtraMetric2 = vm.extraMetric2.name;
			}
			if(vm.extraMetric3 !== undefined){
				modalOptions.displayParameters.sonarDisplayParameters.defaultExtraMetric3 = vm.extraMetric3.name;
			}

			ModalPopupService.showModal(modalDefaults, modalOptions).then(function(response){
				if(response){
					if(modalOptions.displayParameters.gerritDisplayParameters.projectName !== modalOptions.displayParameters.sonarDisplayParameters.projectName){
						//New name so wipe out old id and version fields
						modalOptions.displayParameters.gerritDisplayParameters.id = undefined;
						modalOptions.displayParameters.gerritDisplayParameters.version = undefined;
						modalOptions.displayParameters.sonarDisplayParameters.id = undefined;
						modalOptions.displayParameters.sonarDisplayParameters.version = undefined;
						modalOptions.displayParameters.sonarDisplayParameters.projectName = modalOptions.displayParameters.gerritDisplayParameters.projectName;
						modalOptions.displayParameters.sonarDisplayParameters.sonarTargetParam = {};
					}
					DisplayParametersService.saveParams(modalOptions.displayParameters).then(function(){
						$location.path('/'+vm.parameters.sonarDisplayParameters.projectName);
						getParameters();
					});
				}
			});
		}

		function showOff(sonarChart){
			if(sonarChart.targetStatusClass === 'btn-success'){
				createFirework(25,187,5,1,null,null,null,null,false,true);
				createFirework(25,187,5,1,null,null,null,null,false,true);
				createFirework(25,187,5,1,null,null,null,null,false,true);
				createFirework(25,187,5,1,null,null,null,null,false,true);
				createFirework(25,187,5,1,null,null,null,null,false,true);
			}else{
				var modalDefaults = {
						backdrop : true,
						keyboard : true,
						modalFade : true,
						size: 'md',
						templateUrl : '/fragments/NotHitTargetModal.html'
					};
				var modalOptions = {
					headerText : 'You should probably fix that...',
					closeButtonText : 'Close'
				};

				ModalPopupService.showModal(modalDefaults, modalOptions);
			}
		};

		function chartTargetClass(sonarChart){
			if(sonarChart.data !== undefined){
				var latestScore = sonarChart.data[0][sonarChart.data[0].length-1];
				sonarChart.latestScore = latestScore;
				var latestTarget = sonarChart.data[1][0];
				var trendDirection = sonarChart.trendDirection;
				var aboveTarget;
				if(latestScore > latestTarget){
					aboveTarget = true;
				}else if(latestScore < latestTarget){
					aboveTarget = false;
				}else{
					return 'btn-warning';
				}
				if(trendDirection === 'up'){
					if(aboveTarget){
						return 'btn-success';
					}else{
						return 'btn-danger';
					}
				}else if(trendDirection === 'down'){
					if(aboveTarget){
						return 'btn-danger';
					}else{
						return 'btn-success';
					}
				}
			}
			return '';
		}

		function setupProjectName(){
			if(vm.projectName === undefined){
				var projectName = $location.path();
				if(projectName === undefined || projectName === ''){
					vm.projectName = 'Default';
				}else{
					vm.projectName = projectName.substring(1);
				}
			}else{
				$location.path('/'+vm.projectName);
			}
		}

		function getParameters(){
			DisplayParametersService.getParams(vm.projectName).then(function(response){
				if(response.data === undefined){
					notify('No Project Data Found For Name: '+vm.projectName+' please set some up using the config items on the navbar.', 10000, 'danger');
					vm.projectName = 'Default';
					DisplayParametersService.getParams(vm.projectName).then(function(response){
						processParametersResponse(response.data);
					}, error);
				}else{
					processParametersResponse(response.data);
				}
			}, error);
		}

		function processParametersResponse(responseData){
			vm.projectList = responseData;
			for (let project of vm.projectList) {
				if(vm.projectName === project.gerritDisplayParameters.projectName){
					vm.selectedProject = project;
				}
				if(vm.selectedProject !== undefined && vm.selectedProject.gerritDisplayParameters.projectName === project.gerritDisplayParameters.projectName){
					vm.gerritStatsRequest = {
							gerritProjectRegex : project.gerritDisplayParameters.gerritRegex,
							gerritTopicRegex : project.gerritDisplayParameters.topicRegex,
							gerritProjectFilterOutRegex: project.gerritDisplayParameters.gerritFilterOutRegex,
							gerritStatus: 'merged',
							gerritStartDateOffset: '-30',
							gerritEndDateOffset: '0'
						};
					vm.sonarStatsRequest = {
						sonarProjectRegex : project.sonarDisplayParameters.sonarRegex
					};
					vm.sonarMaxDistance = 100;
					vm.sonarDistanceValue = project.sonarDisplayParameters.sonarHistoryLength;
					vm.parameters = project;
					break;
				}
			}
		}

		function editSonarMetricParameters(sonarMetric){
			var sonarMetricParameters;
			if(vm.parameters.sonarDisplayParameters.sonarTargetParam[sonarMetric.name] !== undefined){
				sonarMetricParameters = vm.parameters.sonarDisplayParameters.sonarTargetParam[sonarMetric.name];
			}else{
				vm.parameters.sonarDisplayParameters.sonarTargetParam[sonarMetric.name] = {
						target : 0,
						trendDirection : 'none',
						metricName : sonarMetric.name,
						chartType : 'line'
				};
				sonarMetricParameters = vm.parameters.sonarDisplayParameters.sonarTargetParam[sonarMetric.name];
			}
			var modalDefaults = {
					backdrop : true,
					keyboard : true,
					modalFade : true,
					size: 'lg',
					templateUrl : '/fragments/SonarTargetModal.html'
				};
			var modalOptions = {
				closeButtonText : 'Cancel',
				saveButtonText : 'Save',
				headerText : sonarMetricParameters.metricName+' Config',
				sonarMetricParameters : sonarMetricParameters
			};

			ModalPopupService.showModal(modalDefaults, modalOptions).then(function(response){
				if(response){
					vm.parameters.sonarDisplayParameters.sonarTargetParam[sonarMetric.name] = modalOptions.sonarMetricParameters;
					DisplayParametersService.saveParams(vm.parameters).then(function(){
						getParameters();
					});
				}
			});
		}

		function updateSonarChart(sonarMetric, sonarChart){
			if(sonarMetric !== undefined){
				var data = [];
				var targets = [];
				var labels = [];
				var target = 0;
				sonarChart.type = 'line';
				sonarChart.trendDirection = 'none';
				if(vm.parameters.sonarDisplayParameters.sonarTargetParam[sonarMetric.name] !== undefined){
					target = vm.parameters.sonarDisplayParameters.sonarTargetParam[sonarMetric.name].target;
					sonarChart.type = vm.parameters.sonarDisplayParameters.sonarTargetParam[sonarMetric.name].chartType;
					sonarChart.trendDirection = vm.parameters.sonarDisplayParameters.sonarTargetParam[sonarMetric.name].trendDirection;
				}

				var sonarStartTime = moment().subtract(vm.sonarDistanceValue, 'months').calendar();
				var dates = Object.keys(vm.sonarStats.sonarMetricPeriods).sort();
				if(sonarChart.type !== 'distribution'){
					for(var date in dates){
						if(moment(dates[date], 'YYYY-MM').isAfter(sonarStartTime, 'MM/dd/YYYY')){
							var period = vm.sonarStats.sonarMetricPeriods[dates[date]];
							labels.push(period.period.year+'-'+period.period.monthValue);
							data.push(getMetricValue(sonarMetric, period));
							targets.push(target);
						}
					}

					var fill = true;

					if(sonarChart.trendDirection === 'down'){
						fill = 'top';//Fill Target line to top of page if trend should be downwards
					}

					if(sonarChart.trendDirection === 'none'){
						targets = [];
						fill = false;
					}

					sonarChart.data = [data, targets];
					sonarChart.labels = labels;
					sonarChart.series = [sonarMetric.name, 'Target'];

					sonarChart.datasetOverride = [
			           {
			               pointBorderColor: '#e6ffee'
			           },
			           {
			        	   pointBorderColor: '#990000',
			        	   fill: fill
			           }
		 	        ];
					sonarChart.colors = ['#e6ffee', '#990000'];
					sonarChart.targetStatusClass = chartTargetClass(sonarChart);
				}else{
					sonarChart.type = 'bar';
					sonarChart.series = [];
					var series;
					for(date in dates){
						if(moment(dates[date], 'YYYY-MM').isAfter(sonarStartTime, 'MM/dd/YYYY')){
							period = vm.sonarStats.sonarMetricPeriods[dates[date]];
							series = Object.keys(period.sonarMetrics[sonarMetric.index].value);
							if(series.length > sonarChart.series.length){
								sonarChart.series = series;
							}
						}
					}
					for(date in dates){
						if(moment(dates[date], 'YYYY-MM').isAfter(sonarStartTime, 'MM/dd/YYYY')){
							period = vm.sonarStats.sonarMetricPeriods[dates[date]];
							labels.push(period.period.year+'-'+period.period.monthValue);
							period.sonarMetrics[sonarMetric.index].value
							getDistributedMetricValues(period.sonarMetrics[sonarMetric.index].value, sonarChart.series.length, data);
						}
					}
					sonarChart.data = data;
					sonarChart.labels = labels;
					var masterColorsArray = ['#990000','#994d00','#999900','#4d9900','#009900','#00994d','#009999','#004d99','#000099','#4d0099','#990099','#99004d',];
					sonarChart.colors = masterColorsArray.slice(0, data.length).reverse();
				}
			}
		}

		function getDistributedMetricValues(values, minimumLength, data){
			var keys = Object.keys(values);
			for(var index = 0; index < minimumLength; index++){
				if(data[index] === undefined){
					data[index] = [];
				}
				if(values[keys[index]]){
					data[index].push(values[keys[index]]);
				}else{
					data[index].push(0);
				}
			}
		}

		function getMetricValue(sonarMetric, period){
			if(sonarMetric.raw){
				return period.sonarMetrics[sonarMetric.index].value;
			}else{
				return period.derivedMetrics[sonarMetric.index].value;
			}
		}

		function updateGerritChart(gerritStats, gerritConfig){
			if(gerritConfig === undefined){
				//notify('Gerrit stats not drawn as config was unavailable', 5000, 'warning');
			}else if(gerritStats === undefined){
				//notify('Gerrit stats not drawn as stats are unloaded', 5000, 'warning');
			}else{
				vm.gerritPieChartData = [gerritStats.noPeerReviewCount, gerritStats.onePeerReviewCount, gerritStats.twoPlusPeerReviewCount];
				vm.gerritActivityChartData = [[],[]];
				vm.gerritActivityChartLabels = [];
				var dates = Object.keys(gerritStats.changeCountHistory).sort();
				var maxCount = 0;
				for (var day in dates){
					var totalActualReviewCount = gerritStats.changeCountHistory[dates[day]].onePeerReviewCount + gerritStats.changeCountHistory[dates[day]].twoPeerReviewCount
					vm.gerritActivityChartData[0].push(gerritStats.changeCountHistory[dates[day]].noPeerReviewCount);
					vm.gerritActivityChartData[1].push(totalActualReviewCount);
					vm.gerritActivityChartLabels.push(moment(dates[day]).format('MM-DD'));
					if(maxCount < totalActualReviewCount){
						maxCount = totalActualReviewCount;
					}
				}
				vm.reviewStats = [];
				var noPeerReviewStats = {
					count: gerritStats.noPeerReviewList.length,
					list: gerritStats.noPeerReviewList,
					type: 'No Peer Reviewers',
					displayClass: 'red'
				};
				var onePeerReviewStats = {
					count: gerritStats.onePeerReviewList.length,
					list: gerritStats.onePeerReviewList,
					type: 'One Peer Reviewers',
					displayClass: 'green'
				};
				var twoPeerReviewStats = {
					count: gerritStats.twoPlusPeerReviewList.length,
					list: gerritStats.twoPlusPeerReviewList,
					type: 'Two+ Peer Reviewers',
					displayClass:  'blue'
				};
				vm.reviewStats.push(noPeerReviewStats);
				vm.reviewStats.push(onePeerReviewStats);
				vm.reviewStats.push(twoPeerReviewStats);
			}
		}

		function updateSonarCharts(sonarStats, sonarConfig){
			if(sonarConfig === undefined){
				//notify('Sonar stats not drawn as config was unavailable', 5000, 'warning');
			}else if(sonarStats === undefined){
				//notify('Sonar stats not drawn as stats are unloaded', 5000, 'warning');
			}else{
				vm.listOfAvailableMetrics =[];
				for(var period in sonarStats.sonarMetricPeriods){
					if (sonarStats.sonarMetricPeriods.hasOwnProperty(period)) {
						if(vm.listOfAvailableMetrics.length === 0){
							vm.listOfAvailableMetrics = getListOfAvailableMetrics(sonarStats.sonarMetricPeriods[period]);
						}
					}
				}
				vm.sonarStats = sonarStats;
				vm.selectedMetric1 = findMetricCalled(vm.listOfAvailableMetrics, vm.parameters.sonarDisplayParameters.defaultMetric1);
				vm.selectedMetric2 = findMetricCalled(vm.listOfAvailableMetrics, vm.parameters.sonarDisplayParameters.defaultMetric2);
				vm.selectedMetric3 = findMetricCalled(vm.listOfAvailableMetrics, vm.parameters.sonarDisplayParameters.defaultMetric3);
				vm.selectedMetric4 = findMetricCalled(vm.listOfAvailableMetrics, vm.parameters.sonarDisplayParameters.defaultMetric4);
				vm.extraMetric1 = findMetricCalled(vm.listOfAvailableMetrics, vm.parameters.sonarDisplayParameters.defaultExtraMetric1);
				vm.extraMetric2 = findMetricCalled(vm.listOfAvailableMetrics, vm.parameters.sonarDisplayParameters.defaultExtraMetric2);
				vm.extraMetric3 = findMetricCalled(vm.listOfAvailableMetrics, vm.parameters.sonarDisplayParameters.defaultExtraMetric3);
			}
		}

		function findMetricCalled(list, metricName){
			for(var metric in list){
				if(list[metric].name === metricName){
					return list[metric];
				}
			}
			return undefined;
		}

		function getListOfAvailableMetrics(sonarPeriod){
			var metrics =[];
			for(var metric in sonarPeriod.sonarMetrics){
				metrics.push({name: sonarPeriod.sonarMetrics[metric].name, index: metric,raw: true});
			}
			for(var derived in sonarPeriod.derivedMetrics){
				metrics.push({name: sonarPeriod.derivedMetrics[derived].name, index: derived,raw: false});
			}
			return metrics;
		}

		function setupGerritChartSettings(){
			//Pie Chart
			vm.gerritPieChartLabels = ['No Peer Review', 'One Peer Review', 'Two+ Peer Review'];
			vm.gerritPieChartColors = ['#990000', '#009933', '#004d99'];
			vm.gerritPieChartOptions = {
					animation: {
						steps: '100',
						easing: 'easeOutBounce'
					},
					elements: {
						arc: {
							borderWidth: 0
						}
					},
					tooltips: {
						displayColors: false
					}
			};
			//Activity Chart
			vm.gerritActivityChartColors = ['#990000', '#e6ffee'];
			vm.gerritActivityChartSeries = ['No Peer Reviews', 'Peer Reviewed'];
			vm.gerritActivityChartOptions = {
					legend: {
						display: true,
						labels: {
							fontColor: '#bbbbbb'
						}
					},
					elements: {
						line: {
							fill: false
						}
					},
					scales: {
						xAxes: [{
							gridLines: {
								color: '#333333'
							}
						}],
						yAxes: [{
							gridLines: {
								color: '#333333'
							}
						}]
					},
					animation: {
						steps: '100',
						easing: 'easeOutBounce'
					}
			};

			vm.gerritActivityDatasetOverride = [
			               {
			                   pointBorderColor: '#990000'
			               },
			               {
			            	   pointBorderColor: '#e6ffee'
			               }
	        ];
		}
		function setupSonarChartSettings(){
			vm.sonarChartOptions = {
					elements: {
						line: {
							fill: false
						}
					},
					scales: {
						xAxes: [{
							gridLines: {
								color: '#333333'
							}
						}],
						yAxes: [{
							gridLines: {
								color: '#333333'
							}
						}]
					},
					animation: {
						steps: '100',
						easing: 'easeOutBounce'
					}
			};
		}

		function getGerritStats(){
			GerritService.getReviewStats(vm.gerritStatsRequest).then(function(response){
				vm.gerritReviewStats = response.data;
			}, error);
			GerritService.getStats(vm.gerritStatsRequest).then(function(response){
				vm.gerritStats = response.data;
				notify('Gerrit Stats Reloaded', 2000, 'info');
				updateGerritChart(vm.gerritStats, vm.gerritConfig);
			}, error).then(function(){
				$scope.$watch(function (){
					return vm.gerritStats;
				},
				function (newValue){
					updateGerritChart(newValue, vm.gerritConfig);
				});
			});
		}

		function getSonarStats(){
			SonarService.getStats(vm.sonarStatsRequest).then(function(response){
				vm.sonarStats = response.data;
				notify('Sonar Stats Reloaded', 2000, 'info');
				updateSonarCharts(vm.sonarStats, vm.sonarConfig);
			}, error).then(function(){
				$scope.$watch(function (){
					return vm.sonarStats;
				},
				function (newValue){
					updateSonarCharts(newValue, vm.sonarConfig);
				});
			});
		}

		function getGerritConfig(){
			GerritService.getConfig().then(function(response){
				vm.gerritConfig = response.data;
			}, error).then(function(){
				$scope.$watch(function (){
					return vm.gerritConfig;
				},
				function (newValue){
					updateGerritChart(vm.gerritStats, newValue);
				});
			});
		}

		function getSonarConfig(){
			SonarService.getConfig().then(function(response){
				vm.sonarConfig = response.data;
			}, error).then(function(){
				$scope.$watch(function (){
					return vm.sonarConfig;
				},
				function (newValue){
					updateSonarCharts(vm.sonarStats, newValue);
				});
			});
		}

		function getAvatarClass(avatarUrl) {
			if (typeof avatarUrl === "undefined") {
				return "hidden";
			} else {
				return "img-circle";
			}
		}

		function setupCustomChartType() {
			Chart.defaults.LineUp = Chart.defaults.line;
			var custom = Chart.DatasetController.extend(Chart.controllers.line.prototype);
			Chart.controllers.LineUp = custom;
		}
	}
})();
