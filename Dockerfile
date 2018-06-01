FROM openjdk:8-jdk-alpine
COPY target/ehcache-0.0.1-SNAPSHOT.jar /usr/src/
WORKDIR /usr/src/
CMD ["java", "-jar", "ehcache-0.0.1-SNAPSHOT.jar"]