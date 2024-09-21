package com.fernandocanabarro.blog_app.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.blog_app.domain.dtos.CommentRequestDTO;
import com.fernandocanabarro.blog_app.domain.dtos.CommentResponseDTO;
import com.fernandocanabarro.blog_app.domain.entities.Comment;
import com.fernandocanabarro.blog_app.domain.entities.Post;
import com.fernandocanabarro.blog_app.domain.entities.User;
import com.fernandocanabarro.blog_app.repositories.CommentRepository;
import com.fernandocanabarro.blog_app.repositories.PostRepository;
import com.fernandocanabarro.blog_app.services.exceptions.ForbiddenException;
import com.fernandocanabarro.blog_app.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Transactional
    public CommentResponseDTO createComment(String postId, CommentRequestDTO dto, HttpServletRequest request) {
        Comment comment = new Comment();
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(postId));
        commentMapper(comment, postId, dto, request);
        comment = commentRepository.save(comment);
        post.addComment(comment);
        return new CommentResponseDTO(comment);
    }

    private void commentMapper(Comment comment, String postId, CommentRequestDTO dto, HttpServletRequest request) {
        comment.setAuthor(userService.getConnectedUser(request));
        comment.setText(dto.getText());
        comment.setMoment(Instant.now());
        comment.setLastUpdate(null);
        comment.setPostId(postId);
    }

    @Transactional
    public CommentResponseDTO update(String id, CommentRequestDTO dto, HttpServletRequest request) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        User user = userService.getConnectedUser(request);
        Post post = postRepository.findById(comment.getPostId()).get();
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new ForbiddenException("Somente o Autor do Comentário pode Alterá-lo");
        }
        comment.setText(dto.getText());
        comment.setLastUpdate(Instant.now());
        comment = commentRepository.save(comment);
        postRepository.save(post);
        return new CommentResponseDTO(comment);
    }

    @Transactional
    public void deleteById(String id, HttpServletRequest request) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        User user = userService.getConnectedUser(request);
        Post post = postRepository.findById(comment.getPostId()).get();
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new ForbiddenException("Somente o Autor do Comentário pode Excluí-lo");
        }
        post.getComments().remove(comment);
        commentRepository.delete(comment);
        postRepository.save(post);
    }
}
