FROM openjdk:8
COPY ./target/universal/odyssey-*.zip /opt
RUN unzip /opt/odyssey-*.zip -d /opt \
 && mv /opt/odyssey-*/ /opt/odyssey/ \
 && rm /opt/odyssey-*.zip
EXPOSE 9000
ENTRYPOINT [ "./opt/odyssey/bin/odyssey" ]