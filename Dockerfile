FROM maven:3.6.0-jdk-11-slim as BUILD

COPY src /usr/app/src
COPY pom.xml /usr/app

WORKDIR /usr/app
RUN mvn clean package




# Running the jar file
FROM openjdk:11

ARG CERT="certificate.cer"
COPY $CERT /opt/workdir/$CERT
RUN keytool -importcert -file /opt/workdir/$CERT -alias $CERT -cacerts -storepass changeit -noprompt



COPY --from=BUILD /usr/app/target/MostWantedFugitives-1.0-SNAPSHOT.jar /usr/app/MostWantedFugitives-1.0-SNAPSHOT.jar

CMD ["java", "-cp", "/usr/app/MostWantedFugitives-1.0-SNAPSHOT.jar", "com.fugitives.Main"]
