# Usando a imagem do Eclipse Temurin com JDK 21 em Alpine como imagem base para o build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /application

# Copiando os arquivos necessários para fazer o build da aplicação
COPY mvnw .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

# Executando o build do .jar
RUN ./mvnw package -DskipTests
RUN cp target/*.jar app.jar
RUN rm -rf target


# Iniciando etapa de build da imagem Docker
FROM eclipse-temurin:21-jre-alpine
WORKDIR /application

# Copiando o arquivo app.jar do estágio de build para o diretório de trabalho no novo container
COPY --from=builder application/app.jar app.jar

# Definindo o ponto de entrada do container, que executa a aplicação Java a partir do JAR
ENTRYPOINT [ "java", "-jar", "app.jar" ]