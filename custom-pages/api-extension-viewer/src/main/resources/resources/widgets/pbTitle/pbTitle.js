angular.module('org.bonitasoft.pagebuilder.widgets')
  .directive('pbTitle', function() {
    return {
      template: '<h1 ng-if="\'Level 1\' === properties.level" class="text-{{ properties.alignment }}">{{properties.text}}</h1>\n<h2 ng-if="\'Level 2\' === properties.level" class="text-{{ properties.alignment }}">{{properties.text}}</h2>\n<h3 ng-if="\'Level 3\' === properties.level" class="text-{{ properties.alignment }}">{{properties.text}}</h3>\n<h4 ng-if="\'Level 4\' === properties.level" class="text-{{ properties.alignment }}">{{properties.text}}</h4>\n<h5 ng-if="\'Level 5\' === properties.level" class="text-{{ properties.alignment }}">{{properties.text}}</h5>\n<h6 ng-if="\'Level 6\' === properties.level" class="text-{{ properties.alignment }}">{{properties.text}}</h6>\n'
    };
  });
