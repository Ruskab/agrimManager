FROM tomcat:9.0

MAINTAINER ikab

COPY /target/agrimManager.war //usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
