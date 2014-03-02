/* global bonitasoft:false */

(function (bonitasoft, assertion, namespace, http) {
    "use strict";

    /*
     * Get object definition.
     */
    var Get = function (url) {
        if (this instanceof Get) {
            this.url = url;
            return this.init();
        }
        return new Get(url);
    };
    Get.prototype.init = function () {
        var self = this;
        return function (id, callback) {
            http.get(self.url + '/' + id, callback);
        };
    };
    
    /*
     * Search object definition.
     */
    var Search = function (url) {
        if (this instanceof Search) {
            this.url = url;
            return this.init();
        }
        return new Search(url);
    };
    Search.prototype.init = function () {
        var self = this;
        return function (parameters, callback) {
            var target = self.url;
            target += parameters.page ? '?p=' + parameters.page : '?p=0';
            target += parameters.count ? '&c=' + parameters.count : '&c=100';
            target += parameters.order ? '&o=' + parameters.order : '';
            if(parameters.filter instanceof Array) {
                parameters.filter.forEach(function(filter) {
                    target += filter ? '&f=' + filter : '';
                });
            } else {
                target += parameters.filter ? '&f=' + parameters.filter : '';
            }
            http.get(target, callback);
        };
    };

    /*
     * APIs factory.
     */
    var api = {
        create: function (configuration) {
            assertion.isDefined(configuration.url);

            var url = configuration.url,
                api = {};

            if (configuration.hasGet === true) {
                api.get = configuration.get || new Get(url);
            }

            if (configuration.hasSearch === true) {
                api.search = configuration.search || new Search(url);
            }

            return api;
        }
    };

    /*
     * Model description.
     */
    namespace.extend(bonitasoft, 'model', function (model) {
        /*
         * Namespace allowing access to processes REST API
         */
        model.processes = (function () {
            return api.create({
                url: '../API/bpm/process',
                hasGet: true,
                hasSearch: true
            });
        })();

        /*
         * Namespace allowing access to cases REST API
         */
        model.cases = (function () {
            return api.create({
                url: '../API/bpm/case',
                hasGet: true,
                hasSearch: true
            });
        })();

        /*
         * Namespace allowing access to archived cases REST API
         */
        model.cases.archives = (function () {

            return api.create({
                url: '../API/bpm/archivedCase',
                hasGet: true,
                hasSearch: true
            });
        })();
    });

    /*
     * Case object description.
     */
    var Case = function (item) {

        if (this instanceof Case) {
            this.item = item;
            return this;
        }
        return new Case(item);
    };
    Case.prototype = {
        id:function() {
            return this.item.sourceObjectId || this.item.id;
        },
        rootCaseId: function() {
            return this.item.rootCaseId;
        }
    };

    /*
     * Custom callbacks description.
     */
    var wrap = function (items, constructor, callback) {

        if (items instanceof Array) {
            var wraps = [];
            items.forEach(function (item) {
                wraps.push(constructor(item));
            });
            callback(wraps);
        } else {
            callback(items);
        }
    };

    namespace.extend(bonitasoft, 'model', function (model) {
        
        model.callback = function (callback) {
            return function (code, response) {

                if (code == 200 && callback.onSuccess !== undefined) {
                    callback.onSuccess(JSON.parse(response));

                } else if (callback.onFail !== undefined) {
                    callback.onFail(code, response);
                }
            };
        };
        
        model.cases.callback = function (callback) {
            return model.callback({
                onSuccess: function (items) {
                    wrap(items, Case, callback.onSuccess);
                },
                onFail: callback.onFail
            });
        };
    });

})(bonitasoft,
    bonitasoft.utils.assertion,
    bonitasoft.utils.namespace,
    bonitasoft.utils.http);