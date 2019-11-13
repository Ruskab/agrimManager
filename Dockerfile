FROM tomcat:9.0

MAINTAINER ikab

RUN ls /webapps

RUN ls ${PWD}

COPY ${PWD}/target/agrimManager.war //usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
