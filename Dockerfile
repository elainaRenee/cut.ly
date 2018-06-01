FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app
COPY . /app
RUN mvn clean install

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=build /app/target/cutly-1.0.0.jar /app
ENTRYPOINT ["sh", "-c" ]
CMD ["java -jar cutly-1.0.0.jar"]