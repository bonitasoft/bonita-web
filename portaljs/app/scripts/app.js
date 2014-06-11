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
        templateUrl: '/bonita/views/main.html',
        controller: 'MainCtrl'
      }).when('/cases', {
        templateUrl: '/bonita/views/cases.html',
        controller: 'CasesCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
