# Usa uma imagem oficial do OpenJDK para rodar a aplicação
FROM eclipse-temurin:17-jre-jammy

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo .jar gerado na sua etapa de build para o container
# Substitua "seu-projeto.jar" pelo nome real do jar gerado na pasta target/ ou build/
COPY target/diorana-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que sua aplicação utiliza (o Render atribui automaticamente à variável $PORT)
EXPOSE 8082

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
