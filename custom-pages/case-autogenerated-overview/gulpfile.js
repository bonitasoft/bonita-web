/* jshint node:true */

'use strict';

var gulp = require('gulp');
var path = require('path');
var properties = require('properties');
var fs = require('fs');
var plumber = require('gulp-plumber');

/* server */
var connect = require('gulp-connect');
var rewriter = require('connect-modrewrite');
var launch = require('gulp-open');

/* build */
var usemin = require('gulp-usemin');
var htmlmin = require('gulp-htmlmin');
var rev = require('gulp-rev');
var clean = require('gulp-clean');
var html2js = require('gulp-ng-html2js');
var concat = require('gulp-concat');
var zip = require('gulp-zip');
var runSequence = require('run-sequence');

/* javascript */
var uglify = require('gulp-uglify');
var jshint = require('gulp-jshint');

/* test */
var gulp = require('gulp');
var karma = require('karma').server;
var protractor = require('gulp-protractor').protractor;

/**
 * Configuration
 */
var pageContent = fs.readFileSync('src/page.properties', 'utf8');
var customPageName = properties.parse(pageContent).name;
var folderName = path.basename(__dirname);

var opt = {
  port: 3000,
  livereload: 31357
};

var htmlminOpt = {
  collapseWhitespace: true,
  removeComments: true,
  useShortDoctype: true
};

var useminOpt = {
  css: ['concat', rev()],
  css2: ['concat', rev()],
  html: [htmlmin(htmlminOpt)],
  js: [uglify(), rev()]
};

function serve(configuration) {
  return connect.server({
    root: ['src', '.'],
    port: opt.port,
    livereload: configuration.livereload,
    middleware: function () {
      return [
        rewriter([
          '^/bonita/portal http://localhost:8080/bonita/portal [P]',
          '^/bonita/API http://localhost:8080/bonita/API [P]',
          '^/bonita/' + folderName + '/css/themeResource http://localhost:8080/bonita/portal/themeResource [P]',
          '^/bonita/' + folderName + ' http://localhost:3000 [P]',
          '^/bonita http://localhost:3000 [P]'
        ])
      ];
    }
  });
}

gulp.task('clean', function () {
  return gulp.src('target', {
      read: false
    })
    .pipe(clean({
      force: true
    }));
});

/**
 * template inlining
 */
gulp.task('html2js', function () {
  return gulp.src(['src/**/*.html', '!src/index.html'])
    .pipe(plumber())
    .pipe(html2js({
      useStrict: true
    }))
    .pipe(concat('templates.js'))
    .pipe(gulp.dest('target/work'));
});

/* usemin task */
gulp.task('usemin', ['html2js'], function () {
  return gulp.src('src/index.html')
    .pipe(plumber())
    .pipe(usemin(useminOpt))
    .pipe(gulp.dest('target/dist'));
});

/** temp task to rename resource path after building dist */
var replace = require('gulp-replace');
gulp.task('repath', ['usemin'], function () {
  return gulp.src('target/dist/index.html')
    .pipe(plumber())
    .pipe(replace(/(src=["|']resources\/([^"']*\.js)["|'])/g, 'src="$2"'))
    .pipe(replace(/(href=["|']resources\/([^"']*\.css)["|'])/g, 'href="$2"'))
    .pipe(gulp.dest('target/dist'));
});

gulp.task('assets', function () {
  return gulp.src('src/page.properties')
    .pipe(gulp.dest('target/dist'));
});

gulp.task('fonts', function () {
  return gulp.src('src/fonts/*')
    .pipe(gulp.dest('target/dist/resources/fonts'));
});


/**
 * JsHint
 * Validate js script
 */
gulp.task('jshint', function () {
  return gulp.src(['src/**/*.js', '!src/**/dev-only.js'])
    .pipe(plumber())
    .pipe(jshint())
    .pipe(jshint.reporter('jshint-stylish'));
});

/**
 * Server task
 */
gulp.task('server', ['html2js'], function () {
  serve({
    livereload: true
  });
});

/**
 * Watch task
 * Launch a server with livereload
 */
gulp.task('watch', ['server'], function () {
  gulp
    .watch(['src/**/*.*'])
    .on('change', function () {
      gulp.src('').pipe(connect.reload());
    });

  gulp.watch(['src/**/*.js', 'test/**/*.js'], ['jshint']);

  gulp
    .watch(['src/index.html'])
    .on('change', function () {
      gulp.src('').pipe(connect.reload());
    });
});

/**
 * Open task
 * Launch default browser on local server url
 */
gulp.task('open', ['server'], function () {
  return gulp.src('src/index.html')
    .pipe(launch('', {
      url: 'http://localhost:' + opt.port + '/bonita/' + folderName + '/index.html?id=2'
    }));
});

/**
 * tdd testing
 * Watch for file changes and re-run tests on each change
 */
gulp.task('tdd', function (done) {
  karma.start({
    configFile: __dirname + '/karma.conf.js'
  }, done);
});

gulp.task('zip', ['assets', 'fonts', 'repath'], function (done) {
  return gulp.src('target/dist/**/*')
    .pipe(zip(customPageName + '.zip'))
    .pipe(gulp.dest('target'));
});

gulp.task('e2e', function (done) {
  serve({
    livereload: false
  });
  gulp.src(["test/e2e/spec/**/*.js"])
    .pipe(protractor({
      configFile: "test/e2e/protractor.conf.js",
      args: ['--baseUrl', 'http://localhost:' + opt.port]
    }))
    .on('error', function (e) {
      throw e
    })
    .on('end', function () {
      connect.serverClose();
      done();
    });
});

gulp.task('default', function (done) {
  runSequence(['jshint', 'clean'], 'zip', done);
});
gulp.task('dev', ['server', 'watch', 'open', 'tdd']);
