FROM eclipse-temurin:23-jdk AS build
WORKDIR /app
COPY gradlew ./
COPY gradle ./gradle
COPY settings.gradle build.gradle ./
COPY src ./src
RUN chmod +x gradlew && ./gradlew installDist --no-daemon

FROM eclipse-temurin:23-jre
WORKDIR /opt/car-dealership
COPY --from=build /app/build/install/car-dealership ./
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"
ENTRYPOINT ["./bin/car-dealership"]
