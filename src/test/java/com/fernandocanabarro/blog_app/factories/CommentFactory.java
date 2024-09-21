package com.fernandocanabarro.blog_app.factories;

import java.time.Instant;

import com.fernandocanabarro.blog_app.domain.dtos.CommentRequestDTO;
import com.fernandocanabarro.blog_app.domain.entities.Comment;

public class CommentFactory {

    public static CommentRequestDTO getCommentRequestDTO(){
        return new CommentRequestDTO("text");
    }

    public static Comment getComment(){
        return new Comment("1", "postId", "text", UserFactory.getUser(), Instant.now(), null);
    }
}
