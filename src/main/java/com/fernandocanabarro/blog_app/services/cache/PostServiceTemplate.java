package com.fernandocanabarro.blog_app.services.cache;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.blog_app.domain.dtos.PostRequestDTO;
import com.fernandocanabarro.blog_app.domain.dtos.PostResponseDTO;
import com.fernandocanabarro.blog_app.domain.entities.Like;
import com.fernandocanabarro.blog_app.domain.entities.Post;
import com.fernandocanabarro.blog_app.domain.entities.User;
import com.fernandocanabarro.blog_app.repositories.CommentRepository;
import com.fernandocanabarro.blog_app.repositories.LikeRepository;
import com.fernandocanabarro.blog_app.repositories.PostRepository;
import com.fernandocanabarro.blog_app.repositories.UserRepository;
import com.fernandocanabarro.blog_app.services.UserService;
import com.fernandocanabarro.blog_app.services.exceptions.ForbiddenException;
import com.fernandocanabarro.blog_app.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PostServiceTemplate extends PostCacheTemplate<String,PostResponseDTO,PostRequestDTO>{

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserService userService;

    private RMapCache<String,PostResponseDTO> map;

    public PostServiceTemplate(RedissonClient client){
        map = client.getMapCache("posts",new TypedJsonJacksonCodec(String.class,PostResponseDTO.class));
    }

    @Transactional
    public PostResponseDTO createPost(PostRequestDTO dto, HttpServletRequest request) {
        Post post = new Post();
        User user = userService.getConnectedUser(request);
        postMapper(post, dto, request);
        post = postRepository.save(post);
        user.addPost(post);
        userRepository.save(user);
        post.setUser(user);
        post = postRepository.save(post);
        PostResponseDTO response = new PostResponseDTO(post);
        map.put(response.getId(), response);
        return response;
    }

    private void postMapper(Post post, PostRequestDTO dto, HttpServletRequest request) {
        post.setTitle(dto.getTitle());
        post.setText(dto.getText());
        post.setMediaUrl(dto.getMediaUrl());
        post.setTags(dto.getTags());
        post.setMoment(LocalDateTime.now());
        post.setLastUpdate(null);
        post.setLikesCount(0);
        post.setLikes(Arrays.asList());
        post.setComments(Arrays.asList());
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
    private PostResponseDTO likePost(User user, Post post) {
        Like like = new Like();
        like.setUser(user);
        like.setPostId(post.getId());
        like = likeRepository.save(like);
        post.addLike(like);
        user.addLikedPost(post);
        postRepository.save(post);
        userRepository.save(user);
        return new PostResponseDTO(post);
    }

    @Transactional
    private PostResponseDTO dislikePost(User user, Post post) {
        Like like = likeRepository.findByUserIdAndPostId(user.getId(), post.getId());
        post.removeLike(like);
        user.removeLikedPost(post);
        likeRepository.delete(like);
        userRepository.save(user);
        postRepository.save(post);
        return new PostResponseDTO(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponseDTO getFromDB(String id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id));
        return new PostResponseDTO(post);
    }

    @Override
    public Optional<PostResponseDTO> getFromCache(String id) {
        return (map.containsKey(id)) 
            ? Optional.of(map.get(id))
            : Optional.empty();
    }

    @Override
    @Transactional
    public PostResponseDTO updateDB(String id, PostRequestDTO dto,HttpServletRequest request) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        User user = userService.getConnectedUser(request);
        if (!user.getId().equals(post.getUser().getId())) {
            throw new ForbiddenException("Somente o Autor do Post pode Alterá-lo");
        }
        post.setTitle(dto.getTitle());
        post.setText(dto.getText());
        post.setMediaUrl(dto.getMediaUrl());
        post.getTags().clear();
        post.setTags(dto.getTags());
        post.setLastUpdate(LocalDateTime.now());
        post = postRepository.save(post);
        return new PostResponseDTO(post);
    }

    @Override
    public void updateCache(String id, PostResponseDTO dto) {
        map.put(id, dto);
    }

    @Override
    @Transactional
    public void deleteFromDB(String id,HttpServletRequest request) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        User user = userService.getConnectedUser(request);
        if (!user.getId().equals(post.getUser().getId())) {
            throw new ForbiddenException("Somente o Autor do Post pode Excluí-lo");
        }
        user.removePost(post);
        userRepository.save(user);
        for (Like like : post.getLikes()){
            User obj = like.getUser();
            obj.removeLikedPost(post);
            likeRepository.delete(like);
            userRepository.save(obj);
        }
        commentRepository.deleteAllByPostId(post.getId());
        postRepository.delete(post);
    }

    @Override
    public void deleteFromCache(String id) {
        map.remove(id);
    }

    @Scheduled(fixedRate = 30000)
    public void updateMap(){
        map.clear();
        Map<String,PostResponseDTO> response = postRepository.findAll()
            .stream().map(PostResponseDTO::new).collect(Collectors.toMap(PostResponseDTO::getId, Function.identity()));
        map.putAll(response);
    }

}
