# Projeto Backend: Plataforma de um Blog 🌐

![Java](https://img.shields.io/badge/java-FF5722.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-0B3D30?style=for-the-badge&logo=mongodb&logoColor=white)
![Mongo Express](https://img.shields.io/badge/Mongo%20Express-285C35?style=for-the-badge&logo=mongodb&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-0077B6?style=for-the-badge&logo=docker&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-8338EC?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-F80000?style=for-the-badge&logo=openid&logoColor=white)
![Keycloak](https://img.shields.io/badge/Keycloak-6A6A6A?style=for-the-badge&logo=keycloak&logoColor=white)
![SendGrid](https://img.shields.io/badge/SendGrid-00BFFF?style=for-the-badge&logo=maildotru&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)

## O que é o projeto?

Este é um projeto em que os usuários podem criar postagens, curtir postagens, comentar em postagens, seguir outros usuários e consultar notícias de uma [API externa](https://newsdata.io/), que contém diversas categorias, como esporte, política, tecnologia, etc. Eu criei este projeto com o intuito de colocar em prática algumas ferramentas/tecnologias novas que eu aprendi a utilizar, como o KeyCloak, que é um serviço de Autenticação e Autorização que segue o padrão do OAuth 2.0, que facilita o processo de efetuar login utilizando serviços de autenticação externo, como Google, Microsoft, etc. Neste projeto, eu permiti aos usuários poderem realizar login usando uma conta do Google, do Facebook ou do GitHub.

Além disso, eu também fiz uso de Bancos de Dados NoSQL, sendo eles: o banco de dados orientado a documentos MongoDB, que permite uma maior flexibilidade e modelagem de dados,além da ótima performance, e o banco de dados em memória Redis, que junto com o Spring Data Redisson, permite realizar consultas sem precisar ir e voltar no banco de dados (que gera latência na aplicação), garantindo uma melhor performance e rapidez para os métodos de consulta.

### Serviço RESTful 🚀

* Desenvolvimento de um serviço RESTful para toda a aplicação.

## Tecnologias 💻
 
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [MongoDB](https://www.mongodb.com/)
- [Mongo Express](https://alphasec.io/mongo-express-mongodb-management-made-easy/)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT](https://jwt.io/)
- [OAuth2](https://oauth.net/2/)
- [KeyCloak](https://www.keycloak.org/)
- [SendGrid](https://sendgrid.com/en-us)
- [SpringDoc OpenAPI 3](https://springdoc.org/v2/#spring-webflux-support)
- [Docker](https://www.docker.com/)
- [JUnit5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Jacoco](https://www.eclemma.org/jacoco/)
- [Bean Validation](https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html)
- [HATEOAS](https://spring.io/projects/spring-hateoas)

## Práticas adotadas ✨

- SOLID, DRY, YAGNI, KISS
- API REST
- Injeção de Dependências
- Testes Automatizados
- Geração automática do Swagger com a OpenAPI 3
- Uso de Banco de Dados NoSQL
- Autenticação e Autorização com KeyCloak
- Uso de Cache com Spring Data Redisson

## Como executar 🎉

1.Clonar repositório git:

```text
git clone https://github.com/FernandoCanabarroAhnert/keycloak-blog-app.git
```

2.Instalar dependências.

```text
mvn clean install
```

3.Executar a aplicação Spring Boot.

4.Testar endpoints através do Postman ou da url
<http://localhost:8080/swagger-ui/index.html#/>

### Usando Docker 🐳

- Clonar repositório git
- Construir o projeto:
```
./mvnw clean package
```
- Construir a imagem:
```
./mvnw spring-boot:build-image
```
- Executar o container:
```
docker run --name blog-app -p 8080:8080  -d blog-app:0.0.1-SNAPSHOT
```
## API Endpoints 📚

Para fazer as requisições HTTP abaixo, foi utilizada a ferramenta [Postman](https://www.postman.com/):
- Collection do Postman completa: [Postman-Collection](https://github.com/user-attachments/files/17087984/blog-app-PostmanCollection.json)

- Criar Post
```
$ http POST http://localhost:8080/posts


{
    "title": "Projeto de um Blog em Spring Boot",
    "text": "Nos últimos dias eu construí um Sistema de um Blog com Spring Boot,\n permitindo aos usuários criarem, consultarem, atualizarem e deletarem posts",
    "mediaUrl": "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.dio.me%2Farticles%2Fspring-boot-e-suas-dependencias&psig=AOvVaw2ldR_EYxaPlRbb5BnFlPRA&ust=1726513272211000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCOi-9KPRxYgDFQAAAAAdAAAAABAE",
    "tags": [
        "Spring Boot",
        "Back End",
        "Java",
        "KeyCloak",
        "API"
    ]
}


```

- Consultar Posts
```
$ http GET http://localhost:8080/posts


"content": [
        {
            "id": "66eecf44dac73b0a2759fd8f",
            "title": "Minha Incrível Viagem pela Itália",
            "text": "Recentemente, fiz uma viagem inesquecível pela Itália, visitando cidades como Roma, Florença e Veneza. Me encantei com a arquitetura histórica, a culinária deliciosa e a cultura rica de cada local.",
            "mediaUrl": "trip-to-italy",
            "tags": [
                "Viagem",
                "Itália",
                "Cultura",
                "Turismo",
                "Aventura"
            ],
            "author": {
                "id": "12bb9a07-40f6-43a9-8c6d-5ecfd8866616",
                "username": "fernandinho",
            },
            "moment": "21/09/2024 10:51",
            "lastUpdate": null,
            "likesCount": 0,
            "links": [
                {
                    "rel": "Consultar Post por Id",
                    "href": "http://localhost:8080/posts/66eecf44dac73b0a2759fd8f"
                }
            ]
        },
        {
            "id": "66eecf4edac73b0a2759fd90",
            "title": "Receita de Lasanha Caseira Perfeita",
            "text": "Hoje, vou compartilhar minha receita favorita de lasanha caseira. Com camadas de massa, molho bolonhesa, e muito queijo, essa lasanha é perfeita para um jantar em família ou com amigos.",
            "mediaUrl": "homemade-lasagna-recipe",
            "tags": [
                "Receita",
                "Lasanha",
                "Culinária",
                "Comida Italiana",
                "Gastronomia"
            ],
            "author": {
                "id": "12bb9a07-40f6-43a9-8c6d-5ecfd8866616",
                "username": "fernandinho",
            },
            "moment": "21/09/2024 10:51",
            "lastUpdate": null,
            "likesCount": 0,
            "links": [
                {
                    "rel": "Consultar Post por Id",
                    "href": "http://localhost:8080/posts/66eecf4edac73b0a2759fd90"
                }
            ]
        }
    ]


```

- Consultar Post por Id
```
$ http POST http://localhost:8080/posts/{id-do-post}


{
    "id": "66eecf58dac73b0a2759fd91",
    "title": "Diversão Garantida no Parque de Diversões",
    "text": "Passei o fim de semana em um parque de diversões e foi incrível! Montanhas-russas emocionantes, shows ao vivo e muita diversão para todas as idades. Foi uma experiência repleta de adrenalina e momentos inesquecíveis.",
    "mediaUrl": "amusement-park-experience",
    "tags": [
        "Diversão",
        "Parque de Diversões",
        "Aventura",
        "Lazer",
        "Família"
    ],
    "author": {
        "id": "12bb9a07-40f6-43a9-8c6d-5ecfd8866616",
        "username": "fernandinho"
    },
    "moment": "21/09/2024 10:51",
    "lastUpdate": null,
    "likesCount": 0,
    "_links": {
        "Consultar Comentários do Post": {
            "href": "http://localhost:8080/posts/66eecf58dac73b0a2759fd91/comments"
        },
        "Consultar Likes do Post": {
            "href": "http://localhost:8080/posts/66eecf58dac73b0a2759fd91/likes"
        },
        "Interagir com o Post": {
            "href": "http://localhost:8080/posts/66eecf58dac73b0a2759fd91/interact"
        },
        "Comentar o Post": {
            "href": "http://localhost:8080/comments/66eecf58dac73b0a2759fd91"
        },
        "Consultar Perfil do Autor": {
            "href": "http://localhost:8080/users/12bb9a07-40f6-43a9-8c6d-5ecfd8866616"
        }
    }
}


```

- Comentar em um Post
```
$ http POST http://localhost:8080/comments/{id-do-post}

{
    "text": "Bom Trabalho!"
}
```

- Consultar o perfil do Usuário Logado
```
$ http GET http://localhost:8080/users/me

{
    "id": "12bb9a07-40f6-43a9-8c6d-5ecfd8866616",
    "fullName": "Fernando Canabarro",
    "username": "fernandinho",
    "email": "fernando@gmail.com",
    "numberOfPosts": 5,
    "followers": 2,
    "following": 1,
    "_links": {
        "Consultar meus Posts": {
            "href": "http://localhost:8080/posts/myPosts"
        }
    }
}

```

- Consultar Notícias de uma Categoria
```
$ http GET http://localhost:8080/news/{categoria}

-exemplo: sports

"content": [
        {
            "title": "Paquetá passa em branco, Chelsea 'atropela' o West Ham e encosta na parte de cima da Premier League",
            "link": "https://www.espn.com.br/futebol/premier-league/artigo/_/id/14201565/paqueta-passa-branco-chelsea-atropela-west-ham-encosta-parte-cima-premier-league",
            "description": "Destaque do jogo, Nicolas Jackson anotou dois gols e ainda deu assistência",
            "pubDate": "2024-09-21 13:59:05",
            "image_url": null,
            "source_name": "Espn",
            "source_url": "https://www.espn.com.br",
            "category": [
                "sports"
            ]
        },

        {
            "title": "Cruzeiro visita o Cuiabá neste domingo e busca encostar no G4 do Brasileirão",
            "link": "https://www.terra.com.br/esportes/futebol/cruzeiro-visita-o-cuiaba-neste-domingo-e-busca-encostar-no-g4-do-brasileirao,10e35aa16fd6c671d38cff32434a8a37vcgl5eeg.html",
            "description": "Neste domingo (22), às 18h30, o Cruzeiro visita o Cuiabá para o duelo da 27a rodada da Série A ...",
            "pubDate": "2024-09-21 13:41:11",
            "image_url": "https://p2.trrsf.com/image/fget/cf/1200/630/middle/images.terra.com/2024/09/21/303897046-cru.jpg",
            "source_name": "Terra",
            "source_url": "https://www.terra.com.br",
            "category": [
                "sports"
            ]
        }
    ]

```


