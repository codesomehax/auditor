# build
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# package
FROM openjdk:11
COPY --from=build /home/app/target/auditor-*.jar /usr/local/lib/auditor.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/auditor.jar"]