FROM frolvlad/alpine-oraclejdk8:slim

ADD maven/task-api.jar task-api.jar
RUN sh -c 'touch /task-api.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005","-jar","/task-api.jar"]