FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
COPY ./keystore.jks /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 8443:8443
RUN mkdir /app
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/to-do-list-app.jar
COPY --from=build /home/gradle/src/keystore.jks /app
ENTRYPOINT ["java","-jar","/app/to-do-list-app.jar"]