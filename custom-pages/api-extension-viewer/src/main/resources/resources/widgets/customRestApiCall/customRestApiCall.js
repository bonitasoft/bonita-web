angular.module('org.bonitasoft.pagebuilder.widgets')
  .directive('customRestApiCall', function() {
    return {
      controllerAs: 'ctrl',
      controller: function WidgetrestApiCallController($scope,$http) {
    
    this.initData= function(){
    // $scope.queryString=$scope.properties.queryString;
   //  alert('init');
     $scope.postPayload='{\n"param1":"abc",\n"param2":123,\n"param3":true\n}';
    }
    
    
    this.callApi = function() {
         $scope.response={};
         
         var apiUrl= '/bonita/API/extension/' + $scope.properties.pathTemplate;
         if ($scope.properties.queryString){
             apiUrl+= "?"+ $scope.properties.queryString ;
         }
         
         $http({method:  $scope.properties.method, url: apiUrl, data: $scope.postPayload })
            .success(function (data, status, headers, config) {
                $scope.response.respData = data;
                $scope.response.respStatus = status;
                $scope.response.respHeaders = headers('awesome header');
            })
            .error(function (data, status, headers, config) {
                $scope.response.respData = data;
                $scope.response.respStatus = status;
            });
        
    };
    
     this.clear = function() {
                $scope.response = null;
                
    };
        
    
    
},
      template: '<hr>\n<div ng-show="properties.method" ng-init="ctrl.initData()">\n    <h3>{{properties.description}}</h3>\n\n    <p>{{properties.method}} | ../API/extension/{{properties.pathTemplate}}\n        <span ng-show="properties.queryString">?<input size="50" type="text" ng-show="properties.queryString" ng-model="properties.queryString"></span>\n    \n    <p ng-show="properties.method==\'POST\'">json POST payload:<br/>\n        <textarea  ng-model="postPayload" style="width: 300px;height: 120px"></textarea>\n    </p>\n    \n    <p>\n        <input type="submit" class="btn btn-primary" ng-click="ctrl.callApi()">\n        <input type="button" class="btn btn-primary" ng-click="ctrl.clear()" value="Clear">\n    </p>\n    <div ng-show="response">\n   \n        <p>status:</p>\n        <pre>{{response.respStatus}}</pre>\n        <p>returned data:</p>\n        <pre>{{response.respData}}</pre>\n        <p ng-show="response.respHeaders">headers:</p>\n        <pre ng-show="response.respHeaders">{{response.respHeaders}}</pre>\n\n    </div>\n    \n    \n</div>'
    };
  });
