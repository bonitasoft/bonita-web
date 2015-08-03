// Karma configuration
// Generated on Thu Sep 04 2014 11:47:01 GMT+0200 (Paris, Madrid (heure d’été))
'use strict';

module.exports = function(config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '',


    autoWatchBatchDelay: 500,

    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['jasmine'],


    // list of files / patterns to load in the browser
    files: [
      'node_modules/angular/angular.js',
      'node_modules/angular-mocks/angular-mocks.js',
      'node_modules/angular-bootstrap/ui-bootstrap.js',
      'node_modules/angular-resource/angular-resource.js',
      'node_modules/angular-gettext/dist/angular-gettext.js',
      'node_modules/ngUpload/ng-upload.js',
      'node_modules/angular-cookies/angular-cookies.js',
      'src/**/*.js',
      'test/unit/**/*Spec.js',
      // include fixtures html  in karma webserver, available at /base/dev/fixtures
      { pattern: 'dev/fixtures/**/*.html', included: false, served: true },
    ],


    // list of files to exclude
    exclude: [
      '**/*.min.js'
    ],


    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
    },


    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress'],


    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: ['Chrome'],


    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: false
  });
};
