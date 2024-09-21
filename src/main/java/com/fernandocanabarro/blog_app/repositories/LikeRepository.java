package com.fernandocanabarro.blog_app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.blog_app.domain.entities.Like;

@Repository
public interface LikeRepository extends MongoRepository<Like,String>{

    @Query("{ 'user.id': ?0, postId: ?1 }")
    Like findByUserIdAndPostId(String userId,String postId);

    @Query("{ postId: ?0 }")
    Page<Like> findByPostId(String postId,Pageable pageable);
}
