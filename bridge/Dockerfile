

# Build Stage
FROM maven:3.9.9-eclipse-temurin-17 AS builder


WORKDIR /app


# Copy security module
COPY security ./security


# Build security jar into local maven repo
RUN mvn -f security/pom.xml clean install -DskipTests


COPY bridge/pom.xml ./bridge/pom.xml
RUN mvn -f bridge/pom.xml dependency:go-offline


# Build user module
COPY bridge ./bridge


RUN mvn -f bridge/pom.xml clean package -DskipTests


# Runtime Stage
FROM eclipse-temurin:17-jre



WORKDIR /app


COPY --from=builder /app/bridge/target/*.jar app.jar


EXPOSE 8081


ENTRYPOINT [ "java","-jar","app.jar" ]