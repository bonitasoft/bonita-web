angular.module('org.bonitasoft.pagebuilder.widgets')
  .directive('pbSelect', function() {
    return {
      controllerAs: 'ctrl',
      controller: function PbSelectCtrl($scope, $parse, widgetNameFactory) {
  var ctrl = this;

  function comparator(initialValue, item) {
    return angular.equals(initialValue, ctrl.getValue(item));
  }

  function createGetter(accessor) {
    return accessor && $parse(accessor);
  }

  this.getLabel = createGetter($scope.properties.displayedKey) || function (item) {
    return typeof item === 'string' ? item : JSON.stringify(item);
  };

  this.getValue = createGetter($scope.properties.returnedKey) || function (item) {
    return item;
  };

  $scope.$watch('properties.availableValues', function(items){
    if (Array.isArray(items)) {
      $scope.properties.value = items
        .filter(comparator.bind(null, $scope.properties.value))
        .reduce(function (acc, item) {
          return ctrl.getValue(item);
        }, undefined);
    }
  });

  this.name = widgetNameFactory.getName('pbSelect');
}
,
      template: '<div ng-class="{\n    \'form-horizontal\': properties.labelPosition === \'left\' && !properties.labelHidden,\n    \'row\': properties.labelPosition === \'top\' && !properties.labelHidden || properties.labelHidden\n    }">\n    <div class="form-group">\n        <label\n            ng-if="!properties.labelHidden"\n            ng-class="{ \'control-label--required\': properties.required }"\n            class="control-label col-xs-{{ !properties.labelHidden && properties.labelPosition === \'left\' ? properties.labelWidth : 12 }}">\n            {{ properties.label }}\n        </label>\n        <div class="col-xs-{{ 12 - (!properties.labelHidden && properties.labelPosition === \'left\' ? properties.labelWidth : 0) }}" >\n            <select\n                class="form-control"\n                name="{{ctrl.name}}"\n                ng-model="properties.value"\n                ng-options="ctrl.getValue(option) as ctrl.getLabel(option) for option in properties.availableValues"\n                ng-required="properties.required"\n                ng-disabled="properties.disabled">\n                <option style="display:none" value="">\n                    {{properties.placeholder}}\n                </option>\n            </select>\n            <div ng-messages="$form[ctrl.name].$dirty && $form[ctrl.name].$error " ng-messages-include="forms-generic-errors.html" role="alert"></div>\n        </div>\n    </div>\n</div>\n'
    };
  });
