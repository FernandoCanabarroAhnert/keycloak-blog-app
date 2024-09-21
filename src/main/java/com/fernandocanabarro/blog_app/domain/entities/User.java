package com.fernandocanabarro.blog_app.domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    private String id;
    private String fullName;
    private String username;
    private String email;

    private Integer numberOfPosts;

    private Integer followers;
    @DBRef(lazy = true)
    private List<User> followersList;

    private Integer following;
    @DBRef(lazy = true)
    private List<User> followingList;

    @DBRef(lazy = true)
    private List<Post> posts = new ArrayList<>();
    @DBRef(lazy = true)
    private List<Post> likedPosts = new ArrayList<>();

    public void addPost(Post post){
        posts.add(post);
        this.setNumberOfPosts(this.getNumberOfPosts() + 1);
    }

    public void removePost(Post post){
        posts.remove(post);
        this.setNumberOfPosts(this.getNumberOfPosts() - 1);
    }

    public void addLikedPost(Post post){
        likedPosts.add(post);
    }

    public void removeLikedPost(Post post){
        likedPosts.remove(post);
    }

    public boolean hasLikedPost(Post post){
        Optional<Post> optional = likedPosts.stream().filter(p -> p.getId().equals(post.getId())).findFirst();
        return optional.isPresent();
    }

    public boolean hasFollower(User user){
        Optional<User> optional = followersList.stream().filter(p -> p.getId().equals(user.getId())).findFirst();
        return optional.isPresent();
    }

    public void addFollower(User user){
        followersList.add(user);
        increaseFollowersNumber();
    }

    public void removeFollower(User user){
        followersList.remove(user);
        decreaseFollowersNumber();
    }

    private void increaseFollowersNumber(){
        this.setFollowers(this.getFollowers() + 1);
    }

    private void decreaseFollowersNumber(){
        this.setFollowers(this.getFollowers() - 1);
    }

    public void addFollowing(User user){
        followingList.add(user);
        increaseFollowingNumber();
    }

    public void removeFollowing(User user){
        followingList.remove(user);
        decreaseFollowingNumber();
    }

    private void increaseFollowingNumber(){
        this.setFollowing(this.getFollowing() + 1);
    }

    private void decreaseFollowingNumber(){
        this.setFollowing(this.getFollowing() - 1);
    }
}
