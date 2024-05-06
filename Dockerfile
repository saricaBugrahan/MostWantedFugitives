FROM maven:3.6.0-jdk-11-slim as BUILD

# Building via maven file

COPY src /usr/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package


FROM openjdk:11
COPY --from=build /usr/src/app/target/MostWantedFugitives-1.0-SNAPSHOT.jar /usr/app/MostWantedFugitives-1.0-SNAPSHOT.jar
CMD ["java","-jar","/usr/app/MostWantedFugitives-1.0-SNAPSHOT.jar"]
