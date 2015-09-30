/** Copyright (C) 2015 Bonitasoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


(function () {
    'use strict';

    describe('Layout page', function () {

        var mock = require('protractor-http-mock');
        var width = 1280, height = 800;

        beforeEach(function () {
            mock([{
                request: {
                    path: '../API/living/application/',
                    method: 'GET',
                    queryString: {
                        c: '1',
                        f: 'token=page'
                    }
                },
                response: {
                    data: [{
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
                    }]
                }
            }]);
        });

        afterEach(function () {
            mock.teardown();
        });

        describe('preview', function () {
            it('should display application name on the browser tab', function () {
                browser.controlFlow().execute(deployLayoutPage).then(function() {
                        browser.driver.sleep(1000);
                        browser.driver.manage().window().setSize(width, height);
                        browser.get('/designer/preview/page/layout-page/');
                        console.log('####GET BROWSER');
                        expect(browser.getTitle()).toEqual('My application');
                        var brandTarget = element(by.css('.navbar-brand')).getAttribute('href');
                        expect(brandTarget).toEqual(browser.baseUrl + '/designer/preview/page/');
                    }
                );
            });
        });
    });

    function deployLayoutPage() {
        var deferred = protractor.promise.defer();
        var pagePath = __dirname + '/../../../../../target/layout-page-7.1.0-SNAPSHOT.zip';
        var restler = require('restler');
        var fs = require('fs');

        fs.stat(pagePath, function (err, stats) {
            restler.post(browser.baseUrl+'/designer/import/page', {
                multipart: true,
                data: {
                    "file": restler.file(pagePath, null, stats.size, null, "application/zip")
                }
            }).on("complete", function () {
                console.log('####FULL Fill');
                deferred.fulfill();
            }).on("error", function (e) {
                deferred.reject(e);
            }).on("fail", function (e) {
                deferred.reject(e);
            });
        });

        return deferred.promise;
    }
})
();
