FROM openjdk:8-jre-alpine
COPY /target/mooc-0.0.1-SNAPSHOT.jar /app/finalbinar.jar
CMD ["java", "-jar", "/app/finalbinar.jar"]
