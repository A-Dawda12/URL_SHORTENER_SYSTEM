
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

copy pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTest package

FROM eclipse-termurin:21-jre-apline
WORKDIR /app

RUN apk add --n-cache wget \
    && addgroup -S app \
    && adduser -S app -G  aap \
COPY --from=build /app/target/url-shortener-backend-*.jar aap.jar \
USER app

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]