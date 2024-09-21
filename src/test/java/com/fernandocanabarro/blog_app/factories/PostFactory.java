package com.fernandocanabarro.blog_app.factories;

import java.util.Arrays;
import java.util.ArrayList;
import java.time.LocalDateTime;

import com.fernandocanabarro.blog_app.domain.dtos.PostRequestDTO;
import com.fernandocanabarro.blog_app.domain.entities.Post;

public class PostFactory {

    public static PostRequestDTO getPostRequestDTO(){
        return new PostRequestDTO("title", "text", "mediaurl", Arrays.asList("tags"));
    }

    public static Post getPost(){
        return new Post("1", "title", "text", "mediaurl", 
            Arrays.asList("tags"), UserFactory.getUser(), LocalDateTime.now(),LocalDateTime.now(), 0,
            new ArrayList<>(),new ArrayList<>());
    }


}
