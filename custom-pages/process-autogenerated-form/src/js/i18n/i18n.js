(function () {
    'use strict';

    angular.module('org.bonitasoft.services.i18n', [
        'ngCookies',
        'gettext',
        'org.bonita.common.resources'
    ])
        .service('i18nService', ['gettextCatalog', '$cookies', 'i18nAPI', function (gettextCatalog, $cookies, i18nAPI) {
            function arrayToObject(array) {
                var object = {};
                for (var i = 0; i < array.length; i++) {
                    object[array[i].key] = array[i].value;
                }
                return object;
            }

            function updateCatalog(catalog) {
                gettextCatalog.currentLanguage = $cookies['BOS_Locale'];
                gettextCatalog.baseLanguage = null;
                gettextCatalog.setStrings($cookies['BOS_Locale'], arrayToObject(catalog));
            }

            //
            gettextCatalog.debug = false;
            return (function loadTranslations() {
                return i18nAPI.query({
                    f: 'locale=' + ($cookies['BOS_Locale'] || 'en')
                }).$promise.then(updateCatalog, function(reason){
                        console.log('Unable to load translations!', reason);
                    });
            })();
        }]);
})();