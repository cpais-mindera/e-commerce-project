FROM eclipse-temurin:21.0.2_13-jdk 

COPY . /app 
WORKDIR /app
RUN ./mvnw clean install
RUN rm -rfv ./mvnw
RUN find . -type f -name "*.jar"
RUN mv ./target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "-Duser.timezone=Europe/London", "-Dspring.profiles.active=dev", "app.jar"]