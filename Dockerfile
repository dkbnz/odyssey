FROM openjdk:8-jre-slim-buster
RUN apt-get update && apt-get upgrade
RUN apt-get install --fix-missing -y xvfb unzip
COPY ./target/universal/odyssey-*.zip /opt
RUN unzip /opt/odyssey-*.zip -d /opt \
 && mv /opt/odyssey-*/ /opt/odyssey/ \
 && rm /opt/odyssey-*.zip
EXPOSE 9000
CMD [ "/usr/bin/xvfb-run", "-e", "/dev/stdout", "-a", "/opt/odyssey/bin/odyssey" ]
