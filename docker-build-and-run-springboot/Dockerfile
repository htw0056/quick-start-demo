FROM maven:3.6.3-jdk-8 AS build
ARG project=springboot-docker-build-run
COPY ./$project /usr/src/$project
RUN mvn -f /usr/src/$project/pom.xml clean package

FROM openjdk:8
ARG project=springboot-docker-build-run
COPY --from=build /usr/src/$project/target/demo-0.0.1-SNAPSHOT.jar /usr/app/target/demo-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/target/demo-0.0.1-SNAPSHOT.jar"]