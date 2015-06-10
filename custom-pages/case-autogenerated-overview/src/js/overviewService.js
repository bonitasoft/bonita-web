(function () {
  'use strict';
  angular.module('caseOverview').factory('overviewSrvc', ['$http', 'archivedTaskAPI', 'contextSrvc', '$q', 'caseAPI', 'archivedCaseAPI','dataSrvc', function($http, archivedTaskAPI, contextSrvc, $q, caseAPI, archivedCaseAPI, dataSrvc) {

    var businessData;
    var documentRefs;
    var responses = 0;
    var awaitedResponses = 0;

    var fetchValue = function(valueToFetch, deferred){
      // Implement fetching of data based on 2 strategies to illustrate the 2 possible capabilities. Using the link is
      // the most generic approach and should be preferred in most of the cases.
      // Using the type and value are most likely to be used to call a custom query on the API.
      if(angular.isObject(valueToFetch) && valueToFetch.storageId){
        fetchDataFromTypeAndStorageId(valueToFetch, deferred);
      } else if(angular.isObject(valueToFetch) && angular.isArray(valueToFetch.storageIds)) {
        fetchDataFromLink(valueToFetch, deferred);
      } else if(angular.isObject(valueToFetch) && valueToFetch.fileName){
        /* Element in context is a reference to a document */
        if(!angular.isDefined(documentRefs)) {
          documentRefs = [];
        }
        if(angular.isArray(valueToFetch)){
          documentRefs = documentRefs.concat(valueToFetch);
        } else {
          documentRefs.push(valueToFetch);
        }
        notifyResponse(deferred);
      } else {
        console.log('ignoring context entry', valueToFetch);
        // ignore value as it is not initialised.
        notifyResponse(deferred);
      }
    };

    var fetchDataFromTypeAndStorageId = function(valueToFetch, deferred) {
      dataSrvc.getData(valueToFetch.type, valueToFetch.storageId).then(function(result){
        initBusinessDataForType(valueToFetch.type);
        businessData[valueToFetch.type].push(result.data);
        notifyResponse(deferred);

      });
    };

    var fetchDataFromLink = function(valueToFetch, deferred) {
      // Follow link to fetch multiple values
      dataSrvc.queryData(valueToFetch.link).then(function(result){
        initBusinessDataForType(valueToFetch.type);
        businessData[valueToFetch.type] = businessData[valueToFetch.type].concat(result.data);
        notifyResponse(deferred);
      });
    };

    var initBusinessDataForType = function(type) {
      if(!angular.isDefined(businessData)) {
        businessData = {};
      }
      if(!angular.isDefined(businessData[type])) {
        businessData[type] = [];
      }
    };

    var notifyResponse = function(deferred) {
      responses = responses + 1;
      if(responses === awaitedResponses) {
        deferred.resolve({businessData: businessData, documents: documentRefs});
      }
    };

    var initNumberOfAwaitedResponses = function(obj) {
      var prop;
      for (prop in obj) {
        awaitedResponses = awaitedResponses + 1;
      }
    };

    return {
      listDoneTasks: function(caseId){
        return archivedTaskAPI.search({
          p:0,
          c:50,
          d:['executedBy'],
          f:['caseId='+caseId],
          o:['reached_state_date DESC']
        }).$promise;
      },
      fetchContext: function(caseId){
        var deferred = $q.defer();
        contextSrvc.fetchCaseContext(caseId).then(function(result){

          initNumberOfAwaitedResponses(result.data);

          var contextData;
          for (contextData in result.data) {
            fetchValue(result.data[contextData], deferred);
          }
        });
        return deferred.promise;
      },

      fetchCase: function(caseId){
        var deferred = $q.defer();
        caseAPI.get({id:caseId, d:['started_by','processDefinitionId']}, function(result){
          deferred.resolve(result);
        }, function(){
          archivedCaseAPI.search(
              {
                p:0,
                c:1,
                d:['started_by','processDefinitionId'],
                f:['sourceObjectId='+caseId]
              }, function(result){
            deferred.resolve(result.data[0]);
          }, function(){
            deferred.reject('Case not found!');
          });
        });
        return deferred.promise;
      }
    };
  }]);
})();





