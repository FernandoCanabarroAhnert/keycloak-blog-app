package com.fernandocanabarro.blog_app.domain.dtos;

import org.springframework.hateoas.RepresentationModel;

import com.fernandocanabarro.blog_app.domain.entities.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends RepresentationModel<UserDTO>{

    private String id;
    private String fullName;
    private String username;
    private String email;
    private Integer numberOfPosts;
    private Integer followers;
    private Integer following;

    public UserDTO(User entity){
        id = entity.getId();
        fullName = entity.getFullName();
        username = entity.getUsername();
        email = entity.getEmail();
        numberOfPosts = entity.getNumberOfPosts();
        followers = entity.getFollowers();
        following = entity.getFollowing();
    }

}
