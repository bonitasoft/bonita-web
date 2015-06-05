'use strict';

describe('Case Overview test', function () {

  var ROOT_PATH = '../';

  var $httpBackend, contextSrvc;

  beforeEach(module('caseOverview'));

  beforeEach(function () {

    inject(function ($injector) {
      // Set up the mock http service responses
      $httpBackend = $injector.get('$httpBackend');
      contextSrvc = $injector.get('contextSrvc');
    });
  });

  afterEach(function () {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  it('should declare a fetch function', function () {

    expect(contextSrvc.fetchCaseContext).toBeTruthy();
  });

  it('should return a context', function () {

    var context = {
      processDefinitionId: '1',
      processInstanceId: '2',
      processInstanceInitiatorId: '1',
      businessData1: 'storageId1',
      businessData2: 'storageId2'
    };
    var response;

    $httpBackend.expect('GET', ROOT_PATH + 'API/bpm/case/' + 2 + '/context')
      .respond(context);
    contextSrvc.fetchCaseContext(2).then(function (fetchedData) {
      response = fetchedData.data;
    });
    $httpBackend.flush();
    expect(response).toEqual(context);
  });

});
