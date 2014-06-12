# Console war #

Bonita web main webapp.  
Build a webapp containing bonita portal and bonita forms

## Launch dev mode ##
To launch application in development mode, you have to run the following command in portal folder
    
    mvn -Pdev gwt:run

## Launch debug mode ##
Run the following command
    
    mvn -Pdev gwt:debug

Then launch a remote debugger on port 8000.  
You can modify sources and put breakpoints to debug your code

## Launch super dev mode ##
Run the following command
    
    mvn process-classes gwt:run-codeserver -Pdev