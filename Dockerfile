# ─── ESTÁGIO 1: Build da Aplicação ───────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copia arquivos do Maven para cache de dependências
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Garante permissão de execução para o wrapper do Maven e baixa as dependências
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copia o código fonte e gera o jar
COPY src ./src
RUN ./mvnw clean package -DskipTests


# ─── ESTÁGIO 2: Imagem de Produção (Otimizada para o Render) ─────────────────
FROM eclipse-temurin:17-jre-alpine AS runner

WORKDIR /app

# Copia o .jar gerado no estágio anterior
COPY --from=builder /app/target/*.jar app.jar

# O Render injeta a porta automaticamente através da variável $PORT.
# Passamos essa variável para o Spring Boot via argumento do sistema (-Dserver.port).
# Também adicionamos flags para otimizar o uso de memória RAM no ambiente limitado do Render.
ENTRYPOINT ["sh", "-c", "java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dserver.port=${PORT:-8081} -jar app.jar"]