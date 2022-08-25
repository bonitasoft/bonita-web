# Server

Bonita server app.  
Enhance server with a REST API

## Launch dev mode
The module need to first be built using

    mvn clean install
    
Then we can build and launch a tomcat to host the app using

    mvn clean verify org.codehaus.cargo:cargo-maven2-plugin:run -DskipTests

H2 database is created (if it does not already exist) in ${user.home}/.bonita/community/database
When you checkout a different branch you need to clean this directory because the database schema may have changed.
    
Hot reload is not supported, but hen you update a class in portal/, server/ or common/ in your IDE all you need to do is to restart the tomcat with the previous command (classes will be retrieved from the projects target/classes directory)

You can debug the server code using the Remote Debug configuration of your ide on the port 5005.