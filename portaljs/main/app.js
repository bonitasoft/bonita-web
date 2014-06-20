'use strict';

angular
    .module('portaljsApp', [
        'ngCookies',
        'ngResource',
        'ngSanitize',
        'ngRoute'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'features/poc/views/main.html',
                controller: 'MainCtrl'
            }).when('/cases', {
                templateUrl: 'features/poc/views/cases.html',
                controller: 'CasesCtrl'
            }).when('/users', {
                templateUrl: 'features/poc/views/users.tpl.html',
                controller: 'UsersCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
