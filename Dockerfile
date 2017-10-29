FROM openjdk:8-jdk
ADD target/*.jar app.jar
RUN sh -c 'touch /app.jar'

ADD ./src/main/docker/start.sh /app.sh
RUN chmod +x /app.sh
ENTRYPOINT [ "/app.sh" ]
CMD [""]
EXPOSE 8080
