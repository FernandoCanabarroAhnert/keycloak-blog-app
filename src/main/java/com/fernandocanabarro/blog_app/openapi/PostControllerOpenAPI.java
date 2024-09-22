package com.fernandocanabarro.blog_app.openapi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fernandocanabarro.blog_app.domain.dtos.CommentResponseDTO;
import com.fernandocanabarro.blog_app.domain.dtos.LikeDTO;
import com.fernandocanabarro.blog_app.domain.dtos.PostRequestDTO;
import com.fernandocanabarro.blog_app.domain.dtos.PostResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface PostControllerOpenAPI {

    @Operation(
    description = "Consultar Posts",
    summary = "Endpoint responsável por receber a requisição de consultar posts",
    responses = {
        @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
        @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
   		}
	)
    ResponseEntity<Page<PostResponseDTO>> getPosts(Pageable pageable,@RequestParam(name = "tag", defaultValue = "") String tag);

    @Operation(
    description = "Consultar Post por Id",
    summary = "Endpoint responsável por receber a requisição de consultar post por Id",
    responses = {
        @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
        @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
        @ApiResponse(description = "Post não Encontrado", responseCode = "404")
   		}
	)
    ResponseEntity<PostResponseDTO> getPostById(@PathVariable String postId);

    @Operation(
        description = "Consultar Posts do Usuário Logado",
        summary = "Endpoint responsável por receber a requisição de Consultar Posts do Usuário Logado",
        responses = {
            @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
            @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
        }
    )
    ResponseEntity<Page<PostResponseDTO>> getMyPosts(Pageable pageable,HttpServletRequest request);

    @Operation(
        description = "Consultar Posts que o Usuário Logado deu Like",
        summary = "Endpoint responsável por receber a requisição de Consultar Posts que o Usuário Logado deu Like",
        responses = {
            @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
            @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
        }
    )
    ResponseEntity<List<PostResponseDTO>> getMyLikedPosts(HttpServletRequest request);

    @Operation(
        description = "Consultar os Comentários de um Post",
        summary = "Endpoint responsável por receber a requisição de Consultar os Comentários de um Post",
        responses = {
            @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
            @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
            @ApiResponse(description = "Post não Encontrado", responseCode = "404")
        }
    )
    ResponseEntity<Page<CommentResponseDTO>> findCommentsByPostId(@PathVariable String postId,Pageable pageable);

    @Operation(
        description = "Consultar os Likes de um Post",
        summary = "Endpoint responsável por receber a requisição de Consultar os Likes de um Post",
        responses = {
            @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
            @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
            @ApiResponse(description = "Post não Encontrado", responseCode = "404")
        }
    )
    ResponseEntity<Page<LikeDTO>> getLikesByPostId(@PathVariable String postId,Pageable pageable);

    @Operation(
        description = "Criar um Post",
        summary = "Endpoint responsável por receber a requisição de Criar um Post",
        responses = {
            @ApiResponse(description = "Post Criado", responseCode = "201"),
            @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
            @ApiResponse(description = "Algum Dado do Corpo da Requisição está inválido", responseCode = "422")
        }
    )
    ResponseEntity<PostResponseDTO> createPost(@RequestBody @Valid PostRequestDTO dto,HttpServletRequest request);

    @Operation(
        description = "Atualizar um Post",
        summary = "Endpoint responsável por receber a requisição de Atualizar um Post",
        responses = {
            @ApiResponse(description = "Post Atualizado", responseCode = "200"),
            @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
            @ApiResponse(description = "Quando um Usuário que não é o Autor do Post tenta atualizá-lo", responseCode = "403"),
            @ApiResponse(description = "Post não Encontrado", responseCode = "404"),
            @ApiResponse(description = "Algum Dado do Corpo da Requisição está inválido", responseCode = "422")
        }
    )
    ResponseEntity<PostResponseDTO> update(@PathVariable String postId,@RequestBody @Valid PostRequestDTO dto,HttpServletRequest request);

    @Operation(
        description = "Deletar um Post",
        summary = "Endpoint responsável por receber a requisição de Deletar um Post",
        responses = {
            @ApiResponse(description = "Post Deletado", responseCode = "204"),
            @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
            @ApiResponse(description = "Quando um Usuário que não é o Autor do Post tenta deletá-lo", responseCode = "403"),
            @ApiResponse(description = "Post não Encontrado", responseCode = "404")
        }
    )
    ResponseEntity<Void> delete(@PathVariable String postId,HttpServletRequest request);

    @Operation(
        description = "Curtir/Descurtir um Post",
        summary = "Endpoint responsável por receber a requisição de Curtir/Descurtir um Post",
        responses = {
            @ApiResponse(description = "Post Curtido/Descurtido", responseCode = "200"),
            @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
            @ApiResponse(description = "Post não Encontrado", responseCode = "404")
        }
    )
    ResponseEntity<PostResponseDTO> interactWithPost(@PathVariable String postId,HttpServletRequest request);

    
}
