# Etapa de compila��o
FROM maven:3.8.7-eclipse-temurin-17 AS build

# Diret�rio de trabalho dentro do cont�iner
WORKDIR /app

# Copia os POMs e c�digo-fonte para o diret�rio de trabalho
COPY pom.xml .
COPY cbyk-api/pom.xml cbyk-api/pom.xml
COPY cbyk-common/pom.xml cbyk-common/pom.xml

# Faz o download das depend�ncias
RUN mvn dependency:go-offline

# Copia o restante do c�digo fonte
COPY . .

# Compila o projeto pai, o que compila tamb�m os subprojetos
RUN mvn clean package -DskipTests

# Etapa de execu��o
FROM eclipse-temurin:17-jre-alpine

# Diret�rio de trabalho onde o JAR ser� copiado
WORKDIR /app

# Copia o JAR gerado para o diret�rio de execu��o
COPY --from=build /app/cbyk-api/target/cbyk-api-*.war /app/cbyk-api.war

# Expor a porta padr�o do Spring Boot (8080)
EXPOSE 8080

# Comando para iniciar a aplica��o Spring Boot
ENTRYPOINT ["java","-jar","/app/cbyk-api.war"]