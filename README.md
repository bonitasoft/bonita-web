# Bonita Web #


## Requirements

>     Java JDK 11

This project bundles the [Maven Wrapper](https://github.com/takari/maven-wrapper), so the `mvnw` script is available at the project root.

### Dependencies

The project depends on bonita-engine artifacts so if you want to build a branch in a SNAPSHOT version, you must build the [bonita-engine](https://github.com/bonitasoft/bonita-engine) first (install artifacts in your local repository).

If you build a tag, you don't need to build the bonita-engine as its artifacts are available on Maven Central.

## Contribution

I you want to contribute, ask questions about the project, report bug, see the [contributing guide](https://github.com/bonitasoft/bonita-developer-resources/blob/master/CONTRIBUTING.MD).


## Build the project ##

At root level (same location as the parent pom.xml) :
    
    ./mvnw clean install

## Execution in hosted mode
In server module, to build and launch a tomcat hosting the app :

    ./mvnw clean verify org.codehaus.cargo:cargo-maven2-plugin:run -DskipTests

H2 database is created (if it does not already exist) in ${user.home}/bonita/community/database
When you checkout a different branch you need to clean this directory because the database schema may have changed.

Hot reload is not supported, but when you update a class in portal/, server/ or common/ in your IDE all you need to do is to restart the tomcat with the previous command (classes will be retrieved from the projects target/classes directory)
    
## Structure ##

### Parent pom.xml ###
Contains the common maven configuration such as:
- the definition of all the dependencies version, e.g., junit.version, bonita.engine.version, gwt.version, ...
- the maven repositories

### common module ###
Contains the back-end business logic, i.e., the code executed on the server side. But also contains shared code between back end and front (e.g. model) and the implement of the REST API. 

### test-toolkit
Contains integration tests utils

### server module
Contains the server side code of portal
