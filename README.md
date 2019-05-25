##API REST desenvolvida para pretenção a vaga de desenvolvedor no banco Inter
###By Samuel Oliveira Chaves

###Leia me antes de prosseguir:

1. Os scripts iniciais são executados utilizando **liquibase**
2. Foi utilizado o banco de dados em memória **H2**
3. As entidades são versionadas para simples controle de concorrência utilizando o **Optimistic Lock** da **JPA**. Portanto, deve-se passar sempre o atributo version com a versão mais atual
4. Essa aplicação foi implementada no ECLIPSE e utiliza-se a biblioteca lombok(facilita geração de getters, seters, constructors, deixando a classe mais limpa). Para instalar o lombok no STS ou eclipse, baixe o jar em https://projectlombok.org/download e informe o executavel .exe do eclipse/STS. Foi utilizado o STS 4.2.1.RELEASE como IDE para construir a aplicação
5. Deve ter o MAVEN instalado para gerar o .jar e rodar os testes
6. Caso não tenha o JAVA_HOME configurado, deve ser apontado para a pasta de instalação do JDK (set JAVA_HOME="<DIRETORIO_JAVA>")
7. A autenticação e geração de token é realizada pelo JWT através do serviço /login passando as credenciais via JSON:
	1. Acesso ADMIN (GET,POST,PUT,DELETE): **{"email":"admin@bancointer.com.br","password":"acessoAdmin123"}**
	2. Acesso CONVIDADO (somente GET): **{"email":"somenteleitura@bancointer.com.br","password":"acessoConvidado123"}**
8. Acesse a documentação para mais detalhes em http://localhost:8080/swagger-ui.html#!/default/login (É preciso logar, pegar o token e utilizar nos acessos dos serviços)
9. Os endpoints podem ser acessados também pelo POSTMAN no link https://www.getpostman.com/collections/f882336d5cb856ef66e0, executando http://localhost:8080/login para ter o token geradado no HEADER Authorization(No postman utilize o token da collection, não precisa passar o 'Bearer ' pois o postman já inclui no inicio automaticamente)
###Testando a API
1. Vá para o diretório da aplicação
2. Corra o comando ./mvnw test

###Gerando o .jar da API
1. Vá para o diretório da aplicação
2. Corra o comando ./mvnw spring-boot:run