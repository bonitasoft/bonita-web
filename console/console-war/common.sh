#!/bin/bash
source $PATHTOPROJECT/console/console-war/target/classes/setenv.sh

#################################################
# Delete annoying .class files from src folder  #
#################################################
## Define function
function deleteClassFilesFromToolkitSrcFolder {
    find "$PATHTOPROJECT/toolkit/toolkit-view/src" -name "*.class" -print0 | xargs -0 rm -rf
}

## Delete .class files
deleteClassFilesFromToolkitSrcFolder

##GWT / GWTx
##gwt
export CLASSPATH="$MAVENREPOPATH/com/google/gwt/gwt-user/2.5.1/gwt-user-2.5.1.jar":"$MAVENREPOPATH/com/google/gwt/gwt-dev/2.5.1/gwt-dev-2.5.1.jar"

# JTA
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/codehaus/btm/btm/2.1.3/btm-2.1.3.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/javax/transaction/jta/1.1/jta-1.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/slf4j/slf4j-api/1.6.0/slf4j-api-1.6.0.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/slf4j/slf4j-log4j12/1.6.0/slf4j-log4j12-1.6.0.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/log4j/log4j/1.2.15/log4j-1.2.15.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/mortbay/jetty/jetty-naming/6.1.26/jetty-naming-6.1.26.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/mortbay/jetty/jetty-plus/6.1.26/jetty-plus-6.1.26.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/codehaus/btm/btm-jetty6-lifecycle/2.0.1/btm-jetty6-lifecycle-2.0.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/mysql/mysql-connector-java/5.1.25/mysql-connector-java-5.1.25.jar"

##BONITA Engine
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/aopalliance/aopalliance/1.0/aopalliance-1.0.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/springframework/spring-context/3.1.2.RELEASE/spring-context-3.1.2.RELEASE.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/springframework/spring-beans/3.1.2.RELEASE/spring-beans-3.1.2.RELEASE.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/springframework/spring-core/3.1.2.RELEASE/spring-core-3.1.2.RELEASE.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/springframework/spring-expression/3.0.6.RELEASE/spring-expression-3.0.6.RELEASE.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/springframework/spring-asm/3.1.2.RELEASE/spring-asm-3.1.2.RELEASE.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/jboss/resteasy/resteasy-jaxrs/2.3.4.Final/resteasy-jaxrs-2.3.4.Final.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/jboss/resteasy/jaxrs-api/2.3.4.Final/jaxrs-api-2.3.4.Final.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/scannotation/scannotation/1.0.3/scannotation-1.0.3.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/javax/annotation/jsr250-api/1.0/jsr250-api-1.0.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/apache/httpcomponents/httpclient/4.1.2/httpclient-4.1.2.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/apache/httpcomponents/httpcore/4.1.2/httpcore-4.1.2.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/net/jcip/jcip-annotations/1.0/jcip-annotations-1.0.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/com/thoughtworks/xstream/xstream/1.4.2/xstream-1.4.2.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/xmlpull/xmlpull/1.1.3.1/xmlpull-1.1.3.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/xpp3/xpp3_min/1.1.4c/xpp3_min-1.1.4c.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/net/sf/ehcache/ehcache-core/2.2.0/ehcache-core-2.2.0.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/apache/xbean/xbean-classloader/3.7/xbean-classloader-3.7.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/apache/chemistry/opencmis/chemistry-opencmis-client-impl/0.6.0/chemistry-opencmis-client-impl-0.6.0.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/apache/chemistry/opencmis/chemistry-opencmis-client-api/0.6.0/chemistry-opencmis-client-api-0.6.0.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/apache/chemistry/opencmis/chemistry-opencmis-commons-api/0.6.0/chemistry-opencmis-commons-api-0.6.0.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/apache/chemistry/opencmis/chemistry-opencmis-commons-impl/0.6.0/chemistry-opencmis-commons-impl-0.6.0.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/com/sun/xml/ws/jaxws-rt/2.1.7/jaxws-rt-2.1.7.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/javax/xml/ws/jaxws-api/2.1/jaxws-api-2.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/javax/xml/bind/jaxb-api/2.1/jaxb-api-2.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/com/sun/xml/bind/jaxb-impl/2.1.11/jaxb-impl-2.1.11.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/com/sun/xml/messaging/saaj/saaj-impl/1.3.3/saaj-impl-1.3.3.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/javax/xml/soap/saaj-api/1.3/saaj-api-1.3.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/com/sun/xml/stream/buffer/streambuffer/0.9/streambuffer-0.9.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/javax/activation/activation/1.1/activation-1.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/codehaus/woodstox/wstx-asl/3.2.3/wstx-asl-3.2.3.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/stax/stax-api/1.0.1/stax-api-1.0.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/jvnet/staxex/stax-ex/1.2/stax-ex-1.2.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/javax/xml/stream/stax-api/1.0.1/stax-api-1.0.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/junit/junit/4.10/junit-4.10.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/hamcrest/hamcrest-core/1.1/hamcrest-core-1.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/com/sun/org/apache/xml/internal/resolver/20050927/resolver-20050927.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/jvnet/mimepull/1.3/mimepull-1.3.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/com/googlecode/json-simple/json-simple/1.1/json-simple-1.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/apache/chemistry/opencmis/chemistry-opencmis-client-bindings/0.6.0/chemistry-opencmis-client-bindings-0.6.0.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/apache/felix/org.osgi.core/1.0.0/org.osgi.core-1.0.0.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/commons-codec/commons-codec/1.7/commons-codec-1.7.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/codehaus/groovy/groovy-all/1.8.6/groovy-all-1.8.6.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/hibernate/hibernate-core/3.6.7.Final/hibernate-core-3.6.7.Final.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/antlr/antlr/2.7.6/antlr-2.7.6.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/commons-collections/commons-collections/3.1/commons-collections-3.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/hibernate/hibernate-commons-annotations/3.2.0.Final/hibernate-commons-annotations-3.2.0.Final.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/hibernate/javax/persistence/hibernate-jpa-2.0-api/1.0.1.Final/hibernate-jpa-2.0-api-1.0.1.Final.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/javassist/javassist/3.12.1.GA/javassist-3.12.1.GA.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/quartz-scheduler/quartz/2.1.6/quartz-2.1.6.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/com/h2database/h2/1.3.170/h2-1.3.170.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/javax/transaction/jta/1.1/jta-1.1.jar"

