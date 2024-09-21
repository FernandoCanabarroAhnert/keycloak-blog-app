package com.test.blog_test.domain.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Post {

    @Id
    private String id;
    private String title;
    private String text;
    private String mediaUrl;
    private List<String> tags;
    private User user;
    private LocalDateTime moment;
    private LocalDateTime lastUpdate;
    private Integer likesCount;
    @DBRef(lazy = true)
    private List<Like> likes = new ArrayList<>();
    @DBRef(lazy = true)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public void addLike(Like like){
        likes.add(like);
        increaseLikesCount();
    }

    public void removeLike(Like like){
        likes.remove(like);
        decreaseLikesCount();
    }

    private void increaseLikesCount(){
        this.setLikesCount(this.likesCount + 1);
    }

    private void decreaseLikesCount(){
        this.setLikesCount(this.likesCount - 1);
    }


}
