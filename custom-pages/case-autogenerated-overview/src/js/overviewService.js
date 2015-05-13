(function () {
  'use strict';
  angular.module('caseOverview').factory('overviewSrvc', ['$http', 'archivedTaskAPI', 'contextSrvc', '$q', 'caseAPI', 'archivedCaseAPI','dataSrvc', function($http, archivedTaskAPI, contextSrvc, $q, caseAPI, archivedCaseAPI, dataSrvc) {

    var businessData = {};
    var responses = 0;
    var awaitedResponses = 0;

    var fetchValue = function(valueToFetch, deferred){
      // Implement fetching of data based on 2 strategies to illustrate the 2 possible capabilities. Using the link is
      // the most generic approach and should be preferred in most of the cases.
      // Using the type and value are most likely to be used to call a custom query on the API.
      if(angular.isObject(valueToFetch) && !angular.isArray(valueToFetch.value)){
        awaitedResponses = awaitedResponses +1;
        fetchDataFromTypeAndStorageId(valueToFetch, deferred);
      } else if(angular.isObject(valueToFetch) && angular.isArray(valueToFetch.value)) {
        awaitedResponses = awaitedResponses +1;
        fetchDataFromLink(valueToFetch, deferred);
      }
    };

    var fetchDataFromTypeAndStorageId = function(valueToFetch, deferred) {
      dataSrvc.getData(valueToFetch.type, valueToFetch.value).then(function(result){
        if(!angular.isDefined(businessData[valueToFetch.type])) {
          businessData[valueToFetch.type] = [];
        }
        businessData[valueToFetch.type].push(result.data);
        notifyResponse(deferred);

      });
    };

    var fetchDataFromLink = function(valueToFetch, deferred) {
      // Follow link to fetch multiple values
      dataSrvc.queryData(valueToFetch.link).then(function(result){
        if(!angular.isDefined(businessData[valueToFetch.type])) {
          businessData[valueToFetch.type] = [];
        }
        businessData[valueToFetch.type] = businessData[valueToFetch.type].concat(result.data);
        notifyResponse(deferred);
      });
    };

    var notifyResponse = function(deferred) {
      responses = responses + 1;
      console.log('responses: ' + responses);
      console.log('awaited: ' + awaitedResponses);
      if(responses === awaitedResponses) {
        deferred.resolve(businessData);
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
          var contextData;
          console.log(result.data);
          for (contextData in result.data) {
            fetchValue(result.data[contextData], deferred);
          }
        });
        return deferred.promise;
      },

      fetchCase: function(caseId){
        var deferred = $q.defer();
        caseAPI.get({id:caseId, d:'started_by'}, function(result){
          deferred.resolve(result);
        }, function(){
          archivedCaseAPI.search(
              {
                p:0,
                c:1,
                d:['started_by'],
                f:['sourceObjectId='+caseId]
              }
              , function(result){
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





