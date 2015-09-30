// A reference configuration file.
'use strict';
exports.config = {
    //seleniumServerJar: './node_modules/protractor/selenium/selenium-server-standalone-2.42.0.jar',
    //seleniumPort: null,
    chromeDriver: './node_modules/protractor/selenium/chromedriver',
    //seleniumArgs: [],
    directConnect: false,

    specs: [
        'src/test/js/e2e/**/*.e2e.js'
    ],

    capabilities: {
        'browserName': 'firefox'
    },

    baseUrl: 'http://localhost:8083',

    rootElement: 'body',

    mocks: {
        dir: './',
        default: []
    },

    onPrepare: function () {
        require('protractor-http-mock').config = {
            rootDirectory: __dirname,
            protractorConfig: 'protractor.conf.js'
        };
    }
};
