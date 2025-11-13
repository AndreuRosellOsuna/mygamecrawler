#FROM bellsoft/liberica-runtime-container:jre-21-slim-musl
FROM mcr.microsoft.com/playwright/java:v1.54.0-noble
WORKDIR /home/app
COPY target/web-*.jar /home/app/mycrawler.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "mycrawler.jar"]
