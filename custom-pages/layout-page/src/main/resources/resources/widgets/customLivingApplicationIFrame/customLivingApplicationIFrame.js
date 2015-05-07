angular.module('org.bonitasoft.pagebuilder.widgets')
  .directive('customLivingApplicationIFrame', function() {
    return {
      controllerAs: 'ctrl',
      controller: function WidgetlivingApplicationIFrameController($scope, $http, $window) {
    var ctrl = this;
    
    var pathArray = $window.location.pathname.split( '/' ); 
    ctrl.applicationToken =  pathArray[pathArray.length-2]; 
    ctrl.pageToken =  pathArray[pathArray.length-1];
    ctrl.queryString = $window.location.search.substring(1);
    
    function getApplication() {
        return $http.get('../../API/living/application?c=1&f=token%3D'+ctrl.applicationToken);
    }
   
    function getTargetedApplicationPage(applicationId) {
        $http.get('../../API/living/application-page?c=1&f=token%3D'+ctrl.pageToken+'&f=applicationId%3D'+applicationId+'&d=pageId')
            .success(function(data) { 
                ctrl.customPageToken = data[0].pageId.urlToken;
            });
    }

    getApplication().then(function(response) {
        var application = response.data[0];
        ctrl.applicationId = application.id;
        return application.id;
    }).then(getTargetedApplicationPage);
     
    ctrl.getSrc = function(){
        return '/bonita/portal/custom-page/'+ctrl.customPageToken+'/?appToken='+ctrl.applicationToken+'&'+ctrl.queryString;
    };
    
},
      template: '\n<iframe id="livingAppIFrame" width="100%" height="{{properties.contentHeight}}" style="border: 0" ng-src="{{properties.src}}"></iframe>'
    };
  });
