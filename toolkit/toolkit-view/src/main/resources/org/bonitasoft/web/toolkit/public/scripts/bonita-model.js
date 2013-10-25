(function (bonitasoft, utils) {
    var http = (function(http) {

        function get(url, callback) {
            var request = new XMLHttpRequest();
            request.onreadystatechange = function() {
                if (request.readyState == 4) {
                    callback(request.status, request.responseText);
                }
            };
            request.open("GET", url, true);
            request.send();
        }

        return http || {
            get: get
        };
    })(http);

    function Search(url) {
        return function(parameters, callback) {
            var target = url;
            target += parameters.page ? '?p=' + parameters.page : '?p=0';
            target += parameters.count ? '&c=' + parameters.count : '&c=100';
            target += parameters.order ? '&o=' + parameters.order : '';
            target += parameters.filter ? '&f=' + parameters.filter : '';
            http.get(target, callback);
        };
    }

    function Fetch(url) {
        return function(id, callback) {
            http.get(url + '/' + id, callback);
        };
    }

    utils.namespace.extend(bonitasoft, 'model', function (model) {
        /*
         * Namespace allowing access to processes REST API 
         */
        model.processes = model.processes || (function () {
            var url = '../API/bpm/process';
            return {
                fetch: new Fetch(url),
                search: new Search(url)
            };
        })();
        
        /*
         * Namespace allowing access to cases REST API 
         */
        model.cases = model.cases || (function () {
            var url = '../API/bpm/case';
            return {
                fetch: new Fetch(url),
                search: new Search(url)
            };
        })();
    });
}) (bonitasoft, bonitasoft.utils);