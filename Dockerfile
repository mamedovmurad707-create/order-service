# Jar host-da yığılır (./gradlew bootJar), image yalnız runtime-dır.
# Şirkət şəbəkəsində docker.io bağlıdır, ona görə Harbor-dakı image istifadə olunur.
FROM harbor.azerconnect.az/infra/eclipse-temurin:17-jre

WORKDIR /app

RUN useradd --system --uid 1001 spring
USER spring

COPY build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
