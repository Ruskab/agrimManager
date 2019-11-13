FROM tomcat:9.0

MAINTAINER ikab

COPY /home/travis/build/Ruskab/agrimManager/target/agrimManager.war //usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
