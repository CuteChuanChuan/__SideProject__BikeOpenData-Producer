FROM openjdk:11-slim

WORKDIR /app

COPY target/scala-2.13/BikeDataProducer-assembly-0.1.0-SNAPSHOT.jar app.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]