# Imagem do OpenJDK 17
FROM openjdk:17-jdk-slim

# Criar diretório da aplicação
WORKDIR /app

# Copiar o arquivo JAR da aplicação
COPY target/creditproposals-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que a aplicação vai rodar (8080)
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]