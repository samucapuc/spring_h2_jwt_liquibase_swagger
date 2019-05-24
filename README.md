# Português

##API REST desenvolvida para pretenção a vaga de desenvolvedor no banco Inter
###By Samuel Oliveira Chaves

###Antes de executar a aplicação é preciso levar em conta:

1. Os scripts iniciais são executados utilizando **liquibase**
2. Foi utilizado o banco de dados em memória **H2**
3. As entidades são versionadas para controle de concorrência utilizando o **Optimistic Lock** da **JPA**. Portanto, deve-se passar sempre o atributo version com a versão mais atual
4. Essa aplicação foi implementada no ECLIPSE e utiliza-se a biblioteca lombok. Para instalar o lombok no STS, baixe o jar em https://projectlombok.org/download e informe o executavel .exe do eclipse
5. Deve ter o MAVEN instalado para gerar o .jar e rodar os testes
6. Caso não tenha o JAVA_HOME configurado, deve ser apontado para a pasta de instalação do JRE (set JAVA_HOME="<DIRETORIO_JAVA>")

###Testando a API
1. Vá para o diretório da aplicação
2. Corra o comando ./mvnw test

###Gerando o .jar da API
1. Vá para o diretório da aplicação
2. Corra o comando ./mvnw spring-boot:run

# English

## REST API developed to claim the developer spot at Inter bank
### By Samuel Oliveira Chaves

### Before running the application, you must take into account:

1. The initial scripts are executed using ** liquibase **
2. The memory database ** H2 ** was used
3. Entities are versioned for competition control using the ** Optimistic Lock ** of ** JPA **. Therefore, you should always pass the version attribute to the most current version
4. This application has been implemented in ECLIPSE and the lombok library is used. To install lombok on STS, download the jar at https://projectlombok.org/download and tell the eclipse executable .exe
5. Must have MAVEN installed to generate the .jar and run the tests
6. If you do not have JAVA_HOME configured, it should be pointed to the JRE installation folder (set JAVA_HOME = "<JAVA DIRECTORY>")

### Testing the API
1. Go to the application directory
2. Run the ./mvnw test command.

### Generating the API .jar
1. Go to the application directory
2. Run the command ./mvnw spring-boot: run