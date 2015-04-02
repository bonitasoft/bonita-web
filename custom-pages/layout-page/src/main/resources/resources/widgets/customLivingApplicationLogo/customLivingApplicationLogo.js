angular.module('org.bonitasoft.pagebuilder.widgets')
  .directive('customLivingApplicationLogo', function() {
    return {
      controllerAs: 'ctrl',
      controller: function() {},
      template: '<img src="{{parameters.src}}" width="{{ parameters.width }}" height="{{ parameters.height }}" style="{{ parameters.style }}">'
    };
  });
