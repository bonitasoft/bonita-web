/*
  *
*/
angular.module('common.directive.bandess',[]).directive('bandeDessinnee', function(){
  // Runs during compile
  return {
    // name: '',
    // priority: 1,
    // terminal: true,
    // scope: {}, // {} = isolate, true = child, false/undefined = no change
    // controller: function($scope, $element, $attrs, $transclude) {},
    // require: 'ngModel', // Array = multiple requires, ? = optional, ^ = check parent elements
     restrict: 'A', // N = Element, A = Attribute, C = Class, M = Comment
    // template: '',
     templateUrl: 'common/directive/bandess/bandess-tpl.html',
    // replace: true,
    // transclude: true,
    // compile: function(tElement, tAttrs, function transclude(function(scope, cloneLinkingFn){ return function linking(scope, elm, attrs){}})),
    link: function($scope, iElm, iAttrs, controller) {
      $scope.bds = [{
        'id': '3',
        'title': 'Des Fleurs et des marmots',
        'series': 'Donjon Parade',
        'author': 'Sfar & Trondheim',
        'drawer': 'Manu Larcenet'
      },{
        'id': '3',
        'title': 'Jeunesse qui s\'enfuit',
        'series': 'Donjon Potron-Minet',
        'author': 'Sfar & Trondheim',
        'drawer': 'Christophe Blain'
      }];
    }
  };
});
