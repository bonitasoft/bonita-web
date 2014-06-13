'use strict';

angular.module('portaljsApp')
    .controller('UsersCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
        $http.get('../API/identity/user?p=0&c=10&o=lastname%20ASC').success(function (data) {
            $scope.users = data;
        });
    }]);
