# Wishlist

O objetivo é que você desenvolva um serviço HTTP resolvendo a
funcionalidade de Wishlist do cliente. Esse serviço deve atender
os seguintes requisitos:
- Adicionar um produto na Wishlist do cliente;
- Remover um produto da Wishlist do cliente;
- Consultar todos os produtos da Wishlist do cliente;
- Consultar se um determinado produto está na Wishlist do cliente;

# Como rodar a aplicação

Criei um arquivo docker-compose.yml onde podemos subir o MongoDB e o Redis.

```
docker-compose up
```

Para rodar os testes e criar o jar:

```
mvn clean package
```


# Endpoints 

Para acessar a documentação os endpoints rest, acesse esse link:
http://localhost:8080/swagger-ui/index.html#/

# Decisões

## Estrutura do projeto

Criei a estrutura do projeto baseado em arquitetura limpa e hexagonal.
Como é um projeto pequeno, não criei muitas camadas e uma estrutura complexa de diretórios.

```
-- adapter
     | -- input 
            | -- controller
     | -- output
            | -- mongo      
-- domain
     | -- usecases
     | -- ports
     | -- exceptions
```

Inicialmente, dividi entre adapter e domain.
No pacote Adapter estão todo código de entrada e saída de dados.
Os endpoints estão na camada do input e controller. Esses endpoints chamam as classes useCases que estão na camada Domain.

Esses useCases podem chamar classes de domínio e da camada de adapter. Para acessar uma implementação na camada de apdater, os
serviços devem chamar a interface Port. A ideia é que cada interface port tenha pelo menos uma implementação.
No nosso caso, as interfaces Port são implementadas por classes Repository.
Por exemplo, temos o `WishlistPort`. A implementação dessa interface é `WishlistRepository`. `WishlistRepository` tem sua própria implementação 
de como adicionar o produto na wishlist e outras operações. A ideia é se um dia alterarmos o banco de dados, ou modificar a forma de buscar e salvar as informações,
a gente alteraria apenas a implementação da interface Port. Portanto, o código que está em domínio, teoricamente, não teria alterações.
Por isso, sempre que estamos passando dados entre as camadas Domain e Adapter, é interessante que se converta o objeto de dados.

O código do repositório de acesso ao mongo está na camada de output.

## System Design

![Image](arch-design.png)

## MongoDB

Eu escolhi MongoDB como base noSql, porque ele é ótimo para armazenar o tipo de estrutura de lista de string(a lista de productId),
e ele é altamente escalável e de alta disponibilidade.
Além disso, eu tenho muito conhecimento sobre essa base de dados. 

Outra base de dados que seria perfeita para esse caso seria uma base chave-valor(Key-value store), tais como cassandra ou dynamoDB.
Pois, o id do cliente seria a key, e a lista de id dos produtos seria o valor. Dessa forma, ficaria simples de pesquisar.
Lembrando que o Cassandra foi projetada para ter um alto volume de escrita com baixa latência, e o DynamoDB projetado para ter alto volume de leitura.

Um pouco mais da explicação de cada um deles:
https://medium.com/@rafaelmchaves/demystifying-database-types-relational-vs-non-relational-and-when-to-use-each-b68f1d8a9357

## Cache

Foi incluído cache na aplicação. Normalmente, uma wishlist é pouco alterada, mas bastante consultada, seja o cliente 
olhando se os produtos na wishlist abaixou, seja pela própria empresa para mandar Ads sobre os produtos na wishlist.
Por causa disso, adicionei cache distribuído a aplicação para que o sistema fique mais responsivo e com respostas mais rápidas, além
disso, reduzindo o throughput no banco de dados em disco. 
Escolhi o Redis como banco de dados de cache: ele está em memória e é um dos melhores banco de dados para cache disponíveis no mercado.

## Unit and integration tests

Eu criei testes unitários para as classes no pacote de domain e repository.

Criei testes integrados que consiga testar todas as integrações dos endpoints. Usei cucumber, onde criei as descrições 
dos testes usando Gherkin. Acredito que o BDD é uma ótima prática que visa melhorar o entendimento do comportamento da aplicação.
