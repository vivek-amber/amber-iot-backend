
# Build Stage
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy security module
COPY security ./security

# Build security jar into local maven repo
RUN mvn -f security/pom.xml clean install -DskipTests

COPY device/pom.xml ./device/pom.xml
RUN mvn -f device/pom.xml dependency:go-offline


# Build user module
COPY device ./device

RUN mvn -f device/pom.xml clean package -DskipTests

# Runner Stage
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/device/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT [ "java","-jar","app.jar" ]
