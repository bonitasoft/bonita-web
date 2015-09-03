angular.module('bonitasoft.ui.widgets')
  .directive('customLivingApplicationIFrame', function() {
    return {
      controllerAs: 'ctrl',
      controller: function WidgetlivingApplicationIFrameController($scope, $element, $interval, $sce) {
    var iframe = $element.find('iframe')[0];
    
    var polling = $interval(function() {
        iframe.style.height = (iframe.contentWindow.document.body.scrollHeight || 400) + "px";
    }, 100);
    
    $scope.$on('$destroy', function() {
        $interval.cancel(polling);
    });
    
    $scope.secureUrl = function() {
        return $sce.trustAsURL($scope.properties.src);
    }
},
      template: '<iframe width="100%" style="border: 0" scrolling="no" ng-src="{{secureUrl(properties.src)}}"></iframe>'
    };
  });
