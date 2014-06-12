'use strict';

angular.module('portaljsApp')
    .controller('UsersCtrl', ['$scope', '$http', function ($scope, $http) {
        $http.get('API/identity/user?p=0&c=10&o=lastname%20ASC').success(function (data) {
            $scope.users = data;
        });
    }]);
