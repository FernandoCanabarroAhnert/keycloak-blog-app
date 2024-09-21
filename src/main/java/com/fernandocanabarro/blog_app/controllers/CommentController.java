package com.fernandocanabarro.blog_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fernandocanabarro.blog_app.domain.dtos.CommentRequestDTO;
import com.fernandocanabarro.blog_app.domain.dtos.CommentResponseDTO;
import com.fernandocanabarro.blog_app.services.CommentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService service;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable String postId,@Valid @RequestBody CommentRequestDTO dto, HttpServletRequest request){
        CommentResponseDTO obj = service.createComment(postId, dto, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> update(@PathVariable String id,@Valid @RequestBody CommentRequestDTO dto, HttpServletRequest request){
        CommentResponseDTO obj = service.update(id, dto, request);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id,HttpServletRequest request){
        service.deleteById(id, request);
        return ResponseEntity.noContent().build();
    }
}
