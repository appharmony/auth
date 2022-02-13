# syntax=docker/dockerfile:1

FROM openjdk:11

VOLUME /tmp
EXPOSE 8080
ADD build/libs/auth-0.0.1-SNAPSHOT.jar auth.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/auth.jar"]
