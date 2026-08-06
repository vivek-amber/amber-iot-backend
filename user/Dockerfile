
# Build Stage
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy security module
COPY security ./security

# Build security jar into local maven repo
RUN mvn -f security/pom.xml clean install -DskipTests

COPY user/pom.xml ./user/pom.xml
RUN mvn -f user/pom.xml dependency:go-offline

# Build user module
COPY user ./user

RUN mvn -f user/pom.xml clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/user/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT [ "java","-jar","app.jar" ]