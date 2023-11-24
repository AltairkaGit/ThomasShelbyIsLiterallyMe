#
# Build stage
#
FROM maven:3-amazoncorretto-17 AS build
WORKDIR /app/

COPY . ./
#Install maven deps
RUN mvn -f ./pom.xml clean package
#
# Package stage
#
FROM amazoncorretto:17-alpine
WORKDIR /app-serv/
COPY --from=build /app/target/thomas-0.0.1-SNAPSHOT.jar .
COPY --from=build /app/src/main/resources .
COPY --from=build /app/env.properties .
RUN chmod +x thomas-0.0.1-SNAPSHOT.jar

# Expose the port your application will run on
EXPOSE 80

CMD ["java", "-jar", "thomas-0.0.1-SNAPSHOT.jar"]
