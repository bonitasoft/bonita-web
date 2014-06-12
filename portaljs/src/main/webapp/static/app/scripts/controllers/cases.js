'use strict';

angular.module('portaljsApp')
  .controller('CasesCtrl', ['$scope', '$location', function ($scope, $location) {
    $scope.cases=[{'name' :'Case1'}, {'name' :'Case2'}];
  }]);
