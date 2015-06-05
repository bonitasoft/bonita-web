(function () {
  'use strict';
  angular.module('caseOverview').factory('contextSrvc', ['$http', function($http) {

    var ROOT_PATH = '../';

    return {
      fetchCaseContext: function (caseId) {
        // fetch context from server
        return $http({
          url: ROOT_PATH + 'API/bpm/case/' + caseId + '/context',
          method: 'GET'
        });
      }
    };
  }]);
})();
