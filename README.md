# Bonita Console #

## Compilation ##
At root level (same location as the parent pom.xml) :
    
    mvn clean install
    
For develoment purpose use the *dev* profile (don't compile every gwt permutations)
    
    mvn clean install -Pdev
    
## Execution in hosted mode for dev/debug ##
In console-war module :
    
    mvn -Pdev gwt:run [-Dbonita.home=/path/to/bonita/home]
    
## Structure ##

### Parent pom.xml ###
Contains the common maven configuration such as:
- the definition of all the dependencies version, e.g., junit.version, bonita.engine.version, gwt.version, ...
- the maven repositories

### Bonita-home module ###
Aggregate engine bonita home and web config modules to build a usable bonita home folder (available in target/bonita-home-${project.version}/bonita)

### Common module ###
### common-config
Contains the common client configuration to add to BONITA_HOME.

### common-server
Contains the back-end business logic, i.e., the code executed on the server side.

### common-test-toolkit
Contains integration tests utils

### Console module
Module containing portal implementation

#### console-config
Contains the console client configuration to add to BONITA_HOME.

#### console-server-impl
Contains the server side code of portal
	
#### console-war
Contains the source code of the client, i.e., the code cross-compiled to javascript and all the resources; 
Build the portal web application
		
### Form module ###
Module containing forms implementation

#### forms-config
Contains the forms client configuration to add to BONITA_HOME.

#### forms-design
// TODO

#### forms-model
Contains the source code of the model definition.
	
#### forms-rpc
Contains the source code of the client-server communication.

#### forms-server
Contains the back-end business logic, i.e., the code executed on the server side.

#### forms-view
Contains the source code of the client, i.e., the code cross-compiled to javascript and all the resources; 

### Platform module
Not maintained anymore

### Rest-api module ###
Module containing rest api implementation over engine api

#### rest-api-model
Contains the rest api model

#### rest-api-server
Contains the server side of rest api

### toolkit module ###
Commons classes for server and client side.
Will be dispatched in proper modules
