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
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      }).when('/cases', {
        templateUrl: 'views/cases.html',
        controller: 'CasesCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
