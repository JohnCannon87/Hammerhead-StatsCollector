var app = angular.module('app', ['appGerritStats'])

app.constant('appConfig', {
	collector: {
		baseUrl: 'http://localhost:8080'
	}	
})