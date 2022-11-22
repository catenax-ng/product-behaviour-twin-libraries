# taken from https://medium.com/geekculture/dockerizing-a-spring-boot-application-with-maven-122286e9f582

# AS <NAME> to name this stage as maven
FROM maven:3.8.4-openjdk-17 as maven
LABEL MAINTAINER="beendikt.franke@dlr.de"

EXPOSE 8080

WORKDIR /usr/src/app
COPY . /usr/src/app
# Compile and package the application to an executable JAR
RUN cd product-behaviour-twin-libraries && mvn clean install  -Dmaven.test.skip && cd ..
RUN cd product-oem-hi-app && mvn clean package spring-boot:repackage -DskipTests -Dspring.profiles.active=hibackendservice

# For Java 11,
FROM openjdk:17-oracle

EXPOSE 25553
# ADD ./resources /opt/app/resources
# ADD ./testdata /opt/app/testdata

ARG JAR_FILE=product-oem-hi-app/target/product-oem-hi-app-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

# Copy the  jar from the maven stage to the /opt/app directory of the current stage.
COPY --from=maven /usr/src/app/${JAR_FILE} /opt/app/app.jar

ENTRYPOINT ["java","-jar", "app.jar", "-Dspring.profiles.active=hibackendservice"]