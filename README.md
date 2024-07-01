# Wishlist

O objetivo é que você desenvolva um serviço HTTP resolvendo a
funcionalidade de Wishlist do cliente. Esse serviço deve atender
os seguintes requisitos:
- Adicionar um produto na Wishlist do cliente;
- Remover um produto da Wishlist do cliente;
- Consultar todos os produtos da Wishlist do cliente;
- Consultar se um determinado produto está na Wishlist do
  cliente;

# Como rodar a aplicação

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

## Cache

## Prometheus and Grafana

## Unit and integration tests