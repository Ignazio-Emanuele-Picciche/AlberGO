FROM openjdk:11
EXPOSE 8080
ADD target/albergo-backend.jar albergo-backend.jar
ENTRYPOINT ["java","-jar","/albergo-backend.jar"]