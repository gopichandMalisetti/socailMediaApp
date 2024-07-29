package com.tasks.socialMediaApp;

import com.tasks.socialMediaApp.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class Queries {

    @PersistenceContext
    EntityManager entityManager;


    @Transactional
    public void createUser(){

//        Profile profile = new Profile("ram charan","00000000000","www.google.com","iam a good boy","gopi@gmail.com");
//
//        User user = new User();
//        user.setUserName("ramcharan");
//        user.setPassword("123456");
//        user.setProfile(profile);
//
//        entityManager.persist(user);

        User u1 = entityManager.find(User.class,1);
        User u2 = entityManager.find(User.class,2);

        //Post post = entityManager.find(Post.class,1);

        Date currentDate = new Date();
        System.out.println(currentDate);
//        Like like = new Like(currentDate,post,u);
//
//        entityManager.persist(like);

//        Image image = new Image(1,post,"https://images.app.goo.gl/wjBJfxty6bmCALLx8");
//        entityManager.persist(image);

//        Comment comment = new Comment(currentDate,"very good morning! have a nice day",1,post,u);
//        entityManager.persist(comment);

//        FollowId followId = new FollowId(u1,u2);
//        Follow follow = new Follow(currentDate,followId);
//        entityManager.persist(follow);

//        List<Comment> user1Comments = u1.getComments();
//        List<Comment> user2Comments = u2.getComments();
//
//        for (Comment comment : user1Comments){
//            System.out.println("------------- user1 comments -------------");
//            System.out.println(comment.getDescription());
//        }
//
//        for (Comment comment : user2Comments){
//            System.out.println(comment.getDescription());
//        }

//        List<Like> likes = post.getLikes();
//        List<Comment> comments = post.getComments();
//        List<Image> images = post.getImages();
//
//        for (Like like : likes){
//            System.out.println("liked time : " + like.getLikedTime());
//            System.out.println("liked user : " + like.getUser());
//        }
//
//        for (Comment comment : comments){
//            System.out.println("comment :" + comment.getDescription());
//        }
//
//        for (Image image : images){
//            System.out.println("image url :" + image.getUrl());
//        }

//        List<Follow> followedUsers = u2.getFollowedUsers();
//        List<Follow> followingUsers = u2.getFollowingUsers();
//
//        System.out.println("--------- user names of whom i am following -------------- ");
//        for (Follow follow : followedUsers){
//            User followedUser = follow.getFollowingUser();
//            System.out.println(followedUser.getUserName());
//        }

//        System.out.println("----------- my followers user name -----------------");
//        for (Follow follow : followingUsers){
//            User followingUser = follow.getFollowedUser();
//            System.out.println(followingUser.getUserName());
//        }

        System.out.println(u2.getProfile().getPhno());
    }
}
