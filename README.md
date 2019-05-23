API REST desenvolvida para pretenção a vaga de desenvolvedor no banco Inter
By Samuel Oliveira Chaves

Antes de executar a aplicação é preciso levar em conta:

1) Os scripts iniciais são executados utilizando liquibase;
2) O banco de dados é em memória utilizando H2
3) As entidades são versionadas para controle de concorrência utilizando o ObjectOptimisticLock do JPA. Portanto, deve-se passar sempre o atributo version com a versão mais atual