package com.fernandocanabarro.blog_app.openapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.fernandocanabarro.blog_app.domain.dtos.CommentRequestDTO;
import com.fernandocanabarro.blog_app.domain.dtos.CommentResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface CommentControllerOpenAPI {

    @Operation(
    description = "Comentar em um Post",
    summary = "Endpoint responsável por receber a requisição de Comentar em um Post",
    responses = {
        @ApiResponse(description = "Comentário Criado", responseCode = "201"),
        @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
        @ApiResponse(description = "Post não Encontrado", responseCode = "404"),
        @ApiResponse(description = "Algum Dado do Corpo da Requisição está inválido", responseCode = "422")
   		}
	)
    ResponseEntity<CommentResponseDTO> createComment(@PathVariable String postId,@Valid @RequestBody CommentRequestDTO dto, HttpServletRequest request);

    @Operation(
    description = "Atualizar um Comentário de um Post",
    summary = "Endpoint responsável por receber a requisição de Atualizar um Comentário de um Post",
    responses = {
        @ApiResponse(description = "Comentário Atualizado", responseCode = "200"),
        @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
        @ApiResponse(description = "Quando um Usuário que não é o Autor do Comentário tenta atualizá-lo", responseCode = "403"),
        @ApiResponse(description = "Post não Encontrado", responseCode = "404"),
        @ApiResponse(description = "Algum Dado do Corpo da Requisição está inválido", responseCode = "422")
   		}
	)
    ResponseEntity<CommentResponseDTO> update(@PathVariable String id,@Valid @RequestBody CommentRequestDTO dto, HttpServletRequest request);

    @Operation(
    description = "Deletar um Comentário de um Post",
    summary = "Endpoint responsável por receber a requisição de Deletar um Comentário de um Post",
    responses = {
        @ApiResponse(description = "Comentário Deletado", responseCode = "204"),
        @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
        @ApiResponse(description = "Quando um Usuário que não é o Autor do Comentário tenta deletá-lo", responseCode = "403"),
        @ApiResponse(description = "Post não Encontrado", responseCode = "404"),
   		}
	)
    ResponseEntity<Void> delete(@PathVariable String id,HttpServletRequest request);

    

    
}
