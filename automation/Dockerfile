
# Build Stage
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy security module
COPY security ./security

# Build security jar into local maven repo
RUN mvn -f security/pom.xml clean install -DskipTests

COPY automation/pom.xml ./automation/pom.xml
RUN mvn -f automation/pom.xml dependency:go-offline

# Build automation module
COPY automation ./automation

RUN mvn -f automation/pom.xml clean package -DskipTests


# Runtime Stage
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/automation/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT [ "java","-jar","app.jar" ]