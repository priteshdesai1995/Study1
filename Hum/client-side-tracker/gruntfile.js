module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    uglify: {
      options: {
        banner: `/*!\nHumaine AI Client Side Tracker\nDate:<%= grunt.template.today("yyyy-mm-dd") %> \n*/\n`
      },
      build: {
        src: 'tracker.js',
        dest: 'tracker.min.js'
      }
    }
  });

  // Load the plugin that provides the "uglify" task.
  grunt.loadNpmTasks('grunt-contrib-uglify');

  // Default task(s).
  grunt.registerTask('default', ['uglify']);
};
