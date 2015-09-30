'use strict';

describe('Custom widget living application menu', function () {

    var $httpBackend, $compile, $rootScope, $scope, $window;

    beforeEach(module('bonitasoft.ui.widgets'));

    beforeEach(function () {

        module(function ($provide) {
            $window = {};
            $window.document = {body: {style: {paddingTop: ''}}};
            $provide.value('$window', $window);
        });

        inject(function ($injector, _$httpBackend_, _$compile_, _$rootScope_) {
            // Set up the mock http service responses
            $httpBackend = _$httpBackend_;
            $compile = _$compile_;
            $rootScope = _$rootScope_;
        });
        $scope = $rootScope.$new();
        $scope.properties = {};

        $httpBackend
            .expectGET('../API/living/application/?c=1&f=token%3DmyApp')
            .respond([{
                'themeId': '3',
                'state': 'ACTIVATED',
                'layoutId': '16',
                'homePageId': '1',
                'version': '1.0',
                'lastUpdateDate': '1442305112306',
                'updatedBy': '4',
                'id': '1',
                'creationDate': '1442234827102',
                'iconPath': '',
                'createdBy': '4',
                'token': 'myapp',
                'description': '',
                'profileId': '2',
                'displayName': 'My application'
            }]);

        $httpBackend
            .expectGET('../API/living/application-menu/?c=100&f=applicationId%3D1&d=applicationPageId&o=menuIndex+ASC')
            .respond([{
                'id': '1',
                'parentMenuId': '-1',
                'applicationPageId': '-1',
                'applicationId': '1',
                'menuIndex': '1',
                'displayName': 'list'
            }, {
                'id': '2',
                'parentMenuId': '1',
                'applicationPageId': {'id': '2', 'token': 'h', 'pageId': '12', 'applicationId': '1'},
                'applicationId': '1',
                'menuIndex': '1',
                'displayName': 'html example'
            }, {
                'id': '3',
                'parentMenuId': '1',
                'applicationPageId': {'id': '3', 'token': 'g', 'pageId': '13', 'applicationId': '1'},
                'applicationId': '1',
                'menuIndex': '2',
                'displayName': 'groovy example'
            }, {
                'id': '4',
                'parentMenuId': '-1',
                'applicationPageId': {'id': '2', 'token': 'h', 'pageId': '12', 'applicationId': '1'},
                'applicationId': '1',
                'menuIndex': '2',
                'displayName': 'home'
            }, {
                'id': '5',
                'parentMenuId': '-1',
                'applicationPageId': {'id': '3', 'token': 'g', 'pageId': '13', 'applicationId': '1'},
                'applicationId': '1',
                'menuIndex': '3',
                'displayName': 'groovy example'
            }]);


    });

    afterEach(function () {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });

    it('should get menu related to the parsed url', function () {

        $window.location = {'pathname': 'myApp/home/', 'search': '?anyparam=value'};
        $compile('<custom-living-application-menu></custom-living-application-menu>')($scope);
        $scope.properties.sticky = true;

        $httpBackend.flush();

        expect($scope.properties.targetUrl).toEqual('../../../portal/resource/app/myApp/home/content/?anyparam=value');
        expect($window.document.body.style.paddingTop).toEqual("70px");
        expect($window.document.title).toEqual('My application');


    });

    it('should identify Parent menu', function () {

        $window.location = {'pathname': 'myApp/home/', 'search': '?anyparam=value'};
        $compile('<custom-living-application-menu></custom-living-application-menu>')($scope);
        $httpBackend.flush();

        expect($scope.ctrl.isParentMenu({parentMenuId:-1, applicationPageId:-1})).toEqual(true);
        expect($scope.ctrl.isParentMenu({parentMenuId:-1, applicationPageId:3})).toEqual(false);
        expect($scope.ctrl.isParentMenu({parentMenuId:2, applicationPageId:-1})).toEqual(false);
        expect($scope.ctrl.isParentMenu({parentMenuId:2, applicationPageId:3})).toEqual(false);

    });
});
