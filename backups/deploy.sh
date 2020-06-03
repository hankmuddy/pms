#!/bin/bash
ssh root@144.76.2.181 /opt/tomcat8/bin/catalina.sh stop
ssh root@144.76.2.181 rm -rf /opt/tomcat8/work/Catalina/localhost
scp web-1.0-SNAPSHOT.war root@144.76.2.181:/opt/tomcat8/webapps/app.war
ssh root@144.76.2.181 /opt/tomcat8/bin/catalina.sh start