call %PATHTOPROJECT%\console\console-war\target\classes\setenv.cmd

rem #################################################
rem # Delete annoying .class files from src folder  #
rem #################################################
del /S %PATHTOPROJECT%\toolkit\toolkit-view\src\*.class

rem GWT / GWTx
rem gwt
set CLASSPATH="%MAVENREPOPATH%\com\google\gwt\gwt-user\2.5.1\gwt-user-2.5.1.jar";
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\com\google\gwt\gwt-dev\2.5.1\gwt-dev-2.5.1.jar";
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\com\googlecode\gwtquery\gwtquery\1.3.1\gwtquery-1.3.1.jar";
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\com\google\code\gwtx\gwtx\1.5.3\gwtx-1.5.3.jar";
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\javax\validation\validation-api\1.0.0.GA\validation-api-1.0.0.GA.jar";

set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\bonitasoft\web\tooling\bonita-gwt-tools\2.5.1\bonita-gwt-tools-2.5.1.jar";

rem JTA
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\codehaus\btm\btm\2.1.3\btm-2.1.3.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\javax\transaction\jta\1.1\jta-1.1.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\slf4j\slf4j-api\1.6.0\slf4j-api-1.6.0.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\slf4j\slf4j-log4j12\1.6.0\slf4j-log4j12-1.6.0.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\log4j\log4j\1.2.15\log4j-1.2.15.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\mortbay\jetty\jetty-naming\6.1.26\jetty-naming-6.1.26.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\mortbay\jetty\jetty-plus\6.1.26\jetty-plus-6.1.26.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\codehaus\btm\btm-jetty6-lifecycle\2.0.1\btm-jetty6-lifecycle-2.0.1.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\mysql\mysql-connector-java\5.1.25\mysql-connector-java-5.1.25.jar"

rem BONITA Engine
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\bonitasoft\engine\bonita-client\%BONITATARGETVERSION%\bonita-client-%BONITATARGETVERSION%.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\bonitasoft\engine\bonita-server\%BONITATARGETVERSION%\bonita-server-%BONITATARGETVERSION%.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\bonitasoft\engine\bonita-common\%BONITATARGETVERSION%\bonita-common-%BONITATARGETVERSION%.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\antlr\antlr\2.7.7\antlr-2.7.7.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\commons-codec\commons-codec\1.7\commons-codec-1.7.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\commons-collections\commons-collections\3.1\commons-collections-3.1.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\dom4j\dom4j\1.6.1\dom4j-1.6.1.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\net\sf\ehcache\ehcache-core\2.2.0\ehcache-core-2.2.0.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\codehaus\groovy\groovy-all\1.8.6\groovy-all-1.8.6.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\com\h2database\h2\1.3.170\h2-1.3.170.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\hibernate\hibernate-commons-annotations\3.2.0.Final\hibernate-commons-annotations-3.2.0.Final.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\hibernate\hibernate-core\3.6.7.Final\hibernate-core-3.6.7.Final.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\slf4j\slf4j-api\1.6.1\slf4j-api-1.6.1.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\hibernate\javax\persistence\hibernate-jpa-2.0-api\1.0.1.Final\hibernate-jpa-2.0-api-1.0.1.Final.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\javassist\javassist\3.12.1.GA\javassist-3.12.1.GA.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\javax\transaction\jta\1.1\jta-1.1.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\apache\chemistry\opencmis\chemistry-opencmis-client-api\0.6.0\chemistry-opencmis-client-api-0.6.0.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\apache\chemistry\opencmis\chemistry-opencmis-client-bindings\0.6.0\chemistry-opencmis-client-bindings-0.6.0.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\apache\chemistry\opencmis\chemistry-opencmis-client-impl\0.6.0\chemistry-opencmis-client-impl-0.6.0.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\apache\chemistry\opencmis\chemistry-opencmis-commons-impl\0.6.0\chemistry-opencmis-commons-impl-0.6.0.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\apache\chemistry\opencmis\chemistry-opencmis-commons-api\0.6.0\chemistry-opencmis-commons-api-0.6.0.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\com\googlecode\gchart\gchart\2.7\gchart-2.7.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\commons-dbcp\commons-dbcp\1.4\commons-dbcp-1.4.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\commons-pool\commons-pool\1.5.5\commons-pool-1.5.5.jar"


rem Bonita web toolkit
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\toolkit\toolkit-view\src\main\java"
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\toolkit\toolkit-view\src\main\resources"
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\toolkit\toolkit-server\src\main\java"
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\toolkit\toolkit-server\src\main\resources"

rem COMMON
rem common-model
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\common\common-model\src\main\java"
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\common\common-model\src\main\resources"

rem common-server
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\common\common-server\src\main\java"

rem CONSOLE
rem console-server-impl
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\console\console-server-impl\src\main\java"

rem console-reporting
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\console\console-reporting\src\main\java"
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\console\console-reporting\src\main\resources"

rem console-war
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\console\console-war\src\main\java"
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\console\console-war\src\main\resources"

rem Bonita Form
rem forms-model
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\forms\forms-model\src\main\java"

rem forms-rpc
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\forms\forms-rpc\src\main\java"

rem forms-server
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\forms\forms-server\src\main\java"

rem forms-view
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\forms\forms-view\src\main\java"
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\forms\forms-view\src\main\resources"

rem forms-application
set CLASSPATH=%CLASSPATH%;"%PATHTOPROJECT%\forms\forms-war\src\main\java"


rem Platform
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\bonitasoft\platform\platform-model\%CONSOLETARGETVERSION%\platform-model-%CONSOLETARGETVERSION%.jar"
set CLASSPATH=%CLASSPATH%;"%MAVENREPOPATH%\org\bonitasoft\platform\platform-server-impl\%CONSOLETARGETVERSION%\platform-server-impl-%CONSOLETARGETVERSION%.jar"

@echo on
java -Xmx512m -XX:MaxPermSize=256m -cp %CLASSPATH% -Dfile.encoding=UTF-8 -Dbonita.home=%BONITA_HOME% -Xdebug -Xnoagent -Djava.compiler=NONE -Dbuild.compiler=org.bonitasoft.tools.gwt.JDTCompiler -Xrunjdwp:transport=dt_socket,server=y,address=8001,suspend=y -Dcatalina.base="%PATHTOPROJECT%\console\console-war\target\tomcat" com.google.gwt.dev.DevMode -server org.bonitasoft.tools.gwt.jetty.BonitaJettyLauncher -gen "%PATHTOPROJECT%\console\console-war\target\.generated" -logLevel INFO -war "%PATHTOPROJECT%\console\console-war\target\console-war-%CONSOLETARGETVERSION%" -port 8888 -startupUrl login.jsp?redirectUrl=portal%%2Fhomepage%%3Fgwt.codesvr%%3D127.0.0.1%%3A9997  org.bonitasoft.console.%GWT_MODULE%

rem #################################################
rem # Delete annoying .class files from src folder  #
rem #################################################
del /S %PATHTOPROJECT%\toolkit\toolkit-view\src\*.class
