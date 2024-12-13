FROM eclipse-temurin:23-jdk AS builder

LABEL maintainer="huiqing"

## How to build the application
WORKDIR /app


# copy files
COPY ./mvnw .
COPY .mvn .mvn

COPY pom.xml .
COPY src src

# make mvnw executable
RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true
# /app/target/noticeboard-0.0.1-SNAPSHOT.jar


FROM eclipse-temurin:23-jre

WORKDIR /app

COPY --from=builder /app/target/noticeboard-0.0.1-SNAPSHOT.jar vttpb-b5-ssf-assessment.jar


# check if curl command is available
RUN apt update && apt install -y curl


ENV PORT=8080
ENV NOTICEBOARD_DB_HOST=localhost NOTICEBOARD_DB_PORT=6379
ENV NOTICEBOARD_DB_USERNAME="" NOTICEBOARD_DB_PASSWORD=""
ENV NOTICEBOARD_DB_SERVER_HOST=https://publishing-production-d35a.up.railway.app/


EXPOSE ${PORT}


HEALTHCHECK --interval=60s --start-period=120s \
    CMD curl http://localhost:${PORT}/status || exit 1


SHELL [ "/bin/sh", "-c" ]


ENTRYPOINT SERVER_PORT=${PORT} java -jar vttpb-b5-ssf-assessment.jar