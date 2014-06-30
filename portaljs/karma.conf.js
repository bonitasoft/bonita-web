// Karma configuration
// http://karma-runner.github.io/0.10/config/configuration-file.html

module.exports = function(config) {
  'use strict';
  config.set({
    // base path, that will be used to resolve files and exclude
    basePath: '',

    // testing framework to use (jasmine/mocha/qunit/...)
    frameworks: ['jasmine'],

    // list of files / patterns to load in the browser
    files: [
      'main/assets/jquery/dist/jquery.js',
      'main/assets/angular/angular.js',
      'main/assets/angular-cookies/angular-cookies.js',
      'main/assets/angular-resource/angular-resource.js',
      'main/assets/angular-route/angular-route.js',
      'main/assets/angular-mocks/angular-mocks.js',
      'main/assets/angular-ui-router/release/angular-ui-router.js',
      'main/assets/angular-bootstrap/ui-bootstrap-tpls.js',
      'main/assets/bootstrap/dist/js/bootstrap.js',
      'main/assets/ng-grid/build/ng-grid.js',
      'main/assets/jqueryui/ui/jquery-ui.js',
      'main/assets/angular-gettext/dist/angular-gettext.js',
      'main/app.js',
      'main/i18n.js',
      'main/common/**/*.js',
      'main/features/**/*.js',
      'test/mock/**/*.js',
      'test/spec/**/*.js'
    ],

    // list of files / patterns to exclude
    exclude: [],

    // web server port
    port: 8080,

    // level of logging
    // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: false,


    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: ['PhantomJS'],


    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false,
    reporters: ['progress', 'html'],

    // the default configuration
    htmlReporter: {
      outputDir: 'karmaReports',
      templatePath: __dirname+'/jasmine_template.html'
    }

  });
};
