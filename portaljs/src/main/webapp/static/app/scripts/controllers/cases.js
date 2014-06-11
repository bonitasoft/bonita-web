'use strict';

angular.module('portaljsApp')
  .controller('CasesCtrl', ['$scope', '$cookies', function ($scope, $cookies) {
    $scope.cases=[{'name' :'Case1'}, {'name' :'Case2'}];
    console.log($cookies);
  }]);
