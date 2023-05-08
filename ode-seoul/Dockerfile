FROM ibm-semeru-runtimes:open-17-jdk-jammy AS builder
WORKDIR /gradle
COPY gradlew build.gradle settings.gradle ./
COPY gradle/ gradle/
RUN ./gradlew build -x test --parallel --continue 2> /dev/null || true
COPY . .
RUN ["./gradlew", "clean", "build", "--no-daemon"]

FROM ibm-semeru-runtimes:open-17-jre-jammy
EXPOSE 8080
WORKDIR /app
COPY --from=builder /gradle/build/libs/*.jar ./app.jar
CMD ["java", "-jar", "app.jar"]
