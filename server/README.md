# Server #

Bonita server app.  
Enhance server with a REST API

## Launch dev mode ##
The module need to first be built using

    mvn clean install -Pdev
    
Then we can launch jetty to host the app using

    mvn jetty:run -Pdev
    
To launch jetty in debug mode

	mvnDebug jetty:run -Pdev -pl server