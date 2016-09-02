/* jshint browser:true, jquery:true*/

var bonitasoft = (function (bonitasoft) {
    "use strict";

    /*
     * Utilities dedicated to expend namespaces.
     */
    var namespace = (function () {
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

        return {
            extend: extend
        };
    })();

    /*
     * Utilities allowing use of asserts.
     */
    var assertion = (function () {
        /*
         * Throw an exception if the
         * condition isn't met.
         */
        function assert(condition, message) {
            if (!condition) {
                throw message || 'Assertion failed';
            }
        }

        function isDefined(variable, message) {
            message = message || 'Some variable must be defined';
            assert(variable !== undefined, message);
        }

        function isArray(variable, message) {
            message = message || 'Some variable must be an array';
            assert(variable instanceof Array, message);
        }

        return {
            assert: assert,
            isDefined: isDefined,
            isArray: isArray
        };
    })();

    /*
     * Utilities to manage variables in html context.
     */
    var variable = (function () {
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
    })();

    /*
     * Utilities dedicated to ease access to http features.
     */
    var http = (function () {

        function get(url, callback) {
            var request = new XMLHttpRequest();
            request.onreadystatechange = function () {
                if (request.readyState == 4) {
                    callback(request.status, request.responseText);
                }
            };
            request.open("GET", url, true);
            var apiToken = getCookie('X-Bonita-API-Token');
            if (apiToken) {
                request.setRequestHeader('X-Bonita-API-Token', apiToken);
            }
            request.send();
        }
        
        function getCookie(cname) {
            var name = cname + "=";
            var ca = document.cookie.split(';');
            for(var i = 0; i < ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0) == ' ') {
                    c = c.substring(1);
                }
                if (c.indexOf(name) == 0) {
                    return c.substring(name.length, c.length);
                }
            }
            return "";
        }

        return {
            get: get,
        };
    })();

    var select = {

        wrap: (function () {

            function append(element, option) {
                if (option) {
                    var optionElement = document.createElement("option");
                    optionElement.innerHTML = option.text;
                    optionElement.value = option.value;
                    element.append(optionElement);
                }
            }

            function getIndex(element, value) {
                return $("option[value=\"" + value + "\"]", element)
                    .prop("index");
            }

            function render(element, items, renderer) {
                assertion.isDefined(Array.prototype.forEach);
                
                items.forEach(function (item) {
                    append(element, renderer(item));
                });
            }

            /*
             * Set select selected index.
             */
            var select = function (value) {
                if (value) {
                    this.getElement().prop("selectedIndex",
                        getIndex(this.getElement(), value));
                }
            };

            /*
             * Clear select content from all options.
             */
            var clear = function () {
                assertion.isDefined(this.getElement());

                this.getElement().html('');
            };

            /*
             * Set items  an item to the select then render the whole select options set.
             * Items are sorted if an order is set.
             *
             * items            - JSON array containing items to append
             * optionRenderer   - function called with the current item in parameter.
             *                  It must return a literal object with val & text of the option.
             *                  e.g { value: '', text: '' }
             */
            var add = function (items, renderer) {
                assertion.isDefined(items);

                renderer = renderer || function (item) {
                    return {
                        text: item.text,
                        value: item.value
                    };
                };

                render(this.getElement(), items, renderer);
            };

            var Select = function (element) {
                this.getElement = function () {
                    return element;
                };
            };

            Select.prototype = {
                clear: clear,
                add: add,
                select: select
            };

            return function (element) {
                return new Select(element);
            };
        })()
    };

    var arrays = (function () {
        var merge = function (a, b, order) {
            assertion.isArray(a, "a must be an array");
            
            if (b instanceof Array) {
                a = a.concat(b);
            }
            if (order !== undefined) {
                a = a.sort(order);
            }
            return a;
        };

        return {
            merge: merge
        };
    })();

    namespace.extend(bonitasoft, 'utils', function (utils) {
        // bonitasoft.utils.namespace
        utils.namespace = namespace;
        // bonitasoft.utils.assertion
        utils.assertion = assertion;
        // bonitasoft.utils.http
        utils.http = http;
        // bonitasoft.utils.arrays
        utils.arrays = arrays;
    });

    // bonitasoft.utils.html.variable
    namespace.extend(bonitasoft, 'utils.html', function (html) {
        html.variable = variable;
    });

    // bonitasoft.utils.form.select
    namespace.extend(bonitasoft, 'utils.form', function (form) {
        form.select = select;
    });

    return bonitasoft;
})(bonitasoft || {});