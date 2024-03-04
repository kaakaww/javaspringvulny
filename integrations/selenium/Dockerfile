
FROM adoptopenjdk/openjdk11:latest

# Update this value with your preferred version of HawkScan
ARG HAWKSCAN_VERSION="2.10.0"

USER root

RUN apt-get update && \
    apt-get install -y simpleproxy curl unzip xvfb firefox && \
    curl -L https://github.com/mozilla/geckodriver/releases/download/v0.32.0/geckodriver-v0.32.0-linux64.tar.gz | tar xz -C /usr/local/bin && \
    curl -L https://download.stackhawk.com/hawk/cli/hawk-${HAWKSCAN_VERSION}.zip -O && \
    unzip hawk-${HAWKSCAN_VERSION}.zip && \
    mkdir zap && \
    mv ./hawk-${HAWKSCAN_VERSION}/* ./zap && \
    rm -d hawk-${HAWKSCAN_VERSION}.zip hawk-${HAWKSCAN_VERSION} && \
    apt-get purge -y ca-certificates curl

WORKDIR /hawk

ENV PATH $JAVA_HOME/bin:/zap:$PATH

ENTRYPOINT ["shawk"]
