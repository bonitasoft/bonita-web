angular.module('org.bonitasoft.pagebuilder.widgets')
  .directive('customIframe', function() {
    return {
      controllerAs: 'ctrl',
      controller: function($scope, $sce){
     this.getSrc = function(){
         return $sce.trustAsResourceUrl($scope.parameters.src);
         
     }
},
      template: '<iframe width="100%" height="{{parameters.contentHeight}}" style="border: 0" ng-src="{{ctrl.getSrc()}}"></iframe>'
    };
  });
