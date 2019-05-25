##API REST desenvolvida para pretenção a vaga de desenvolvedor no banco Inter
###By Samuel Oliveira Chaves

###Antes de executar a aplicação é preciso levar em conta:

1. Os scripts iniciais são executados utilizando **liquibase**
2. Foi utilizado o banco de dados em memória **H2**
3. As entidades são versionadas para controle de concorrência utilizando o **Optimistic Lock** da **JPA**. Portanto, deve-se passar sempre o atributo version com a versão mais atual
4. Essa aplicação foi implementada no ECLIPSE e utiliza-se a biblioteca lombok. Para instalar o lombok no STS, baixe o jar em https://projectlombok.org/download e informe o executavel .exe do eclipse
5. Deve ter o MAVEN instalado para gerar o .jar e rodar os testes
6. Caso não tenha o JAVA_HOME configurado, deve ser apontado para a pasta de instalação do JRE (set JAVA_HOME="<DIRETORIO_JAVA>")
7. A autenticação é realizada através de token gerado pelo JWT através do serviço /login passando as credenciais via JSON:
	1. Para acesso ADMIN (POST,PUT,DELETE):
		> {
			"email":"admin@bancointer.com.br","password":"acessoAdmin123"
		> }
	2. Para acesso CONVIDADO (GET):
		> {
			"email":"somenteleitura@bancointer.com.br","password":"acessoConvidado123"
		> }	
8. Acesse a documentação para mais detalhes em http://localhost:8080/swagger-ui.html#!/default/login (Preciso logar, pegar o token e utilizar nos acessos dos serviços)
###Testando a API
1. Vá para o diretório da aplicação
2. Corra o comando ./mvnw test

###Gerando o .jar da API
1. Vá para o diretório da aplicação
2. Corra o comando ./mvnw spring-boot:run