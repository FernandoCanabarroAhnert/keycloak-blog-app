package com.fernandocanabarro.blog_app.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.blog_app.controllers.PostController;
import com.fernandocanabarro.blog_app.controllers.UserController;
import com.fernandocanabarro.blog_app.domain.dtos.AuthorDTO;
import com.fernandocanabarro.blog_app.domain.dtos.UserDTO;
import com.fernandocanabarro.blog_app.domain.entities.User;
import com.fernandocanabarro.blog_app.repositories.UserRepository;
import com.fernandocanabarro.blog_app.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public User getConnectedUser(HttpServletRequest request) {
        String userId = jwtService.extractClaimFromRequest(request, JwtService.USERID);
        Optional<User> user = userRepository.findById(userId);
        String username = jwtService.extractClaimFromRequest(request, JwtService.USERNAME);
        String fullName = jwtService.extractClaimFromRequest(request, JwtService.FULL_NAME);
        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setId(userId);
            newUser.setFullName(fullName);
            newUser.setUsername(username);
            newUser.setEmail(jwtService.extractClaimFromRequest(request, JwtService.EMAIL));
            newUser.setPosts(new ArrayList<>(Arrays.asList()));
            newUser.setLikedPosts(Collections.emptyList());
            newUser.setFollowers(0);
            newUser.setFollowing(0);
            newUser.setNumberOfPosts(0);
            newUser.setFollowersList(new ArrayList<>(Arrays.asList()));
            newUser.setFollowingList(new ArrayList<>(Arrays.asList()));
            userRepository.save(newUser);
            user = Optional.of(newUser);
        }
        return user.get();
    }

    @Transactional(readOnly = true)
    public UserDTO findById(String id){
        return new UserDTO(userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id)))
            .add(linkTo(methodOn(UserController.class).getPostsByAuthor(null, id)).withRel("Consultar Posts deste Usuário"))
            .add(linkTo(methodOn(UserController.class).interactWithUser(id, null)).withRel("Interagir com Usuário")
            );
    }

    @Transactional(readOnly = true)
    public UserDTO getMe(HttpServletRequest request){
        User user = getConnectedUser(request);
        return new UserDTO(userRepository.findById(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException(user.getId())))
            .add(linkTo(methodOn(PostController.class).getMyPosts(null, request)).withRel("Consultar meus Posts"));
    }

    @Transactional(readOnly = true)
    public Page<AuthorDTO> searchUsersByUsername(String username,Pageable pageable){
        return (!username.isBlank()) 
            ? userRepository.findByUsername(username, pageable)
                .map(x -> new AuthorDTO(x).add(linkTo(methodOn(UserController.class).getUserById(x.getId())).withRel("Consultar Perfil deste Usuário")))
            : userRepository.findAll(pageable)
                .map(x -> new AuthorDTO(x).add(linkTo(methodOn(UserController.class).getUserById(x.getId())).withRel("Consultar Perfil deste Usuário")));
    }

    @Transactional
    public UserDTO interactWithUser(String userId,HttpServletRequest request){
        User connectedUser = getConnectedUser(request);
        User interactedUser = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(userId));
        if (!interactedUser.hasFollower(connectedUser)) {
            return followUser(connectedUser, interactedUser);
        }
        return unfollowUser(connectedUser,interactedUser);
    }

    @Transactional
    private UserDTO followUser(User connectedUser, User interactedUser){
        connectedUser.addFollowing(interactedUser);
        interactedUser.addFollower(connectedUser);
        connectedUser = userRepository.save(connectedUser);
        interactedUser = userRepository.save(interactedUser);
        return new UserDTO(interactedUser);
    }

    @Transactional
    private UserDTO unfollowUser(User connectedUser, User interactedUser){
        connectedUser.removeFollowing(interactedUser);
        interactedUser.removeFollower(connectedUser);
        connectedUser = userRepository.save(connectedUser);
        interactedUser = userRepository.save(interactedUser);
        return new UserDTO(interactedUser);
    }
}
