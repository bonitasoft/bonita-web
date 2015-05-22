(function () {
  'use strict';
  angular.module('caseOverview').factory('dataSrvc', ['$http', function($http) {

    return {
      getData: function (resourceType, storageId) {
        // fetch data from server
        return $http({
          url: '/bonita/API/bdm/businessData/' + resourceType + '/' + storageId,
          method: 'GET'
        });
      },
      queryData: function (queryURL) {
        // query data from server
        return $http({
          url: '/bonita/' + queryURL,
          method: 'GET'
        });
      }
    };
  }]);
})();
