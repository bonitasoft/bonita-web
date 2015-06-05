(function () {
  'use strict';
  var app = angular.module('caseOverview', [
    'ui.bootstrap',
    'ngResource',
    'org.bonita.common.resources',
    'angular-timeline'
    ]);

  app.controller('MainCtrl', ['$scope','$window', 'archivedTaskAPI', '$location', 'overviewSrvc', '$http', function ($scope, $window, archivedTaskAPI, $location, overviewSrvc, $http) {

    $scope.case = {};

    $scope.isInternalField = function(propertyName) {
     return (propertyName === 'persistenceId') || (propertyName === 'persistenceVersion')|| (propertyName === 'links');
    };

    var caseId = $location.search().id;

    $http({ method: 'GET', url: '../API/system/session/unusedId' })
    .success(function(data, status, headers) {
            $http.defaults.headers.common['X-Bonita-API-Token'] = headers('X-Bonita-API-Token');
            init();
        }
    );

    var init = function() {
        overviewSrvc.fetchCase(caseId).then(function(result){
            $scope.case = result;
        });
        overviewSrvc.listDoneTasks(caseId).then(function mapArchivedTasks(data){
            $scope.doneTasks = data;
        });
        overviewSrvc.fetchContext(caseId).then(function(data){
            $scope.businessData = data.businessData;
            $scope.documents = data.documents;
        });
    };
  }])

  .config(['$locationProvider',function($locationProvider) {
    $locationProvider.html5Mode({
      enabled: true,
      requireBase: false
    });
  }]);


})();
