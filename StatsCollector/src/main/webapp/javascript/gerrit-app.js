var app = angular.module('app', ['appGerritStats'])

app.constant('gerritAppConfig', {
	gerrit: {
		baseUrl: 'http://localhost:8080',
		goals: {
			noPeerReviews: 0.05
		},
		projectsRegex: '.*Ojp.*'
	}	
})