(function () {
    'use strict';

    angular.module('org.bonitasoft.common.filters.stringTemplater', []).filter('stringTemplater', function () {
        return function (template, replacement) {
            var templteRE = /\{\}/;
            if (!template || !replacement || typeof template !== 'string') {
                return template;
            }
            if (replacement instanceof Array) {
                return replacement.reduce(function (newTemplate, currentReplacement) {
                    return newTemplate.replace(templteRE, currentReplacement);
                }, template).replace(/\{\}/g, '');
            } else {
                return template.replace(templteRE, replacement).replace(/\{\}/g, '');
            }
        };
    });
})();