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


RUN apt-get update && \
    apt-get install -y wget gnupg && \
    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable

ENV CHROME_VERSION=124.0.6367.155
RUN wget -q --continue -P . "https://storage.googleapis.com/chrome-for-testing-public/$CHROME_VERSION/linux64/chromedriver-linux64.zip" && \
    unzip -q ./chromedriver-linux64.zip -d /usr/local/bin/ && \
    chmod +x /usr/local/bin/chromedriver-linux64/chromedriver && \
    rm ./chromedriver-linux64.zip


COPY --from=BUILD /usr/app/target/MostWantedFugitives-1.0-SNAPSHOT.jar /usr/app/MostWantedFugitives-1.0-SNAPSHOT.jar

CMD ["java", "-cp", "/usr/app/MostWantedFugitives-1.0-SNAPSHOT.jar", "com.fugitives.Main"]
