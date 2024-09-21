package com.fernandocanabarro.blog_app.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fernandocanabarro.blog_app.domain.dtos.CommentRequestDTO;
import com.fernandocanabarro.blog_app.domain.dtos.CommentResponseDTO;
import com.fernandocanabarro.blog_app.domain.entities.Comment;
import com.fernandocanabarro.blog_app.domain.entities.Post;
import com.fernandocanabarro.blog_app.domain.entities.User;
import com.fernandocanabarro.blog_app.factories.CommentFactory;
import com.fernandocanabarro.blog_app.factories.PostFactory;
import com.fernandocanabarro.blog_app.factories.UserFactory;
import com.fernandocanabarro.blog_app.repositories.CommentRepository;
import com.fernandocanabarro.blog_app.repositories.PostRepository;
import com.fernandocanabarro.blog_app.services.exceptions.ForbiddenException;
import com.fernandocanabarro.blog_app.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {

    @InjectMocks
    private CommentService commentService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;

    private String existingId;
    private String nonExistingId;
    private Comment comment;
    private CommentRequestDTO requestDTO;
    private Post post;
    private User user;

    @BeforeEach
    public void setup(){
        existingId = "1";
        nonExistingId = "2";
        comment = CommentFactory.getComment();
        requestDTO = CommentFactory.getCommentRequestDTO();
        post = PostFactory.getPost();
        user = UserFactory.getUser();
    }

    @Test
    public void createCommentShouldReturnCommentResponseDTOWhenPostExists(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(postRepository.findById(existingId)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDTO response = commentService.createComment(existingId, requestDTO, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("1");
        assertThat(response.getPostId()).isEqualTo("postId");
        assertThat(response.getText()).isEqualTo("text");
    }

    @Test
    public void createCommentShouldThrowResourceNotFoundExceptionWhenPostDoesNotExits(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(postRepository.findById(existingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.createComment(existingId, requestDTO, request)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateCommentShouldReturnCommentResponseDTOWhenBothCommentAndPostExist(){
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(commentRepository.findById(existingId)).thenReturn(Optional.of(comment));
        when(userService.getConnectedUser(any(HttpServletRequest.class))).thenReturn(user);
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        CommentResponseDTO response = commentService.update(existingId, requestDTO, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("1");
        assertThat(response.getPostId()).isEqualTo("postId");
        assertThat(response.getText()).isEqualTo("text");
    }

    @Test
    public void updateCommentShouldThrowResourceNotFoundExceptionWhenCommentDoesNotExist(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(commentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.update(nonExistingId, requestDTO, request)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateCommentShouldThrowForbiddenExceptionWhenLoggedUserIsNotTheCommentAuthor(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        User otherUser = new User("2", "other", "other", "other", 0, 0, 
            new ArrayList<>(), 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        comment.setAuthor(otherUser);
        when(commentRepository.findById(existingId)).thenReturn(Optional.of(comment));
        when(userService.getConnectedUser(any(HttpServletRequest.class))).thenReturn(user);
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));

        assertThatThrownBy(() -> commentService.update(existingId, requestDTO, request)).isInstanceOf(ForbiddenException.class);
    }

    @Test
    public void deleteByIdCommentShouldReturnCommentResponseDTOWhenBothCommentAndPostExist(){
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(commentRepository.findById(existingId)).thenReturn(Optional.of(comment));
        when(userService.getConnectedUser(any(HttpServletRequest.class))).thenReturn(user);
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));

        doNothing().when(commentRepository).delete(any(Comment.class));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        assertThatCode(() -> commentService.deleteById(existingId,request)).doesNotThrowAnyException();
    }

    @Test
    public void deleteByIdCommentShouldThrowResourceNotFoundExceptionWhenCommentDoesNotExist(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(commentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.deleteById(nonExistingId,request)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void deleteByIdCommentShouldThrowForbiddenExceptionWhenLoggedUserIsNotTheCommentAuthor(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        User otherUser = new User("2", "other", "other", "other", 0, 0, 
            new ArrayList<>(), 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        comment.setAuthor(otherUser);
        when(commentRepository.findById(existingId)).thenReturn(Optional.of(comment));
        when(userService.getConnectedUser(any(HttpServletRequest.class))).thenReturn(user);
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));

        assertThatThrownBy(() -> commentService.deleteById(existingId,request)).isInstanceOf(ForbiddenException.class);
    }

}
