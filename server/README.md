# Server #

Bonita server app.  
Enhance server with a REST API

## Launch dev mode ##
The module need to first be built using

    mvn clean install -Pdev
    
Then we can launch jetty to host the app using

    mvn jetty:run -Pdev [-Dbonita.home=/path/to/bonita/home]
    
By default, bonita.home point to *target/bonita-home-${project.version}/bonita*. If you want to specify an other bonita.home add argument -Dbonita.home to command line.
