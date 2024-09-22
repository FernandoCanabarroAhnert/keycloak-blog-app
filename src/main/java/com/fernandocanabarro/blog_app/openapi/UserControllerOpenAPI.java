package com.fernandocanabarro.blog_app.openapi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fernandocanabarro.blog_app.domain.dtos.AuthorDTO;
import com.fernandocanabarro.blog_app.domain.dtos.PostResponseDTO;
import com.fernandocanabarro.blog_app.domain.dtos.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UserControllerOpenAPI {

    @Operation(
    description = "Consultar Posts de um dado usuário",
    summary = "Endpoint responsável por receber a requisição de consultar os posts de um usuário",
    responses = {
         @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
         @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
         @ApiResponse(description = "Usuário Não Encontrado", responseCode = "404"),
   		}
	)
    ResponseEntity<Page<PostResponseDTO>> getPostsByAuthor(Pageable pageable,@PathVariable String userId);

    @Operation(
    description = "Seguir/Parar de Seguir um Usuário",
    summary = "Endpoint responsável por receber a requisição de Seguir/Parar de Seguir um Usuário",
    responses = {
         @ApiResponse(description = "Usuário foi adicionado/removido da lista de usuários seguindo", responseCode = "200"),
         @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
         @ApiResponse(description = "Usuário Não Encontrado", responseCode = "404"),
   		}
	)
    ResponseEntity<UserDTO> interactWithUser(@PathVariable String userId,HttpServletRequest request);

    @Operation(
        description = "Consultar o perfil de um usuário",
        summary = "Endpoint responsável por receber a requisição de Consultar o perfil de um usuário",
        responses = {
             @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
             @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401"),
             @ApiResponse(description = "Usuário Não Encontrado", responseCode = "404"),
        }
    )
    ResponseEntity<UserDTO> getUserById(@PathVariable String userId);

    @Operation(
        description = "Consultar o Perfil do Usuário Logado",
        summary = "Endpoint responsável por receber a requisição de Consultar o Perfil do Usuário Logado",
        responses = {
             @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
             @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401")
        }
    )
    ResponseEntity<UserDTO> getMe(HttpServletRequest request);

    @Operation(
        description = "Consultar Usuários pelo Username",
        summary = "Endpoint responsável por receber a requisição de Consultar Usuários pelo Username",
        responses = {
             @ApiResponse(description = "Consulta Realizada", responseCode = "200"),
             @ApiResponse(description = "Quando um Usuário não logado faz a requisição", responseCode = "401")
        }
    )
    ResponseEntity<Page<AuthorDTO>> searchUsersByUsername(@RequestParam(name = "username",defaultValue = "") String username,Pageable pageable);
}
