var gulp = require("gulp");
var protractor = require("gulp-protractor").protractor;

gulp.task("e2e", function () {
    gulp.src(["./src/test/js/e2e/**/*.e2e.js"])
    .pipe(protractor({
            configFile: "protractor.conf.js"
        }))
        .on('error', function (e) {
            console.log(e);
            throw e;
        });
});

