var bonitasoft = (function (bonitasoft) {
    "use strict";
    
    /*
     * Utilities dedicated to expend namespaces.
     */
    var namespace = (function(namespace) {
        /*
         * Extend namespace by creating all 
         * intermediate namespaces and calling
         * extension method to extends requested 
         * namespace.
         */
        function extend(root, namespaces, extension) {
            var i, 
                current = root, 
                spaces = namespaces.split('.');
            for (i = 0; i < spaces.length; i += 1) {
                var namespace = spaces[i];
                current[namespace] = current[namespace] || {};
                current = current[namespace];
            }
            extension(current);
        }
        
        return namespace || {
            extend: extend
        };
    })(namespace);
            
    /*
     * Utilities allowing use of asserts.
     */
    var assertion = (function(assertion) {
        /*
         * Throw an exception if the
         * condition isn't met.
         */
        function assert(condition, message) {
            if(!condition) {
                throw message || 'Assertion failed';
            }
        }
        return assertion || {
            assert: assert
        };
    })(assertion);

    /*
     * Utilities to manage variables in html context.
     */
    var variable = (function(variable, assertion) {
        /*
         * Replace following pattern %variable-name%
         * with value in element's html.
         */
        function inject(context, name, value) {
            assertion.assert(context !== undefined, "context must be defined");
            
            var delimiter = '%';
            context.html(context.html().replace(
                new RegExp(delimiter + name + delimiter, "g"), 
                value));
        }
        return variable || {
            inject: inject
        };
    })(variable, assertion);

    /*
     * Utilities dedicated to ease access to http features.
     */
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

    namespace.extend(bonitasoft, 'utils', function (utils) {
        // bonitasoft.utils.namespace
        utils.namespace = namespace;
        // bonitasoft.utils.assertion
        utils.assertion = assertion;
        // bonitasoft.utils.http
        utils.http = http;
    });

    // bonitasoft.utils.html.variable
    namespace.extend(bonitasoft, 'utils.html', function (html) {
        html.variable = variable;
    });

    return bonitasoft;
})(bonitasoft || {});
