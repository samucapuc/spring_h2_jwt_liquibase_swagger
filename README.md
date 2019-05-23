# API REST desenvolvida para pretenção a vaga de desenvolvedor no banco Inter
#By Samuel Oliveira Chaves

##Antes de executar a aplicação é preciso levar em conta:

<ol>
<ul> Os scripts iniciais são executados utilizando <b>liquibase</b>;</ul>
<ul> O banco de dados é em memória utilizando <b>H2</b></ul>
<ul> As entidades são versionadas para controle de concorrência utilizando o <b>ObjectOptimisticLock</b> do <b>JPA</b>. Portanto, deve-se passar sempre o atributo version com a versão mais atual</ul>
<ul> Essa aplicação foi implementada no ECLIPSE e utiliza-se a biblioteca lombok, para instalar o lombok no STS, baixe o jar em https://projectlombok.org/download e informe o executavel .exe do eclipse</ul>
</ol>