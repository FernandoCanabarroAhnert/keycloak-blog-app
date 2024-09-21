package com.fernandocanabarro.blog_app.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.blog_app.controllers.CommentController;
import com.fernandocanabarro.blog_app.controllers.PostController;
import com.fernandocanabarro.blog_app.controllers.UserController;
import com.fernandocanabarro.blog_app.domain.dtos.CommentResponseDTO;
import com.fernandocanabarro.blog_app.domain.dtos.LikeDTO;
import com.fernandocanabarro.blog_app.domain.dtos.PostRequestDTO;
import com.fernandocanabarro.blog_app.domain.dtos.PostResponseDTO;
import com.fernandocanabarro.blog_app.domain.entities.Like;
import com.fernandocanabarro.blog_app.domain.entities.Post;
import com.fernandocanabarro.blog_app.domain.entities.User;
import com.fernandocanabarro.blog_app.repositories.CommentRepository;
import com.fernandocanabarro.blog_app.repositories.LikeRepository;
import com.fernandocanabarro.blog_app.repositories.PostRepository;
import com.fernandocanabarro.blog_app.repositories.UserRepository;
import com.fernandocanabarro.blog_app.services.cache.PostServiceTemplate;
import com.fernandocanabarro.blog_app.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class PostService {

    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostServiceTemplate postServiceTemplate;

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> getPosts(Pageable pageable, String tag) {
        return (tag.isBlank()) ? postRepository.findAll(pageable).map(x -> new PostResponseDTO(x)
                    .add(linkTo(methodOn(PostController.class).getPostById(x.getId())).withRel("Consultar Post por Id")))
                : postRepository.findByTags(tag, pageable).map(x -> new PostResponseDTO(x)
                    .add(linkTo(methodOn(PostController.class).getPostById(x.getId())).withRel("Consultar Post por Id")));
    }

    @Transactional(readOnly = true)
    public PostResponseDTO findById(String postId) {
        PostResponseDTO post = postServiceTemplate.findById(postId);
        return post
                .add(linkTo(methodOn(PostController.class).findCommentsByPostId(postId, null)).withRel("Consultar Comentários do Post"))
                .add(linkTo(methodOn(PostController.class).getLikesByPostId(postId, null)).withRel("Consultar Likes do Post"))
                .add(linkTo(methodOn(PostController.class).interactWithPost(postId, null)).withRel("Interagir com o Post"))
                .add(linkTo(methodOn(CommentController.class).createComment(postId, null, null)).withRel("Comentar o Post"))
                .add(linkTo(methodOn(UserController.class).getUserById(post.getAuthor().getId())).withRel("Consultar Perfil do Autor"));
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> getPostsByAuthor(Pageable pageable, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(userId));
        return postRepository.findByUser(user.getId(), pageable).map(x -> new PostResponseDTO(x)
            .add(linkTo(methodOn(PostController.class).getPostById(x.getId())).withRel("Consultar Post por Id")));
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> getMyPosts(Pageable pageable, HttpServletRequest request) {
        User user = userService.getConnectedUser(request);
        return postRepository.findByUser(user.getId(), pageable).map(x -> new PostResponseDTO(x)
            .add(linkTo(methodOn(PostController.class).getPostById(x.getId())).withRel("Consultar Post por Id")));
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getMyLikedPosts(HttpServletRequest request) {
        User user = userService.getConnectedUser(request);
        return user.getLikedPosts().stream().map(x -> new PostResponseDTO(x)
            .add(linkTo(methodOn(PostController.class).getPostById(x.getId())).withRel("Consultar Post por Id")))
            .toList();
    }

    @Transactional(readOnly = true)
    public Page<CommentResponseDTO> findCommentsByPostId(String postId, Pageable pageable) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(postId));
        return commentRepository.findByPostId(post.getId(), pageable).map(x -> new CommentResponseDTO(x)
            .add(linkTo(methodOn(UserController.class).getUserById(x.getAuthor().getId())).withRel("Consultar Perfil do Autor")));
    }

    @Transactional(readOnly = true)
    public Page<LikeDTO> getLikesByPostId(String postId, Pageable pageable) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(postId));
        return likeRepository.findByPostId(post.getId(), pageable).map(x -> new LikeDTO(x)
            .add(linkTo(methodOn(UserController.class).getUserById(x.getUser().getId())).withRel("Consultar Perfil do Autor")));
    }

    public PostResponseDTO insert(PostRequestDTO dto, HttpServletRequest request) {
        PostResponseDTO response =  postServiceTemplate.createPost(dto, request);
        return response
            .add(linkTo(methodOn(PostController.class).findCommentsByPostId(response.getId(), null)).withRel("Consultar Comentários do Post"))
            .add(linkTo(methodOn(PostController.class).getLikesByPostId(response.getId(), null)).withRel("Consultar Likes do Post"))
            .add(linkTo(methodOn(PostController.class).interactWithPost(response.getId(), null)).withRel("Interagir com o Post"))
            .add(linkTo(methodOn(CommentController.class).createComment(response.getId(), null, null)).withRel("Comentar o Post"))
            .add(linkTo(methodOn(UserController.class).getUserById(response.getAuthor().getId())).withRel("Consultar Perfil do Autor"));
    }

    public PostResponseDTO update(String id, HttpServletRequest request, PostRequestDTO dto) {
        PostResponseDTO response = postServiceTemplate.update(id, dto, request);
        return response
            .add(linkTo(methodOn(PostController.class).findCommentsByPostId(response.getId(), null)).withRel("Consultar Comentários do Post"))
            .add(linkTo(methodOn(PostController.class).getLikesByPostId(response.getId(), null)).withRel("Consultar Likes do Post"))
            .add(linkTo(methodOn(PostController.class).interactWithPost(response.getId(), null)).withRel("Interagir com o Post"))
            .add(linkTo(methodOn(CommentController.class).createComment(response.getId(), null, null)).withRel("Comentar o Post"))
            .add(linkTo(methodOn(UserController.class).getUserById(response.getAuthor().getId())).withRel("Consultar Perfil do Autor"));
    }

    @Transactional
    public void deleteById(String id, HttpServletRequest request) {
        postServiceTemplate.delete(id, request);
    }

    @Transactional
    public PostResponseDTO interactWithPost(String postId, HttpServletRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(postId));
        User user = userService.getConnectedUser(request);
        if (!user.hasLikedPost(post)) {
            return likePost(user, post);
        }
        return dislikePost(user,post);
    }

    @Transactional
    public PostResponseDTO likePost(User user, Post post) {
        Like like = new Like();
        like.setUser(user);
        like.setPostId(post.getId());
        like = likeRepository.save(like);
        post.addLike(like);
        user.addLikedPost(post);
        postRepository.save(post);
        userRepository.save(user);
        return new PostResponseDTO(post)
            .add(linkTo(methodOn(PostController.class).findCommentsByPostId(post.getId(), null)).withRel("Consultar Comentários do Post"))
            .add(linkTo(methodOn(PostController.class).getLikesByPostId(post.getId(), null)).withRel("Consultar Likes do Post"))
            .add(linkTo(methodOn(PostController.class).interactWithPost(post.getId(), null)).withRel("Interagir com o Post"))
            .add(linkTo(methodOn(CommentController.class).createComment(post.getId(), null, null)).withRel("Comentar o Post"))
            .add(linkTo(methodOn(UserController.class).getUserById(post.getUser().getId())).withRel("Consultar Perfil do Autor"));
    }

    @Transactional
    private PostResponseDTO dislikePost(User user, Post post) {
        Like like = likeRepository.findByUserIdAndPostId(user.getId(), post.getId());
        post.removeLike(like);
        user.removeLikedPost(post);
        likeRepository.delete(like);
        userRepository.save(user);
        postRepository.save(post);
        return new PostResponseDTO(post)
            .add(linkTo(methodOn(PostController.class).findCommentsByPostId(post.getId(), null)).withRel("Consultar Comentários do Post"))
            .add(linkTo(methodOn(PostController.class).getLikesByPostId(post.getId(), null)).withRel("Consultar Likes do Post"))
            .add(linkTo(methodOn(PostController.class).interactWithPost(post.getId(), null)).withRel("Interagir com o Post"))
            .add(linkTo(methodOn(CommentController.class).createComment(post.getId(), null, null)).withRel("Comentar o Post"))
            .add(linkTo(methodOn(UserController.class).getUserById(post.getUser().getId())).withRel("Consultar Perfil do Autor"));
    }

}
