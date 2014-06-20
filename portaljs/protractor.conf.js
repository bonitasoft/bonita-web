// A reference configuration file.
exports.config = {
    seleniumServerJar: './node_modules/protractor/selenium/selenium-server-standalone-2.42.0.jar',
        seleniumPort: null,
    chromeDriver: './node_modules/protractor/selenium/chromedriver',
    seleniumArgs: [],

        specs: [
        'test/e2e/**/*.spec.js'
    ],

    capabilities: {
        'browserName': 'chrome'
    },

    baseUrl: 'http://localhost:9000/',

    rootElement: 'body',

    onPrepare: function() {
    },

    jasmineNodeOpts: {
        // onComplete will be called just before the driver quits.
        onComplete: null,
        // If true, display spec names.
        isVerbose: false,
        // If true, print colors to the terminal.
        showColors: true,
        // If true, include stack traces in failures.
        includeStackTrace: true,
        // Default time to wait in ms before a test fails.
        defaultTimeoutInterval: 10000
    }
};
