FROM --platform=linux/amd64 amazoncorretto:21

WORKDIR /app

# Changed from build/libs/ to app/build/libs/ to match your structure
COPY app/build/libs/*.jar app.jar

EXPOSE 9810
ENTRYPOINT ["java", "-jar", "app.jar"]