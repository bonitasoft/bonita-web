# Bonita Console #

## Compilation ##
At root level (same location as the parent pom.xml) :
    
    mvn clean install
    
For develoment purpose use the *dev* profile (don't compile every gwt permutations)
    
    mvn clean install -Pdev
    
## Execution in hosted mode for dev/debug ##
In server module :

    mvn jetty:run -Pdev
    
In portal module :
     
    Super dev mode: mvn -Pdev process-classes gwt:run-codeserver
    Dev mode: mvn -Pdev gwt:debug
    
## Structure ##

### Parent pom.xml ###
Contains the common maven configuration such as:
- the definition of all the dependencies version, e.g., junit.version, bonita.engine.version, gwt.version, ...
- the maven repositories

### common module ###
Contains the back-end business logic, i.e., the code executed on the server side. But also contains shared code between back end and front (e.g. model) and the implement of the REST API. 

### common-test-toolkit
Contains integration tests utils

### portal module
Module containing portal implementation
Contains the source code of the client, i.e., the code cross-compiled to javascript and all the resources; 
Build the portal web application

### server module
Contains the server side code of portal
	
### Form module ###
Module containing forms implementation

#### forms-design
Contains customizable theme shared between form's web application & studio.

#### forms-model
Contains the source code of the model definition.
	
#### forms-rpc
Contains the source code of the client-server communication.

#### forms-server
Contains the back-end business logic, i.e., the code executed on the server side.

#### forms-view
Contains the source code of the client, i.e., the code cross-compiled to javascript and all the resources; 