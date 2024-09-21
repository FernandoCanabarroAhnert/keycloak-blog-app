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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import com.fernandocanabarro.blog_app.domain.dtos.CommentResponseDTO;
import com.fernandocanabarro.blog_app.domain.dtos.LikeDTO;
import com.fernandocanabarro.blog_app.domain.dtos.PostRequestDTO;
import com.fernandocanabarro.blog_app.domain.dtos.PostResponseDTO;
import com.fernandocanabarro.blog_app.domain.entities.Comment;
import com.fernandocanabarro.blog_app.domain.entities.Like;
import com.fernandocanabarro.blog_app.domain.entities.Post;
import com.fernandocanabarro.blog_app.domain.entities.User;
import com.fernandocanabarro.blog_app.factories.CommentFactory;
import com.fernandocanabarro.blog_app.factories.LikeFactory;
import com.fernandocanabarro.blog_app.factories.PostFactory;
import com.fernandocanabarro.blog_app.factories.UserFactory;
import com.fernandocanabarro.blog_app.repositories.CommentRepository;
import com.fernandocanabarro.blog_app.repositories.LikeRepository;
import com.fernandocanabarro.blog_app.repositories.PostRepository;
import com.fernandocanabarro.blog_app.repositories.UserRepository;
import com.fernandocanabarro.blog_app.services.cache.PostServiceTemplate;
import com.fernandocanabarro.blog_app.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @InjectMocks
    private PostService postService;
    @Mock
    private PostServiceTemplate postServiceTemplate;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private UserService userService;

    private String existingId,nonExistingId;
    private PostRequestDTO requestDTO;
    private Post post;
    private PostResponseDTO responseDTO;
    private Page<Post> postPage;
    private Pageable pageable;
    private User user;
    private Comment comment;
    private Page<Comment> commentPage;
    private Like like;
    private Page<Like> likePage;

    @BeforeEach
    public void setup(){
        existingId = "1";
        nonExistingId = "2";
        requestDTO = PostFactory.getPostRequestDTO();
        post = PostFactory.getPost();
        responseDTO = new PostResponseDTO(post);
        postPage = new PageImpl<>(Arrays.asList(post));
        pageable = PageRequest.of(0, 10);
        user = UserFactory.getUser();
        comment = CommentFactory.getComment();
        commentPage = new PageImpl<>(Arrays.asList(comment));
        like = LikeFactory.getLike();
        likePage = new PageImpl<>(Arrays.asList(like));
    }

    @Test
    public void getPostsShouldReturnPageOfPostResponseDTOWhenTagsIsNotBlank(){
        when(postRepository.findByTags("tags", pageable)).thenReturn(postPage);

        Page<PostResponseDTO> response = postService.getPosts(pageable, "tags");

        assertThat(response).isNotEmpty();
        assertThat(response.getContent().get(0).getId()).isEqualTo(responseDTO.getId());
        assertThat(response.getContent().get(0).getTitle()).isEqualTo(responseDTO.getTitle());
        assertThat(response.getContent().get(0).getMediaUrl()).isEqualTo(responseDTO.getMediaUrl());
        assertThat(response.getContent().get(0).getAuthor().getUsername()).isEqualTo(responseDTO.getAuthor().getUsername());
    }

    @Test
    public void getPostsShouldReturnPageOfPostResponseDTOWhenTagsIsBlank(){
        when(postRepository.findAll(pageable)).thenReturn(postPage);

        Page<PostResponseDTO> response = postService.getPosts(pageable, "");

        assertThat(response).isNotEmpty();
        assertThat(response.getContent().get(0).getId()).isEqualTo(responseDTO.getId());
        assertThat(response.getContent().get(0).getTitle()).isEqualTo(responseDTO.getTitle());
        assertThat(response.getContent().get(0).getMediaUrl()).isEqualTo(responseDTO.getMediaUrl());
        assertThat(response.getContent().get(0).getAuthor().getUsername()).isEqualTo(responseDTO.getAuthor().getUsername());
    }

    @Test
    public void findByIdShouldReturnPostResponseDTOWhenIdExists(){
        when(postServiceTemplate.findById(existingId)).thenReturn(responseDTO);

        PostResponseDTO response = postService.findById(existingId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(responseDTO.getId());
        assertThat(response.getTitle()).isEqualTo(responseDTO.getTitle());
        assertThat(response.getMediaUrl()).isEqualTo(responseDTO.getMediaUrl());
        assertThat(response.getAuthor().getUsername()).isEqualTo(responseDTO.getAuthor().getUsername());
    }

    @Test
    public void getPostsByAuthorShouldReturnPageOfPostResponseDTOWhenAuthorExists(){
        when(userRepository.findById(existingId)).thenReturn(Optional.of(user));
        when(postRepository.findByUser(anyString(), any(Pageable.class))).thenReturn(postPage);

        Page<PostResponseDTO> response = postService.getPostsByAuthor(pageable, existingId);

        assertThat(response).isNotEmpty();
        assertThat(response.getContent().get(0).getId()).isEqualTo(responseDTO.getId());
        assertThat(response.getContent().get(0).getTitle()).isEqualTo(responseDTO.getTitle());
        assertThat(response.getContent().get(0).getMediaUrl()).isEqualTo(responseDTO.getMediaUrl());
        assertThat(response.getContent().get(0).getAuthor().getUsername()).isEqualTo(responseDTO.getAuthor().getUsername());
    }

    @Test
    public void getPostsByAuthorShouldThrowResourceNotFoundExceptionWhenUserIdDoesNotExist(){
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getPostsByAuthor(pageable, nonExistingId)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void getMyPostsShouldReturnPageOfPostResponseDTOWhenAuthorExists(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(userService.getConnectedUser(any(HttpServletRequest.class))).thenReturn(user);
        when(postRepository.findByUser(anyString(), any(Pageable.class))).thenReturn(postPage);

        Page<PostResponseDTO> response = postService.getMyPosts(pageable, request);

        assertThat(response).isNotEmpty();
        assertThat(response.getContent().get(0).getId()).isEqualTo(responseDTO.getId());
        assertThat(response.getContent().get(0).getTitle()).isEqualTo(responseDTO.getTitle());
        assertThat(response.getContent().get(0).getMediaUrl()).isEqualTo(responseDTO.getMediaUrl());
        assertThat(response.getContent().get(0).getAuthor().getUsername()).isEqualTo(responseDTO.getAuthor().getUsername());
    }

    @Test
    public void getMyLikedPostsShouldReturnPageOfPostResponseDTOWhenAuthorExists(){
        user.addLikedPost(post);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(userService.getConnectedUser(any(HttpServletRequest.class))).thenReturn(user);

        List<PostResponseDTO> response = postService.getMyLikedPosts(request);

        assertThat(response).isNotEmpty();
        assertThat(response.get(0).getId()).isEqualTo(responseDTO.getId());
        assertThat(response.get(0).getTitle()).isEqualTo(responseDTO.getTitle());
        assertThat(response.get(0).getMediaUrl()).isEqualTo(responseDTO.getMediaUrl());
        assertThat(response.get(0).getAuthor().getUsername()).isEqualTo(responseDTO.getAuthor().getUsername());
    }

    @Test
    public void findCommentsByPostIdShouldReturnPageOfPostResponseDTOWhenAuthorExists(){
        when(postRepository.findById(existingId)).thenReturn(Optional.of(post));
        when(commentRepository.findByPostId(existingId, pageable)).thenReturn(commentPage);
        
        Page<CommentResponseDTO> response = postService.findCommentsByPostId(existingId,pageable);

        assertThat(response).isNotEmpty();
        assertThat(response.getContent().get(0).getId()).isEqualTo("1");
        assertThat(response.getContent().get(0).getText()).isEqualTo("text");
        assertThat(response.getContent().get(0).getPostId()).isEqualTo("postId");
        assertThat(response.getContent().get(0).getAuthor().getUsername()).isEqualTo(responseDTO.getAuthor().getUsername());
    }

    @Test
    public void findCommentsByPostIdShouldThrowResourceNotFoundExceptionWhenPostDoesNotExist(){
        when(postRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->  postService.findCommentsByPostId(nonExistingId,pageable)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void getLikesByPostIdShouldReturnPageOfPostResponseDTOWhenAuthorExists(){
        when(postRepository.findById(existingId)).thenReturn(Optional.of(post));
        when(likeRepository.findByPostId(existingId, pageable)).thenReturn(likePage);
        
        Page<LikeDTO> response = postService.getLikesByPostId(existingId,pageable);

        assertThat(response).isNotEmpty();
        assertThat(response.getContent().get(0).getId()).isEqualTo("1");
        assertThat(response.getContent().get(0).getPostId()).isEqualTo("postId");
        assertThat(response.getContent().get(0).getAuthor().getUsername()).isEqualTo(responseDTO.getAuthor().getUsername());
    }

    @Test
    public void getLikesByPostIdShouldThrowResourceNotFoundExceptionWhenPostoesNotExist(){
        when(postRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->  postService.getLikesByPostId(nonExistingId,pageable)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void insertShouldReturnPostDTOWhenDataIsValid(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(postServiceTemplate.createPost(requestDTO, request)).thenReturn(responseDTO);

        PostResponseDTO response = postService.insert(requestDTO, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(responseDTO.getId());
        assertThat(response.getTitle()).isEqualTo(responseDTO.getTitle());
        assertThat(response.getMediaUrl()).isEqualTo(responseDTO.getMediaUrl());
        assertThat(response.getAuthor().getUsername()).isEqualTo(responseDTO.getAuthor().getUsername());
    }

    @Test
    public void updateShouldReturnPostDTOWhenDataIsValid(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(postServiceTemplate.update(existingId,requestDTO, request)).thenReturn(responseDTO);

        PostResponseDTO response = postService.update(existingId,request, requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(responseDTO.getId());
        assertThat(response.getTitle()).isEqualTo(responseDTO.getTitle());
        assertThat(response.getMediaUrl()).isEqualTo(responseDTO.getMediaUrl());
        assertThat(response.getAuthor().getUsername()).isEqualTo(responseDTO.getAuthor().getUsername());
    }

    @Test
    public void deleteByIdShouldThrowNoException(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        doNothing().when(postServiceTemplate).delete(existingId, request);

        assertThatCode(() -> postService.deleteById(existingId, request)).doesNotThrowAnyException();
    }

    @Test
    public void interactWithPostShouldReturnPostResponseDTOWhenUserHasNotLikedThePost(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(postRepository.findById(existingId)).thenReturn(Optional.of(post));
        when(userService.getConnectedUser(any(HttpServletRequest.class))).thenReturn(user);
        when(likeRepository.save(any(Like.class))).thenReturn(like);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        PostResponseDTO response = postService.interactWithPost(existingId,request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(responseDTO.getId());
        assertThat(response.getTitle()).isEqualTo(responseDTO.getTitle());
        assertThat(response.getMediaUrl()).isEqualTo(responseDTO.getMediaUrl());
        assertThat(response.getAuthor().getUsername()).isEqualTo(responseDTO.getAuthor().getUsername());
    }

    @Test
    public void interactWithPostShouldReturnPostResponseDTOWhenUserHasLikedThePost(){
        user.addLikedPost(post);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(postRepository.findById(existingId)).thenReturn(Optional.of(post));
        when(userService.getConnectedUser(any(HttpServletRequest.class))).thenReturn(user);
        when(likeRepository.findByUserIdAndPostId(existingId, existingId)).thenReturn(like);
        doNothing().when(likeRepository).delete(any(Like.class));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(userRepository.save(any(User.class))).thenReturn(user);

        PostResponseDTO response = postService.interactWithPost(existingId,request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(responseDTO.getId());
        assertThat(response.getTitle()).isEqualTo(responseDTO.getTitle());
        assertThat(response.getMediaUrl()).isEqualTo(responseDTO.getMediaUrl());
        assertThat(response.getAuthor().getUsername()).isEqualTo(responseDTO.getAuthor().getUsername());
    }

    @Test
    public void interactWithPostShouldThrowResourceNotFoundExceptionWhenPostDoesNotExist(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(postRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->  postService.interactWithPost(nonExistingId,request)).isInstanceOf(ResourceNotFoundException.class);
    }
}
