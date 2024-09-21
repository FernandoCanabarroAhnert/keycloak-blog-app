package com.fernandocanabarro.blog_app.domain.dtos;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.fernandocanabarro.blog_app.domain.entities.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO extends RepresentationModel<PostResponseDTO>{

    private String id;
    private String title;
    private String text;
    private String mediaUrl;
    private List<String> tags;
    private AuthorDTO author;
    private String moment;
    private String lastUpdate;
    private Integer likesCount;

    public PostResponseDTO(Post entity){
        id = entity.getId();
        title = entity.getTitle();
        text = entity.getText();
        mediaUrl = entity.getMediaUrl();
        tags = entity.getTags();
        author = new AuthorDTO(entity.getUser());
        moment = entity.getMoment().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        lastUpdate = (entity.getLastUpdate() == null) ? null : entity.getMoment().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        likesCount = entity.getLikesCount();
    }
}

