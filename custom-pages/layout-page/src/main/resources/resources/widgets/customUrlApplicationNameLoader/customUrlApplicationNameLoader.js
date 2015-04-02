angular.module('org.bonitasoft.pagebuilder.widgets')
  .directive('customUrlApplicationNameLoader', function() {
    return {
      controllerAs: 'ctrl',
      controller: function($scope, $location) {
    var urlAsTab = $location.absUrl().split("/");
    $scope.parameters.applicationName = urlAsTab[urlAsTab.length-2];
    $scope.parameters.pageToken = urlAsTab[urlAsTab.length-1];
},
      template: ''
    };
  });
