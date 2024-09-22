package com.fernandocanabarro.blog_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fernandocanabarro.blog_app.domain.dtos.AuthorDTO;
import com.fernandocanabarro.blog_app.domain.dtos.PostResponseDTO;
import com.fernandocanabarro.blog_app.domain.dtos.UserDTO;
import com.fernandocanabarro.blog_app.openapi.UserControllerOpenAPI;
import com.fernandocanabarro.blog_app.services.PostService;
import com.fernandocanabarro.blog_app.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController implements UserControllerOpenAPI{

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPostsByAuthor(Pageable pageable,@PathVariable String userId){
        Page<PostResponseDTO> page = postService.getPostsByAuthor(pageable,userId);
        return ResponseEntity.ok(page);   
    }

    @PutMapping("/{userId}/interact")
    public ResponseEntity<UserDTO> interactWithUser(@PathVariable String userId,HttpServletRequest request){
        UserDTO user = userService.interactWithUser(userId,request);
        return ResponseEntity.ok(user);   
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId){
        UserDTO obj = userService.findById(userId);
        return ResponseEntity.ok(obj);   
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe(HttpServletRequest request){
        UserDTO obj = userService.getMe(request);
        return ResponseEntity.ok(obj);   
    }

    @GetMapping
    public ResponseEntity<Page<AuthorDTO>> searchUsersByUsername(@RequestParam(name = "username",defaultValue = "") String username,Pageable pageable){
        Page<AuthorDTO> page = userService.searchUsersByUsername(username,pageable);
        return ResponseEntity.ok(page);   
    }

}
