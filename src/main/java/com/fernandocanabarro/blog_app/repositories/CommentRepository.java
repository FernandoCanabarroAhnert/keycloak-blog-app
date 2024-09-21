package com.fernandocanabarro.blog_app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.blog_app.domain.entities.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String>{

    @Query("{ postId: ?0 }")
    Page<Comment> findByPostId(String postId,Pageable pageable);

    void deleteAllByPostId(String postId);
}
