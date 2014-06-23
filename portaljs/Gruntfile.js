// Generated on 2014-06-10 using generator-angular 0.8.0
'use strict';

// # Globbing
// for performance reasons we're only matching one level down:
// 'test/spec/{,*/}*.js'
// use this if you want to recursively match all subfolders:
// 'test/spec/**/*.js'

module.exports = function (grunt) {

	// Load grunt tasks automatically
	require('load-grunt-tasks')(grunt);

	// Time how long tasks take. Can help when optimizing build times
	require('time-grunt')(grunt);
	grunt.loadNpmTasks('grunt-connect-proxy');
  grunt.loadNpmTasks('grunt-connect-rewrite');
	grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-ngdocs');

	// Define the configuration for all the tasks
	grunt.initConfig({

		// Project settings
		portaljs: {
			// configurable paths
			app: 'main',
			dist: 'dist'
		},

		// Watches files for changes and runs tasks based on the changed files
		watch: {
			bower: {
				files: ['bower.json'],
				tasks: ['bowerInstall']
			},
			js: {
				files: ['<%= portaljs.app %>/features/**/*.js', '<%= portaljs.app %>/commons/**/*.js', '<%= portaljs.app %>/assets/**/*.js', '<%= portaljs.app %>/app.js'],
				tasks: ['newer:jshint:all'],
				options: {
					livereload: true
				}
			},
			jsTest: {
				files: ['test/spec/**/*.js'],
				tasks: ['newer:jshint:test', 'karma']
			},
			styles: {
				files: ['<%= portaljs.app %>/styles/{,*/}*.css'],
				tasks: ['newer:copy:styles', 'autoprefixer']
			},
			gruntfile: {
				files: ['Gruntfile.js']
			},
			livereload: {
				options: {
					livereload: '<%= connect.options.livereload %>'
				},
				files: [
					'<%= portaljs.app %>/**/*.html',
					'.tmp/styles/{,*/}*.css'
				]
			}
		},

		// The actual grunt server settings
		connect: {
			options: {
				port: 9000,
				// Change this to '0.0.0.0' to access the server from outside.
				hostname: 'localhost',
				livereload: 35729
			},
			server : {
				proxies: [
					{
            context: '/bonita/API',
						host: 'localhost',
						port: 8080,
						https: false,
						changeOrigin: false,
						xforward: false
          }
				]
			},
            rules: [
                // Contextualize app
                { from: '^/bonita/portaljs(.*)$', to: '/$1' },
                { from: '^(?!/bonita/portaljs)(.*)$', to: '/bonita/portaljs$1', redirect: 'permanent' }
            ],
			livereload: {
				options: {
					open: true,
					base: [
						'.tmp',
						'<%= portaljs.app %>'
					],
					middleware: function (connect, options) {
						if (!Array.isArray(options.base)) {
							options.base = [options.base];
						}

						// Setup the proxy
                        var middlewares = [
                            require('grunt-connect-proxy/lib/utils').proxyRequest,
                            require('grunt-connect-rewrite/lib/utils').rewriteRequest];

						// Serve static files.
						options.base.forEach(function(base) {
							middlewares.push(connect.static(base));
						});

						// Make directory browse-able.
						var directory = options.directory || options.base[options.base.length - 1];
						middlewares.push(connect.directory(directory));

						return middlewares;
					}
				}
			},
			test: {
				options: {
					port: 9001,
					base: [
						'.tmp',
						'test',
						'<%= portaljs.app %>'
					]
				}
			},
			dist: {
				options: {
					base: '<%= portaljs.dist %>'
				}
			}
		},

		// Make sure code styles are up to par and there are no obvious mistakes
		jshint: {
			options: {
				jshintrc: '.jshintrc',
				reporter: require('jshint-stylish')
			},
			all: [
				'<%= portaljs.app %>/**/*.js'
			],
			test: {
				options: {
					jshintrc: 'test/.jshintrc'
				},
				src: ['test/spec/**/*.js']
			}
		},

		// Empties folders to start fresh
		clean: {
			dist: {
				files: [{
					dot: true,
					src: [
						'.tmp',
						'<%= portaljs.dist %>/*',
						'!<%= portaljs.dist %>/.git*'
					]
				}]
			},
			server: '.tmp'
		},

		// Add vendor prefixed styles
		autoprefixer: {
			options: {
				browsers: ['last 1 version']
			},
			dist: {
				files: [{
					expand: true,
					cwd: '.tmp/styles/',
					src: '{,*/}*.css',
					dest: '.tmp/styles/'
				}]
			}
		},

		// Automatically inject Bower components into the app
		bowerInstall: {
			app: {
				src: ['<%= portaljs.app %>/index.html'],
				ignorePath: '<%= portaljs.app %>/'
			}
		},

		// Renames files for browser caching purposes
		rev: {
			dist: {
				files: {
					src: [
						'<%= portaljs.dist %>/**/*.js',
						'<%= portaljs.dist %>/styles/{,*/}*.css'
					]
				}
			}
		},

		// Reads HTML for usemin blocks to enable smart builds that automatically
		// concat, minify and revision files. Creates configurations in memory so
		// additional tasks can operate on them
		useminPrepare: {
			html: '<%= portaljs.app %>/index.html',
			options: {
				dest: '<%= portaljs.dist %>',
				flow: {
					html: {
						steps: {
							js: ['concat', 'uglifyjs'],
							css: ['cssmin']
						},
						post: {}
					}
				}
			}
		},

		// Performs rewrites based on rev and the useminPrepare configuration
		usemin: {
			html: ['<%= portaljs.dist %>/**/*.html'],
			css: ['<%= portaljs.dist %>/styles/{,*/}*.css'],
			options: {
				assetsDirs: ['<%= portaljs.dist %>']
			}
		},

		// The following *-min tasks produce minified files in the dist folder
		cssmin: {
			options: {
				root: '<%= portaljs.app %>'
			}
		},

		htmlmin: {
			dist: {
				options: {
					collapseWhitespace: true,
					collapseBooleanAttributes: true,
					removeCommentsFromCDATA: true,
					removeOptionalTags: true
				},
				files: [{
					expand: true,
					cwd: '<%= portaljs.dist %>',
					src: ['*.html', '**/*.html'],
					dest: '<%= portaljs.dist %>'
				}]
			}
		},

		// ngmin tries to make the code safe for minification automatically by
		// using the Angular long form for dependency injection. It doesn't work on
		// things like resolve or inject so those have to be done manually.
		ngmin: {
			dist: {
				files: [{
					expand: true,
					cwd: '.tmp/concat/scripts',
					src: '*.js',
					dest: '.tmp/concat/scripts'
				}]
			}
		},

		// Replace Google CDN references
		cdnify: {
			dist: {
				html: ['<%= portaljs.dist %>/*.html']
			}
		},

		// Copies remaining files to places other tasks can use
		copy: {
			dist: {
				files: [{
					expand: true,
					dot: true,
					cwd: '<%= portaljs.app %>',
					dest: '<%= portaljs.dist %>',
					src: [
						'**/*.html'
					]
				}]
      },
			styles: {
				expand: true,
				cwd: '<%= portaljs.app %>/styles',
				dest: '.tmp/styles/',
				src: '{,*/}*.css'
			}
		},

		// Run some tasks in parallel to speed up the build process
		concurrent: {
			server: [
				'copy:styles'
			],
			test: [
				'copy:styles'
			],
			dist: [
				'copy:styles'
			]
		},

		// By default, your `index.html`'s <!-- Usemin block --> will take care of
		// minification. These next options are pre-configured if you do not wish
		// to use the Usemin blocks.
		// cssmin: {
		//   dist: {
		//     files: {
		//       '<%= portaljs.dist %>/styles/main.css': [
		//         '.tmp/styles/{,*/}*.css',
		//         '<%= portaljs.app %>/styles/{,*/}*.css'
		//       ]
		//     }
		//   }
		// // },
		// uglify: {
		//   dist: {
		//     files: {
		//       '<%= portaljs.dist %>/scripts/bonita-portal-2.0.js': [
		//         '<%= portaljs.dist %>/scripts/*.js'
		//       ]
		//     }
		//   },
		//   options: {
		//   	sourceMap: true,
  //       sourceMapName: '<%= portaljs.dist %>/scripts/bonita-portal-2.0.map'
		//   }
		// },
		// concat: {
		//   dist: {}
		// },

		// Test settings
		karma: {
			unit: {
				configFile: 'karma.conf.js',
				singleRun: true
			}
		},
    ngdocs: {
      all: ['<%= portaljs.app %>/features/**/*.js', '<%= portaljs.app %>/commons/**/*.js', '<%= portaljs.app %>/app.js']
    }
	});


	grunt.registerTask('serve', function (target) {
		if (target === 'dist') {
			return grunt.task.run(['build', 'connect:dist:keepalive']);
		}

		grunt.task.run([
			'clean:server',
			'bowerInstall',
			'concurrent:server',
            'configureRewriteRules',
			'configureProxies:server',
			'autoprefixer',
			'connect:livereload',
			'watch'
		]);
	});

	grunt.registerTask('server', function (target) {
		grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
		grunt.task.run(['serve:' + target]);
	});

	grunt.registerTask('test', [
		'clean:server',
		'concurrent:test',
		'autoprefixer',
		'connect:test',
		'karma'
	]);

	grunt.registerTask('build', [
		'clean:dist',
		'bowerInstall',
		'useminPrepare',
		'concurrent:dist',
		'autoprefixer',
		'concat',
		'ngmin',
		'copy:dist',
		'cdnify',
		'cssmin',
		'uglify',
		'rev',
		'usemin',
		'htmlmin'
	]);

	grunt.registerTask('uglication', [
		'clean:dist',
		'bowerInstall',
		'useminPrepare',
		'concurrent:dist',
		'autoprefixer',
		'concat',
		'copy:dist',
		'uglify'
	]);

	grunt.registerTask('default', [
		//'newer:jshint',
		'test',
		'build'
	]);
};
