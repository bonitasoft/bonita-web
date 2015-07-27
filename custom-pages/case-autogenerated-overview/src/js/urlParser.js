(function () {
  'use strict';
  angular.module('caseOverview').factory('urlParser', ['$window', function($window) {

    return {
      getQueryStringParamValue: function (paramName) {
        // return the value of the requested query string parameter
          var regExpGetId = new RegExp("([\\?&]|^)" + paramName + "=(\\d+)");
          var searshMatcher = regExpGetId.exec($window.location.search);
          return searshMatcher?searshMatcher[2]:'';
      }
    };
  }]);
})();
