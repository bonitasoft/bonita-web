(function () {
  'use strict';
  angular.module('caseOverview').factory('dataSrvc', ['$http', function($http) {

    var ROOT_PATH = '../';

    return {
      getData: function (resourceType, storageId) {
        // fetch data from server
        return $http({
          url: ROOT_PATH + 'API/bdm/businessData/' + resourceType + '/' + storageId,
          method: 'GET'
        });
      },
      queryData: function (queryURL) {
        // query data from server
        return $http({
          url: ROOT_PATH + queryURL,
          method: 'GET'
        });
      }
    };
  }]);
})();
