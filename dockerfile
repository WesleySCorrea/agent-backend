# ---------- Etapa 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia apenas o pom.xml para cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do código fonte
COPY src ./src

# Faz o build gerando o fat jar executável
RUN mvn clean package spring-boot:repackage -DskipTests

# ---------- Etapa 2: Runtime ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia o jar do build stage
COPY --from=build /app/target/*.jar app.jar

# Variáveis de ambiente opcionais
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Porta padrão do Spring Boot
EXPOSE 8080

# Comando de execução
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
