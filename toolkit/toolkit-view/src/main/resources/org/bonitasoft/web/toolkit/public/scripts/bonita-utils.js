var bonitasoft = (function (bonitasoft) {
    "use strict";
    
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
            
    var variable = (function(variable, assertion) {
        /*
         * Replace following pattern %variable-name%
         * with value in element's html.
         */
        function inject(context, name, value) {
            assertion.assert(context !== undefined);
            context.html(context.html().replace('%' + name + '%', value));
        }
        return variable || {
            inject: inject
        };
    })(variable, assertion);

    var arrays = (function(arrays) {
        /*
         * Iterate across each elements
         * of an arrays to call apply
         * method on this element.
         */
        function foreach(items, apply) {
            for(var i = 0; i < items.length; i += 1) {
                apply(items[i]);
            }
        }
        return arrays || {
            foreach: foreach
        };
    })(arrays);

    namespace.extend(bonitasoft, 'utils', function (utils) {
        // bonitasoft.utils.namespace
        utils.namespace = namespace;
        // bonitasoft.utils.assertion
        utils.assertion = assertion;
        // bonitasoft.utils.arrays
        utils.arrays = arrays;
    });

    // bonitasoft.utils.html.variable
    namespace.extend(bonitasoft, 'utils.html', function (html) {
        html.variable = variable;
    });

    return bonitasoft;
})(bonitasoft || {});
