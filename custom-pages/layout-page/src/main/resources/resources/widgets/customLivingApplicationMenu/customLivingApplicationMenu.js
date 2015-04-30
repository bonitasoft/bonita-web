angular.module('org.bonitasoft.pagebuilder.widgets')
  .directive('customLivingApplicationMenu', function() {
    return {
      controllerAs: 'ctrl',
      controller: function WidgetlivingApplicationMenuController($scope, $http, $window) {
    var ctrl = this;
    
    var pathArray = $window.location.pathname.split( '/' ); 
    ctrl.applicationToken =  pathArray[pathArray.length-2]; 
    ctrl.pageToken =  pathArray[pathArray.length-1];

    function getApplication() {
        return $http.get('../../API/living/application?c=1&f=token%3D'+ctrl.applicationToken);
    }
   
    function getApplicationMenuList(applicationId) {
        $http.get('../../API/living/application-menu?c=100&f=applicationId%3D'+applicationId+'&d=applicationPageId&o=menuIndex+ASC')
            .success(function(data) { 
                ctrl.applicationMenuList = data;
            });
    }

    ctrl.hasChildren= function(menu) {
        var children = ctrl.applicationMenuList.filter(function(currentMenu){return currentMenu.parentMenuId===menu.id});
        return children.length > 0;
    };
    
    getApplication().then(function(response) {
        var application = response.data[0];
        ctrl.applicationName = application.displayName;
        return application.id;
    }).then(getApplicationMenuList);
},
      template: '<div class="navbar navbar-inverse" role="navigation">\n      <div class="container">\n         <div class="container">  \n			<div class="navbar-header">\n				<a class="navbar-brand">{{ctrl.applicationName}}</a>\n		        <button type="button" ng-init="navCollapsed = true" ng-click="navCollapsed = !navCollapsed" class="navbar-toggle">\n                   <span class="icon-bar"></span>\n                   <span class="icon-bar"></span>\n                   <span class="icon-bar"></span>\n                </button>\n			</div>\n			<div collapse="navCollapsed" class="collapse navbar-responsive-collapse navbar-collapse">\n		      <ul class="nav navbar-nav">\n				<li ng-class="{active:ctrl.pageToken===menu.applicationPageId.token}" ng-repeat="menu in ctrl.applicationMenuList | filter:{parentMenuId:-1}" dropdown>\n					<a ng-if="!ctrl.hasChildren(menu)" href="{{menu.applicationPageId.token}}" >{{menu.displayName}}</a>            \n				    <a ng-if="ctrl.hasChildren(menu)" dropdown-toggle>{{menu.displayName}}<span class="caret"></span></a>\n				    <ul ng-if="ctrl.hasChildren(menu)" class="dropdown-menu">\n						<li ng-repeat="childrenMenu in ctrl.applicationMenuList | filter:{parentMenuId:menu.id}">\n							<a href="{{childrenMenu.applicationPageId.token}}">{{childrenMenu.displayName}}</a>\n						</li>\n				    </ul>\n				</li>\n			   </ul>\n		    </div>\n	    </div>\n    </div>\n</div>\n'
    };
  });
