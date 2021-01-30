FROM openjdk:8
MAINTAINER Prashanth
ARG JAR_FILE=target/gameofthree-1.0.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080