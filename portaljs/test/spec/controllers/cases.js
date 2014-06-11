'use strict';

describe('Controller: CasesCtrl', function () {

  // load the controller's module
  beforeEach(module('portaljsApp'));

  var CasesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CasesCtrl = $controller('CasesCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
