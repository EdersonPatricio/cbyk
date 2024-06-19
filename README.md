# README - CBYK | Desafio Técnico 🚀

## Descrição 📝

Este projeto consiste na implementação de uma API REST para um sistema de contas a pagar, de acordo com o que foi pedido. O projeto é desenvolvido em Java com o framework Spring Boot. Além disso, foi implementado Swagger para facilitar a visualização dos casos de uso da API, e também utilizo a biblioteca lombok.

## API (Backend) 💻

A API é responsável por fornecer 1 controller contendo os seguintes endpoints:

- save -> Cadastra uma nova conta
- atualizar-conta -> Atualiza uma conta
- atualizar-situacao -> Atualiza a situação de uma conta
- find-paginado -> Consulta todos as contas de forma paginada
- find-por-id -> Consulta uma conta a partir do id
- find-contas-a-pagar -> Consulta lista de contas a pagar com filtro de data de vencimento e descrição
- total-pago-por-periodo -> Obtem o valor total pago por período
- importar-contas -> Realiza importação de contas a pagar via arquivo CSV
- deletar -> Deleta uma conta

Também foram implementados testes unitários para a classe controller( CbykApiApplicationTests.java ) e para a classe do servico( ContaServiceTest.java ).

## Tecnologias Utilizadas 🛠️

- Java 17
- Spring Boot 3.3.0
- Maven 3.8.1
- Swagger 3.1.0
- SpringDocs 2.5.0( ferramenta que auxilia a configuração do Swagger no projeto )
- Flyway 10.15.0( utilizado para criar a estrutura do banco de dados )
- Docker

## Configuração do Ambiente ⚙️

Antes de executar o projeto, certifique-se de ter o seguinte configurado em sua máquina:

- JDK 17 (Java Development Kit)
- IDE (Eclipse, IntelliJ, ou outra IDE)
- Instalação do lombok.jar( https://projectlombok.org ) na IDE( Ex.: [https://ia-tec-development.medium.com/lombok-como-instalar-na-spring-tool-suite-4-ide-48defb1d0eb9](https://ia-tec-development.medium.com/lombok-como-instalar-na-spring-tool-suite-4-ide-48defb1d0eb9) )

## Execução ▶️

Para executar o projeto, siga estas etapas:

1. Clone este repositório em sua máquina local
2. Abra o prompt de comando para executar o projeto via docker compose( Git Bash por exemplo )
3. Execute o seguinte comando: docker compose up --build
4. Após iniciar a aplicação, acesse a documentação do Swagger [http://localhost:8080/swagger-ui/index.html#/ContaController](http://localhost:8080/swagger-ui/index.html#/ContaController) para visualizar e testar os endpoints
5. Ao acessar o swagger, deve ser feita a autenticação antes de tentar acessar qualquer serviço
6. A autenticação é feita com a definição do usuário em memória. Por padrão, os valores são: usuario = user, senha = password

## Documentação da API 📖

Após iniciar a aplicação, você pode acessar a documentação da API por meio do Swagger. Abra seu navegador e navegue para [http://localhost:8080/swagger-ui/index.html#/ContaController](http://localhost:8080/swagger-ui/index.html#/ContaController).
Lá, você encontrará uma descrição do endpoint disponível, bem como a capacidade de testá-lo diretamente do navegador.

Para testar o endpoint importar-contas, você pode baixar o arquivo Arquivo_Contas.csv que deixei na pasta \src\test\resources do projeto cbyk-api e utilizá-lo.

## Contribuição 🤝

Contribuições são bem-vindas! Sinta-se à vontade para abrir um pull request para propor melhorias ou correções.

## Contato 📧

Para mais informações ou dúvidas, entre em contato com [edersonpatricio25@gmail.com](edersonpatricio25@gmail.com).
