'use strict';

angular.module('portaljsApp')
  .controller('CasesCtrl', ['$scope', '$location', function ($scope, $location) {
    $scope.cases=[{'name' :'Case1'}, {'name' :'Case2'}];
    $scope.goToUsersLink= '#'+$location.path() + '?_p=userlistingadmin&_f=enabledusers';

  }]);
