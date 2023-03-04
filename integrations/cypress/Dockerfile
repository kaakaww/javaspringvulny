FROM stackhawk/hawkscan

ARG NODE_VERSION="16.x"
ARG CYPRESS_VERSION="10.10.0"

USER root

RUN apt-get update && \
    apt-get -y install curl libnss3 libatk-bridge2.0-0 libdrm-dev libxkbcommon-dev libgbm-dev libasound-dev libatspi2.0-0 libxshmfence-dev && \
    curl -fsSL https://deb.nodesource.com/setup_${NODE_VERSION} | bash && \
    apt-get -y install nodejs && \
    npm install --global cypress@${CYPRESS_VERSION}

WORKDIR /hawk
