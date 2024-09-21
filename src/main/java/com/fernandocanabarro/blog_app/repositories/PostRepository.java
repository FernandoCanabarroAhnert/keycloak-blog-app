package com.fernandocanabarro.blog_app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.blog_app.domain.entities.Post;

@Repository
public interface PostRepository extends MongoRepository<Post,String>{

    @Query("{ 'user.id': ?0 }")
    Page<Post> findByUser(String userId,Pageable pageable);

    @Query("{ tags: { $regex: ?0, $options: i } }")
    Page<Post> findByTags(String tags,Pageable pageable);

}
