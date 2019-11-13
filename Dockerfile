FROM tomcat:9.0

MAINTAINER ikab

RUN ls /usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
