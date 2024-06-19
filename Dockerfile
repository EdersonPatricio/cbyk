# Etapa de compilação
FROM maven:3.8.7-eclipse-temurin-17 AS build

# Diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia os POMs e código-fonte para o diretório de trabalho
COPY pom.xml .
COPY cbyk-api/pom.xml cbyk-api/pom.xml
COPY cbyk-common/pom.xml cbyk-common/pom.xml

# Faz o download das dependências
RUN mvn dependency:go-offline

# Copia o restante do código fonte
COPY . .

# Compila o projeto pai, o que compila também os subprojetos
RUN mvn clean package -DskipTests

# Etapa de execução
FROM eclipse-temurin:17-jre-alpine

# Diretório de trabalho onde o JAR será copiado
WORKDIR /app

# Copia o JAR gerado para o diretório de execução
COPY --from=build /app/cbyk-api/target/cbyk-api-*.war /app/cbyk-api.war

# Expor a porta padrão do Spring Boot (8080)
EXPOSE 8080

# Comando para iniciar a aplicação Spring Boot
ENTRYPOINT ["java","-jar","/app/cbyk-api.war"]