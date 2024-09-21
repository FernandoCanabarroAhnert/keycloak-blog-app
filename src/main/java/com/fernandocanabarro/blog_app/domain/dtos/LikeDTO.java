package com.fernandocanabarro.blog_app.domain.dtos;

import org.springframework.hateoas.RepresentationModel;

import com.fernandocanabarro.blog_app.domain.entities.Like;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO extends RepresentationModel<LikeDTO>{

    private String id;
    private AuthorDTO author;
    private String postId;

    public LikeDTO(Like entity){
        id = entity.getId();
        author = new AuthorDTO(entity.getUser());
        postId = entity.getPostId();
    }
}
