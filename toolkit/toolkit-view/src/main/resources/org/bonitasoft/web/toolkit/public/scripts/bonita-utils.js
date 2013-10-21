var bonitasoft = (function (bonitasoft) {
    "use strict";
    
    var namespace = (function(namespace) {
        /*
         * Extend namespace by creating all 
         * intermediate namespace and calling 
         * extension method with requested 
         * namespace to be extended
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
         * Throw an excepton if the condition is false.
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
         * Replace variables contained in html.
         * Variable are added with the following
         * pattern %variable-name%. This methode
         * replace them with value.
         */
        function inject(context, name, value) {
            assertion.assert(context !== undefined);
            context.html(context.html().replace('%' + name + '%', value));
        }
        return variable || {
            inject: inject
        };
    })(variable, assertion);

    // bonitasoft.utils.namespace    
    namespace.extend(bonitasoft, 'utils', function (utils) {
        utils.namespace = namespace;
    });
    
    // bonitasoft.utils.assertion
    namespace.extend(bonitasoft, 'utils', function (utils) {
        utils.assertion = assertion;
    });

    // bonitasoft.utils.html.variable
    namespace.extend(bonitasoft, 'utils.html', function (html) {
        html.variable = variable;
    });
        
    return bonitasoft;
})(bonitasoft || {});
