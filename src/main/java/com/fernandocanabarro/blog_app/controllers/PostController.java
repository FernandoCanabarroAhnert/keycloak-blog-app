package com.fernandocanabarro.blog_app.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fernandocanabarro.blog_app.domain.dtos.CommentResponseDTO;
import com.fernandocanabarro.blog_app.domain.dtos.LikeDTO;
import com.fernandocanabarro.blog_app.domain.dtos.PostRequestDTO;
import com.fernandocanabarro.blog_app.domain.dtos.PostResponseDTO;
import com.fernandocanabarro.blog_app.services.PostService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService service;

    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getPosts(Pageable pageable,@RequestParam(name = "tag", defaultValue = "") String tag){
        Page<PostResponseDTO> page = service.getPosts(pageable, tag);
        return ResponseEntity.ok(page);   
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable String postId){
        PostResponseDTO obj = service.findById(postId);
        return ResponseEntity.ok(obj);   
    }

    @GetMapping("/myPosts")
    public ResponseEntity<Page<PostResponseDTO>> getMyPosts(Pageable pageable,HttpServletRequest request){
        Page<PostResponseDTO> page = service.getMyPosts(pageable,request);
        return ResponseEntity.ok(page);   
    }

    @GetMapping("/likedPosts")
    public ResponseEntity<List<PostResponseDTO>> getMyLikedPosts(HttpServletRequest request){
        List<PostResponseDTO> list = service.getMyLikedPosts(request);
        return ResponseEntity.ok(list);   
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<CommentResponseDTO>> findCommentsByPostId(@PathVariable String postId,Pageable pageable){
        Page<CommentResponseDTO> page = service.findCommentsByPostId(postId,pageable);
        return ResponseEntity.ok(page);   
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<Page<LikeDTO>> getLikesByPostId(@PathVariable String postId,Pageable pageable){
        Page<LikeDTO> page = service.getLikesByPostId(postId,pageable);
        return ResponseEntity.ok(page);   
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody @Valid PostRequestDTO dto,HttpServletRequest request){
        PostResponseDTO obj = service.insert(dto, request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable String postId,@RequestBody @Valid PostRequestDTO dto,HttpServletRequest request){
        PostResponseDTO obj = service.update(postId,request,dto);
        return ResponseEntity.ok(obj);   
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable String postId,HttpServletRequest request){
        service.deleteById(postId,request);
        return ResponseEntity.noContent().build(); 
    }

    @PutMapping("/{postId}/interact")
    public ResponseEntity<PostResponseDTO> interactWithPost(@PathVariable String postId,HttpServletRequest request){
        PostResponseDTO obj = service.interactWithPost(postId, request);
        return ResponseEntity.ok(obj);   
    }

   
}
