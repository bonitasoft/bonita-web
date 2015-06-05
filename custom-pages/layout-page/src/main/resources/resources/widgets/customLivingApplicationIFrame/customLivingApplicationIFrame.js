angular.module('org.bonitasoft.pagebuilder.widgets')
  .directive('customLivingApplicationIFrame', function() {
    return {
      controllerAs: 'ctrl',
      controller: function WidgetlivingApplicationIFrameController($scope, $element, $interval) {
    var iframe = $element.find('iframe')[0];
    
    var polling = $interval(function() {
        if(iframe.contentDocument.documentElement){
			iframe.style.height = (iframe.contentDocument.documentElement.scrollHeight || 400) + "px";
		}
    }, 100);
    
    $scope.$on('$destroy', function() {
        $interval.cancel(polling);
    });
},
      template: '<iframe width="100%" style="border: 0" scrolling="no" ng-src="{{properties.src}}"></iframe>'
    };
  });
