var app = angular.module('app', ['appGerritStats'])

app.constant('gerritAppConfig', {
	gerrit: {
		baseUrl: 'http://localhost:8080'
	}	
})