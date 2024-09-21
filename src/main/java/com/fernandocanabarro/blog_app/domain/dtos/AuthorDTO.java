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
public class AuthorDTO extends RepresentationModel<AuthorDTO>{

    private String id;
    private String username;

    public AuthorDTO(User entity){
        id = entity.getId();
        username = entity.getUsername();
    }
}
