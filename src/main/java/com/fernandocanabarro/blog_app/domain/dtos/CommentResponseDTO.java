package com.fernandocanabarro.blog_app.domain.dtos;

import com.fernandocanabarro.blog_app.domain.entities.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO extends RepresentationModel<CommentResponseDTO>{

    private String id;
    private String postId;
    private String text;
    private AuthorDTO author;
    private Instant moment;
    private Instant lastUpdate;

    public CommentResponseDTO(Comment comment) {
        id = comment.getId();
        postId = comment.getPostId();
        text = comment.getText();
        author = new AuthorDTO(comment.getAuthor());
        moment = comment.getMoment();
        lastUpdate = comment.getLastUpdate();
    }
}