export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/bonitasoft/engine/bonita-server/$BONITATARGETVERSION/bonita-server-$BONITATARGETVERSION.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/bonitasoft/engine/log/bonita-log-impl/$BONITATARGETVERSION/bonita-log-impl-$BONITATARGETVERSION.jar"

export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/com/googlecode/gchart/gchart/2.7/gchart-2.7.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/commons-dbcp/commons-dbcp/1.4/commons-dbcp-1.4.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/commons-pool/commons-pool/1.5.5/commons-pool-1.5.5.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/com/googlecode/gwtquery/gwtquery/1.3.1/gwtquery-1.3.1.jar"

export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/bonitasoft/web/tooling/bonita-gwt-tools/2.5.1/bonita-gwt-tools-2.5.1.jar"

##Bonita web toolkit
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/toolkit/toolkit-view/src/main/java"
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/toolkit/toolkit-view/src/main/resources"
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/toolkit/toolkit-server/src/main/java"
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/toolkit/toolkit-server/src/main/resources"

##Bonita form part
##forms-model
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/forms/forms-model/src/main/java"

##forms-rpc
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/forms/forms-rpc/src/main/java"

##forms-server
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/forms/forms-server/src/main/java"

##forms-view
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/forms/forms-view/src/main/java"
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/forms/forms-view/src/main/resources"

##forms-application
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/forms/forms-war/src/main/java"

##SECURITY
##common-model
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/common/common-model/src/main/java"
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/common/common-model/src/main/resources"

##common-server
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/common/common-server/src/main/java"


##console
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/console/console-model/src/main/java"
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/console/console-server-impl/src/main/java"
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/console/console-reporting/src/main/java"
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/console/console-reporting/src/main/resources"
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/console/console-war/src/main/java"
export CLASSPATH=$CLASSPATH:"$PATHTOPROJECT/console/console-war/src/main/resources"

##platform
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/bonitasoft/platform/platform-model/$BONITATARGETVERSION/platform-model-$BONITATARGETVERSION.jar"
export CLASSPATH=$CLASSPATH:"$MAVENREPOPATH/org/bonitasoft/platform/platform-server-impl/$BONITATARGETVERSION/platform-server-impl-$BONITATARGETVERSION.jar"

#source $BONITA_HOME/client/platform/scripts/createDefaultTenant.sh 1 $BONITA_HOME $PATHTOPROJECT/console/console-war/target/console-war-$CONSOLETARGETVERSION/WEB-INF/lib $TECH_USER $TECH_PASSWORD
java -Xmx512m -XX:MaxPermSize=256m -cp $CLASSPATH -Dfile.encoding=UTF-8 -Dbonita.home=$BONITA_HOME -Xdebug -Xnoagent -Djava.compiler=NONE -Dbuild.compiler=org.bonitasoft.tools.gwt.JDTCompiler -Xrunjdwp:transport=dt_socket,server=y,address=8001,suspend=y -Dcatalina.base="$PATHTOPROJECT/console/console-war/target/tomcat" com.google.gwt.dev.DevMode -server org.bonitasoft.tools.gwt.jetty.BonitaJettyLauncher -gen "$PATHTOPROJECT/console/console-war/target/.generated" -logLevel INFO -war "$PATHTOPROJECT/console/console-war/target/console-war-$CONSOLETARGETVERSION" -port 8888 -startupUrl login.jsp?redirectUrl=portal%2Fhomepage%3Fgwt.codesvr%3D127.0.0.1%3A9997 org.bonitasoft.console.$GWT_MODULE

## Delete .class files
deleteClassFilesFromToolkitSrcFolder
