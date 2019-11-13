FROM tomcat:9.0

MAINTAINER ikab

COPY /usr/local/tomcat/webapps/target/agrimManager.war //usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
