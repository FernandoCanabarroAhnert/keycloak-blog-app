package com.fernandocanabarro.blog_app.openapi;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.fernandocanabarro.blog_app.domain.dtos.newsapi.NewsApiNotice;
import com.fernandocanabarro.blog_app.domain.dtos.newsapi.NewsHub;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface NewsControllerOpenAPI {

    @Operation(
    description = "Consultar as Categorias Disponíveis de Notícias",
    summary = "Endpoint responsável por receber a requisição de Consultar as Categorias Disponíveis de Notícias",
    responses = {
        @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
        @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
   		}
	)
    ResponseEntity<NewsHub> getNoticesCategories();

    @Operation(
    description = "Consultar as Notícias mais Recentes, sem uma categoria em específico",
    summary = "Endpoint responsável por receber a requisição de Consultar as Notícias mais Recentes",
    responses = {
        @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
        @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
   		}
	)
    ResponseEntity<Page<NewsApiNotice>> getLatestNews();

    @Operation(
    description = "Consultar as Notícias mais Recentes de uma Dada Categoria",
    summary = "Endpoint responsável por receber a requisição de Consultar as Notícias mais Recentes de uma Categoria",
    responses = {
        @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
        @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
   		}
	)
    ResponseEntity<Page<NewsApiNotice>> getNoticesByCategory(@PathVariable String category);

    

    
}
