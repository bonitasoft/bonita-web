angular.module('org.bonitasoft.pagebuilder.widgets')
  .directive('pbParagraph', function() {
    return {
      template: '<p class="text-{{ properties.alignment }}" ng-bind-html="properties.text"></p>\n'
    };
  });
