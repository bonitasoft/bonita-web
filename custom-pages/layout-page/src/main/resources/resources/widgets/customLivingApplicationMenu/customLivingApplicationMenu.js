angular.module('bonitasoft.ui.widgets')
  .directive('customLivingApplicationMenu', function() {
    return {
      controllerAs: 'ctrl',
      controller: function WidgetlivingApplicationMenuController($scope, $http, $window) {
    var ctrl = this;
    var pathArray = $window.location.pathname.split( '/' ); 
    ctrl.applicationToken =  pathArray[pathArray.length-3]; 
    ctrl.pageToken =  pathArray[pathArray.length-2];
    
    this.isSticky = function() {
        $window.document.body.style.paddingTop = $scope.properties.sticky ? "70px": "0px";
        return $scope.properties.sticky;
    }

    function getApplication() {
        return $http.get('../API/living/application/?c=1&f=token%3D'+ctrl.applicationToken);
    }
    
    this.filterChildren = function (parentId) {
        return (ctrl.applicationMenuList||[]).filter(function(menu){
            return menu.parentMenuId === '' + parentId;
        });
        
    }
   
    function getApplicationMenuList(applicationId) {
        
        $http.get('../API/living/application-menu/?c=100&f=applicationId%3D'+applicationId+'&d=applicationPageId&o=menuIndex+ASC')
            .success(function(data) { 
                ctrl.applicationMenuList = data;
            });
        return applicationId;
    }

    function setTargetedUrl() {
        $scope.properties.targetUrl = "../../../portal/resource/app/"+ctrl.applicationToken+"/"+ ctrl.pageToken+"/content/"+ $window.location.search;
    }

    ctrl.isParentMenu= function(menu) {
        return menu.parentMenuId==-1 && menu.applicationPageId==-1;
    };
    
    getApplication().then(function(response) {
        var application = response.data[0];
        ctrl.applicationName = application.displayName;
        return application.id;
    }).then(getApplicationMenuList).then(setTargetedUrl);
    
},
      template: '<div class="navbar navbar-inverse" ng-class="{\'navbar-fixed-top\': ctrl.isSticky() }" role="navigation">\n    <div class="container">\n        <div class="container">\n            <div class="navbar-header">\n                <a class="navbar-brand">{{ctrl.applicationName}}</a>\n                <button type="button" ng-init="navCollapsed = true" ng-click="navCollapsed = !navCollapsed" class="navbar-toggle">\n                    <span class="icon-bar"></span>\n                    <span class="icon-bar"></span>\n                    <span class="icon-bar"></span>\n                </button>\n            </div>\n            <div collapse="navCollapsed" class="collapse navbar-responsive-collapse navbar-collapse">\n                <ul class="nav navbar-nav">\n                    <li ng-class="{active:ctrl.pageToken===menu.applicationPageId.token}" ng-repeat="menu in ctrl.filterChildren(-1)" dropdown>\n                        <a ng-if="!ctrl.isParentMenu(menu)" ng-href="../{{menu.applicationPageId.token}}/" >{{menu.displayName}}</a>\n                        <a ng-if="ctrl.isParentMenu(menu)" dropdown-toggle>{{menu.displayName}}<span class="caret"></span></a>\n                        <ul ng-if="ctrl.isParentMenu(menu)" class="dropdown-menu">\n                            <li ng-repeat="childMenu in ctrl.filterChildren(menu.id)">\n                                <a ng-href="../{{childMenu.applicationPageId.token}}/">{{childMenu.displayName}}</a>\n                            </li>\n                        </ul>\n                    </li>\n                </ul>\n            </div>\n        </div>\n    </div>\n</div>'
    };
  });
