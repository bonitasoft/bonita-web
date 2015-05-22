(function () {
  'use strict';
  angular.module('caseOverview').factory('contextSrvc', ['$http', function($http) {

    return {
      fetchCaseContext: function (caseId) {
        // fetch context from server
        return $http({
          url: '/bonita/API/bpm/case/' + caseId + '/context',
          method: 'GET'
        });
      }
    };
  }]);
})();
