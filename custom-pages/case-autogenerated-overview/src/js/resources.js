(function () {
  'use strict';
  /**
   * @ngdoc service
   * @name bonita.common.resources.Resources
   * @description
   * Define the resources accessible from the Bonita API
   */
  var ROOT_PATH = '../';
  var API_PATH = ROOT_PATH + 'API/';

  /**
   * @internal
   * Parse Content-Range header and return an object with pagination infos
   * @param  {String} strContentRange Content-Range header attribute
   * @return {Object}                 pagination object
   */
  function parseContentRange(strContentRange) {
    if (strContentRange === null) {
      return {};
    }
    var arrayContentRange = strContentRange.split('/');
    var arrayIndexNumPerPage = arrayContentRange[0].split('-');
    return {
      total: parseInt(arrayContentRange[1], 10),
      index: parseInt(arrayIndexNumPerPage[0], 10),
      currentPage: parseInt(arrayIndexNumPerPage[0], 10) + 1,
      numberPerPage: parseInt(arrayIndexNumPerPage[1], 10)
    };
  }

  var resourceDecorator = ['$delegate', function ($delegate) {
    return function (url, paramDefaults, actions, options) {
      actions = angular.extend({}, actions, {
        'search': {
          isArray: true,
          interceptor: {
            response: function (response) {
              response.resource.pagination = parseContentRange(response.headers('Content-Range'));
              return response;
            }
          }
        },
        'update': {
          method: 'PUT'
        }
      });
      return $delegate(url, paramDefaults, actions, options);
    };
  }];


  var module = angular.module('org.bonita.common.resources', ['ngResource'])
    .constant('API_PATH', API_PATH)

  /**
   * @ngdoc method
   * @name Resources#search
   * @methodOf bonita.common.resources.Resources
   * @description
   * the Resources service decorate the $resource to add a new search
   * function parsing the http header response to find the number of results
   * for the given resource search
   */
    .config(['$provide', '$httpProvider', function ($provide, $httpProvider) {
      $httpProvider.interceptors.push('unauthorizedResponseHandler');
      $provide.decorator('$resource', resourceDecorator);
    }])

    .factory('unauthorizedResponseHandler', ['$q', '$window', function ($q, $window) {
      return {
        'responseError': function (rejection) {
          if (rejection.status === 401) {
            $window.top.location.reload();
          }
          return $q.reject(rejection);
        }
      };
    }]);


  /**
   * @ngdoc service
   * @name bonita.common.resources:User
   * @requires $resource
   * @description
   *
   * var user = User.get({ id: 1 });
   *
   * User is then empty but can be use in a scope.
   * It will be filled with its actual values once the http request is back.
   * We still can use the associated promise to use the data as soon as it gets back.
   *
   * user.$promise.then(function (user) {
             *  console.log(user);
             * });
   *
   **/
  (function (resources) {
    angular.forEach(resources, function (path, name) {
      module.factory(name, ['$resource', function ($resource) {
        return $resource(API_PATH + path + '/:id', { id: '@id' });
      }]);
    });
  })({
    'actorAPI': 'bpm/actor',
    'actorMemberAPI': 'bpm/actorMember',
    'archivedCaseAPI': 'bpm/archivedCase',
    'caseAPI': 'bpm/case',
    'categoryAPI': 'bpm/category',
    'customPageAPI' : 'portal/page',
    'flowNodeAPI':'bpm/flowNode',
    'formMappingAPI' : 'form/mapping',
    'groupAPI': 'identity/group',
    'humanTaskAPI': 'bpm/humanTask',
    'i18nAPI': 'system/i18ntranslation',
    'membershipAPI': 'identity/membership',
    'personalDataAPI': 'identity/personalcontactdata',
    'processAPI': 'bpm/process',
    'processConnectorAPI' : 'bpm/processConnector',
    'processResolutionProblemAPI' : 'bpm/processResolutionProblem',
    'processSupervisorAPI': 'bpm/processSupervisor',
    'processParameterAPI': 'bpm/processParameter',
    'professionalDataAPI': 'identity/professionalcontactdata',
    'profileAPI': 'portal/profile',
    'roleAPI': 'identity/role',
    'archivedTaskAPI': 'bpm/archivedHumanTask',
    'userAPI': 'identity/user'

  });
})();


