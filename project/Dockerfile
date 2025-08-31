FROM openjdk:21

WORKDIR /app

COPY target/project-0.0.1-SNAPSHOT.jar /app/project-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","project-0.0.1-SNAPSHOT.jar"]