FROM maven:3.6.2-jdk-11 as builder

WORKDIR /usr/local/tomcat/webapps/

COPY . .

RUN mvn clean install -DskipTests -Denvironment=tomcat

FROM tomcat:9.0

MAINTAINER ikab

COPY --from=builder /usr/local/tomcat/webapps/target/agrimManager.war //usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]