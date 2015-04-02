angular.module('org.bonitasoft.pagebuilder.widgets')
  .directive('customLivingApplicationMenu', function() {
    return {
      controllerAs: 'ctrl',
      controller: function($scope, $location) {

    this.selectTargetedMenu = function(menu) {
        $scope.parameters.selectedMenu = menu;
    };
    
    this.hasChildren= function(menu, menuList) {
        var children = menuList.filter(function(currentMenu){return currentMenu.parentMenuId===menu.id});
        return children.length > 0;
    }

},
      template: '\n <nav class="navbar-inverse navbar" role="navigation>\n    <div class="navbar-header">\n      <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target="#application-menu">\n        <span class="sr-only">Toggle navigation</span>\n        <span class="icon-bar"></span>\n        <span class="icon-bar"></span>\n        <span class="icon-bar"></span>\n      </button>\n    </div>\n    <div class="collapse navbar-collapse" id="application-menu">\n      <ul class="nav navbar-nav">\n        <li dropdown="" ng-class="{active:parameters.selectedPageToken===menu.applicationPageId.token} {dropdown:ctrl.hasChildren(menu, parameters.menuList)===true} {open:ctrl.hasChildren(menu, parameters.menuList)===true}" ng-repeat="menu in parameters.menuList | filter:{parentMenuId:-1}">\n			<a ng-show="!ctrl.hasChildren(menu, parameters.menuList)" href="{{menu.applicationPageId.token}}" >{{menu.displayName}}</a>\n				\n            <a ng-show="ctrl.hasChildren(menu, parameters.menuList)" role="button" class="dropdown-toggle" dropdown-toggle="" aria-haspopup="true" aria-expanded="true">\n                {{menu.displayName}}<span class="caret"></span>\n            </a>\n    		<ul ng-show="ctrl.hasChildren(menu, parameters.menuList)" class="dropdown-menu">\n			    <li ng-repeat="childrenMenu in parameters.menuList | filter:{parentMenuId:menu.id}"><a href="{{childrenMenu.applicationPageId.token}}">{{childrenMenu.displayName}}</a>\n			    </li>\n		    </ul>\n        </li>\n      </ul>\n    </div>\n  </nav>'
    };
  });
