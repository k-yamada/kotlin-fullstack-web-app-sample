FROM amazoncorretto:11-alpine

ENV APPLICATION_USER ktor
RUN adduser -D -g '' $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY ./build/libs/kotlin-fullstack-web-app-sample-1.0-SNAPSHOT.jar /app/fullstack.jar
WORKDIR /app

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:MaxRAMPercentage=90", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-XshowSettings:vm", "-jar", "fullstack.jar"]
