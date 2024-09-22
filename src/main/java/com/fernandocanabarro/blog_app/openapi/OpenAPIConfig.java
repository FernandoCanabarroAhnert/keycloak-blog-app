package com.fernandocanabarro.blog_app.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
            .info(new Info()
                .title("Projeto Pessoal: Blog")
                .version("FernandoCanabarroAhnert")
                .description("Este é um projeto em que os usuários podem criar postagens, curtir postagens, comentar em postagens, seguir outros usuários e consultar notícias de uma API externa, que contém diversas categorias, como esporte, política, tecnologia, etc."));
    }
}
