/* global bonitasoft:false */

(function (bonitasoft, assertion, namespace, http) {
    "use strict";

    var createSearch = function (url) {
        return function (parameters, callback) {
            var target = url;
            target += parameters.page ? '?p=' + parameters.page : '?p=0';
            target += parameters.count ? '&c=' + parameters.count : '&c=100';
            target += parameters.order ? '&o=' + parameters.order : '';
            target += parameters.filter ? '&f=' + parameters.filter : '';
            http.get(target, callback);
        };
    };

    var createGet = function (url) {
        return function (id, callback) {
            http.get(url + '/' + id, callback);
        };
    };

    var api = {
        create: function (configuration) {
            assertion.isDefined(configuration.url);

            var url = configuration.url,
                api = {};

            if (configuration.hasGet === true) {
                api.get = configuration.get || createGet(url);
            }

            if (configuration.hasSearch === true) {
                api.search = configuration.search || createSearch(url);
            }

            return api;
        },
        callback: function (callback) {
            return function(code, response) {
                if(code == 200) {
                    callback.onSuccess && callback.onSuccess(JSON.parse(response));
                } else {
                    callback.onFail && callback.onFail(code, response);
                }
            }
        }

    };
    
    namespace.extend(bonitasoft, 'model', function (model) {
        model.callback = api.callback;
        /*
         * Namespace allowing access to processes REST API
         */
        model.processes = model.processes || (function () {
            return api.create({
                url: '../API/bpm/process',

                hasGet: true,
                hasSearch: true
            });
        })();

        /*
         * Namespace allowing access to cases REST API
         */
        model.cases = model.cases || (function () {
            return api.create({
                url: '../API/bpm/case',

                hasGet: true,
                hasSearch: true
            });
        })();
        
        /*
         * Namespace allowing access to archived cases REST API
         */
        model.cases.archives = model.cases.archives || (function () {
            return api.create({
                url: '../API/bpm/archivedCase',

                hasGet: true,
                hasSearch: true
            });
        })();
    });
})(bonitasoft, bonitasoft.utils.assertion, bonitasoft.utils.namespace, bonitasoft.utils.http);