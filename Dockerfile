FROM tomcat:9.0

MAINTAINER ikab

RUN ls

RUN ls /home/travis/build/Ruskab/agrimManager/target/

COPY ${PWD}/home/travis/build/Ruskab/agrimManager/target/agrimManager.war //usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
