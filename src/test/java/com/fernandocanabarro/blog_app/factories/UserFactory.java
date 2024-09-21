package com.fernandocanabarro.blog_app.factories;

import com.fernandocanabarro.blog_app.domain.entities.User;
import java.util.ArrayList;

public class UserFactory {

    public static User getUser(){
        return new User("1", "name", "username", "email", 0, 0, 
        new ArrayList<>(), 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}
