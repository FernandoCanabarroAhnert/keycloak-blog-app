package com.fernandocanabarro.blog_app.factories;

import com.fernandocanabarro.blog_app.domain.entities.Like;

public class LikeFactory {

    public static Like getLike(){
        return new Like("1", UserFactory.getUser(), "postId");
    }
}
