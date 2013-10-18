var bonitasoft = (function (bonitasoft) {
    "use strict";
    
    // namespace utilitaries.
    function Namespace() {
        
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
        
        return {
            extend: extend
        };
    }
    
    // variables utilitaries.
    function Variable() {

	/*
	 * Replace variables contained in html.
	 * Variable are added with the following
	 * pattern %variable-name%. This methode
	 * replace them with value.
	 */
        function inject(context, name, value) {
            context.html(context.html().replace('%' + name + '%', value));
        }
        
        return {
            inject: inject
        };
    }
    
    var namespace = new Namespace();
    namespace.extend(bonitasoft, 'utils', function (utils) {
        utils.namespace = namespace;
    });

    namespace.extend(bonitasoft, 'utils.html', function (html) {
        html.variable = new Variable();
    });
    return bonitasoft;
})(bonitasoft || {});
