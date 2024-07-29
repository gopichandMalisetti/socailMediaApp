package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.model.Like;
import com.tasks.socialMediaApp.model.LikeId;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.LikeRepository;
import com.tasks.socialMediaApp.repositories.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {


    LikeRepository likeRepository;
    PostRepository postRepository;

    LikeService(LikeRepository likeRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
    }

    public Like addLike(Like like, User user, Post post){

        like.setPost(post);
        like.setUser(user);

        Like savedLike = null;
        try{
            savedLike = likeRepository.save(like);
            post.setLikesCount(post.getLikesCount() + 1);
            postRepository.save(post);
        }catch (Exception e){
            System.out.println("Exception Found: ");
            System.out.println(e.getMessage());
            return null;
        }

        System.out.println("like: "+ savedLike);
        return savedLike;
    }

    public boolean findLike(User user, Post post){

        return likeRepository.findByUserAndPost(user,post) != null;
    }

    public  boolean unLike(User user, Post post){

        LikeId likeId = new LikeId(user,post);
        try{
            likeRepository.deleteById(likeId);
            post.setLikesCount(post.getLikesCount()-1);
            postRepository.save(post);
        }catch (Exception e){
            System.out.println("Exception Found : ");
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}
