angular.module('org.bonitasoft.pagebuilder.widgets')
  .directive('customLivingApplicationIFrame', function() {
    return {
      controllerAs: 'ctrl',
      controller: function WidgetlivingApplicationIFrameController($scope) {
},
      template: '\n<iframe id="livingAppIFrame" width="100%" height="{{properties.contentHeight}}" style="border: 0" ng-src="{{properties.src}}"></iframe>'
    };
  });
