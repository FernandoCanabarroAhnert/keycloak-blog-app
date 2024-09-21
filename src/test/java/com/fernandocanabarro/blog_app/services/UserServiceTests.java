package com.fernandocanabarro.blog_app.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.fernandocanabarro.blog_app.domain.dtos.AuthorDTO;
import com.fernandocanabarro.blog_app.domain.dtos.UserDTO;
import com.fernandocanabarro.blog_app.domain.entities.User;
import com.fernandocanabarro.blog_app.factories.UserFactory;
import com.fernandocanabarro.blog_app.repositories.UserRepository;
import com.fernandocanabarro.blog_app.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;

    private String existingId,nonExistingId;
    private User user;

    @BeforeEach
    public void setup() throws Exception{
        existingId = "1";
        nonExistingId = "2";
        user = UserFactory.getUser();
    }

    @Test
    public void getConnectedUserShouldReturnUserWhenUserAlreadyExistsInDB(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jwtService.extractClaimFromRequest(request, "sub")).thenReturn(existingId);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        User response = userService.getConnectedUser(request);

        assertThat(response).isNotNull();
        assertThat(response.getFullName()).isEqualTo("name");
        assertThat(response.getEmail()).isEqualTo("email");
        assertThat(response.getUsername()).isEqualTo("username");
    }

    @Test
    public void getConnectedUserShouldReturnUserWhenUserDoesNotExistInDB(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jwtService.extractClaimFromRequest(request, "sub")).thenReturn(existingId);
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        when(jwtService.extractClaimFromRequest(request, "email")).thenReturn(user.getEmail());
        when(jwtService.extractClaimFromRequest(request, "preferred_username")).thenReturn(user.getUsername());
        when(jwtService.extractClaimFromRequest(request, "name")).thenReturn(user.getFullName());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User response = userService.getConnectedUser(request);

        assertThat(response).isNotNull();
        assertThat(response.getFullName()).isEqualTo("name");
        assertThat(response.getEmail()).isEqualTo("email");
        assertThat(response.getUsername()).isEqualTo("username");
    }

    @Test
    public void findByIdShouldReturnUserDTOWhenUserExists(){
        when(userRepository.findById(existingId)).thenReturn(Optional.of(user));

        UserDTO response = userService.findById(existingId);

        assertThat(response).isNotNull();
        assertThat(response.getFullName()).isEqualTo("name");
        assertThat(response.getEmail()).isEqualTo("email");
        assertThat(response.getUsername()).isEqualTo("username");
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenUserDoesNotExist(){
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(nonExistingId)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void getMeShouldReturnUserDTOWhenUserExists(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jwtService.extractClaimFromRequest(request, "sub")).thenReturn(existingId);
        when(userRepository.findById(existingId)).thenReturn(Optional.of(user));

        UserDTO response = userService.getMe(request);

        assertThat(response).isNotNull();
        assertThat(response.getFullName()).isEqualTo("name");
        assertThat(response.getEmail()).isEqualTo("email");
        assertThat(response.getUsername()).isEqualTo("username");
    }

    @Test
    public void getMeShouldThrowResourceNotFoundExceptionWhenUserDoesNotExist(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jwtService.extractClaimFromRequest(request, "sub")).thenReturn(nonExistingId);
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getMe(request)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void findUsersShouldReturnPageOfAuthorDTOWhenUsernameIsNotEmpty(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findByUsername("username", pageable)).thenReturn(page);

        Page<AuthorDTO> response = userService.searchUsersByUsername("username", pageable);

        assertThat(response).isNotEmpty();
        assertThat(response.getContent().get(0).getId()).isEqualTo("1");
        assertThat(response.getContent().get(0).getUsername()).isEqualTo("username");
    }

    @Test
    public void findUsersShouldReturnPageOfAuthorDTOWhenUsernameIsEmpty(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<AuthorDTO> response = userService.searchUsersByUsername("", pageable);

        assertThat(response).isNotEmpty();
        assertThat(response.getContent().get(0).getId()).isEqualTo("1");
        assertThat(response.getContent().get(0).getUsername()).isEqualTo("username");
    }

    @Test
    public void interactWithUserShouldReturnUserDTOWhenInteractedUserDoesNotHaveTheFollower(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        User interactedUser = new User("2", "other", "other", "other", 0, 0, 
            new ArrayList<>(), 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        UserService spyService = spy(userService);

        when(spyService.getConnectedUser(any(HttpServletRequest.class))).thenReturn(user);
        when(userRepository.findById(existingId)).thenReturn(Optional.of(interactedUser));
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.save(interactedUser)).thenReturn(interactedUser);

        UserDTO response = spyService.interactWithUser(existingId, request);

        assertThat(response).isNotNull();
        assertThat(response.getFullName()).isEqualTo("other");
        assertThat(response.getEmail()).isEqualTo("other");
        assertThat(response.getUsername()).isEqualTo("other");
    }

    @Test
    public void interactWithUserShouldReturnUserDTOWhenInteractedUserDoesHaveTheFollower(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        User interactedUser = new User("2", "other", "other", "other", 0, 0, 
            new ArrayList<>(), 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        UserService spyService = spy(userService);
        interactedUser.addFollower(user);

        when(spyService.getConnectedUser(any(HttpServletRequest.class))).thenReturn(user);
        when(userRepository.findById(existingId)).thenReturn(Optional.of(interactedUser));
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.save(interactedUser)).thenReturn(interactedUser);

        UserDTO response = spyService.interactWithUser(existingId, request);

        assertThat(response).isNotNull();
        assertThat(response.getFullName()).isEqualTo("other");
        assertThat(response.getEmail()).isEqualTo("other");
        assertThat(response.getUsername()).isEqualTo("other");
    }

    @Test
    public void interactWithUserShouldThrowResourceNotFoundExceptionWhenInteractedUserDoesNotExist(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserService spyService = spy(userService);

        when(spyService.getConnectedUser(any(HttpServletRequest.class))).thenReturn(user);
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> spyService.interactWithUser(nonExistingId, request)).isInstanceOf(ResourceNotFoundException.class);
    }
}
