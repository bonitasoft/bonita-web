# Bonita Web #


## Requirements

>     Java JDK 1.8 or higher

This project bundles the [Maven Wrapper](https://github.com/takari/maven-wrapper), so the `mvnw` script is available at the project root.

### Dependencies

The project depends on bonita-engine artifacts so if you want to build a branch in a SNAPSHOT version, you must build the [bonita-engine](https://github.com/bonitasoft/bonita-engine) first (install artifacts in your local repository).

If you build a tag, you don't need to build the bonita-engine as its artifacts are available on Maven Central.

## Contribution

I you want to contribute, ask questions about the project, report bug, see the [contributing guide](https://github.com/bonitasoft/bonita-developer-resources/blob/master/CONTRIBUTING.MD).



## Build the project ##

At root level (same location as the parent pom.xml) :
    
    ./mvnw clean install -Djvm_gwt=<PATH_TO_JAVA_8>/bin/java

This `jvm_gwt` parameter is mandatory for GWT compilation.

For development purpose use the *dev* profile (don't compile every gwt permutations)
    
    ./mvnw clean install -Pdev -Djvm_gwt=<PATH_TO_JAVA_8>/bin/java

## Execution in hosted mode for dev/debug ##
In server module, to build and launch a tomcat hosting the app :

    ./mvnw clean verify org.codehaus.cargo:cargo-maven2-plugin:run -DskipTests -Pdev -Djvm_gwt=<PATH_TO_JAVA_8>/bin/java

H2 database is created (if it does not already exist) in ${user.home}/bonita/community/database
When you checkout a different branch you need to clean this directory because the database schema may have changed.

Hot reload is not supported, but when you update a class in portal/, server/ or common/ in your IDE all you need to do is to restart the tomcat with the previous command (classes will be retrieved from the projects target/classes directory)
    
In portal module :
    
    Super dev mode: ./mvnw -Pdev process-classes gwt:run-codeserver

Visit the indicated URL and bookmark the Dev Mode On/Off links
Then visit http://localhost:8080/bonita, login with install/install and click on the Dev Mode On link
click on the portal module link to re-generate the portal Javascript
    
## Structure ##

### Parent pom.xml ###
Contains the common maven configuration such as:
- the definition of all the dependencies version, e.g., junit.version, bonita.engine.version, gwt.version, ...
- the maven repositories

### common module ###
Contains the back-end business logic, i.e., the code executed on the server side. But also contains shared code between back end and front (e.g. model) and the implement of the REST API. 

### test-toolkit
Contains integration tests utils

### portal module
Module containing portal implementation
Contains the source code of the client, i.e., the code cross-compiled to javascript and all the resources; 
Build the portal web application

### server module
Contains the server side code of portal
	
### Forms module ###
Module containing 6.x forms implementation

#### forms-design
Contains a customizable theme shared between form's web application & studio.

#### forms-model
Contains the source code of the model definition.
	
#### forms-rpc
Contains the source code of the client-server communication.

#### forms-server
Contains the back-end business logic, i.e., the code executed on the server side.

#### forms-view
Contains the source code of the client, i.e., the code cross-compiled to javascript and all the resources; 