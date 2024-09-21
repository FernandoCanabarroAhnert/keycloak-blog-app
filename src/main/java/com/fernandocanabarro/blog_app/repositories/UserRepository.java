package com.fernandocanabarro.blog_app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.blog_app.domain.entities.User;

@Repository
public interface UserRepository extends MongoRepository<User,String>{

    @Query("{ username: { $regex: ?0, $options: i } }")
    Page<User> findByUsername(String username,Pageable pageable);
}
