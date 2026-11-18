# ---------- Estágio 1: build do .jar com Maven ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests clean package

# ---------- Estágio 2: imagem final, só com o JRE ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/sombracerta-0.0.1-SNAPSHOT.jar app.jar

# O plano Free do Render tem 512 MB de RAM
ENV JAVA_OPTS="-Xmx320m -XX:+UseSerialGC"

EXPOSE 8080
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
