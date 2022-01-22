FROM openjdk:11
ADD target/albergo-backend.jar albergo-backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/albergo-backend.jar"]