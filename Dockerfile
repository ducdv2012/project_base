FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/project-base-system.jar

RUN mkdir /opt/base-system

COPY ${JAR_FILE} /opt/base-system/project-base-system.jar

ENTRYPOINT ["java","-jar","/opt/base-system/project-base-system.jar"]
