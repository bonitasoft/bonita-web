# Console war #

Bonita web main webapp
Build a webapp containing bonita portal and bonita forms

## Launch dev mode ##
To launch application in development mode, you have to run the following command in console-war folder
    
    mvn -Pdev gwt:run [-Dbonita.home=/path/to/bonita/home]

It open an embeded jetty server launching the app in development mode, you can modify sources a refresh your browser to see changes.  
By default, bonita.home point to ../../bonita-home/target/bonita-home-${project.version}/bonita. If you want to specify an other bonita.home add argument -Dbonita.home to command line.

## Launch debug mode ##
Run the following command
    
    mvn -Pdev gwt:debug [-Dbonita.home=/path/to/bonita/home]

Then launch a remote debugger on port 8000.  
You can modify sources and put breakpoint to debug your code
