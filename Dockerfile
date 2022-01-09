FROM openjdk:11.0.3-jdk-slim-stretch
ENV TZ Asia/Baku
ADD build/libs/spring-security-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app.jar"]