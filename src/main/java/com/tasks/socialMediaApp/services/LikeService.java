package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.Exceptions.ActionDoneBeforeException;
import com.tasks.socialMediaApp.Exceptions.InternalServerException;
import com.tasks.socialMediaApp.Exceptions.NotFoundException;
import com.tasks.socialMediaApp.model.Like;
import com.tasks.socialMediaApp.model.LikeId;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.LikeRepository;
import com.tasks.socialMediaApp.repositories.PostRepository;
import com.tasks.socialMediaApp.responseModel.ResponseLike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);
    LikeRepository likeRepository;
    PostRepository postRepository;
    PostService postService;

    LikeService(LikeRepository likeRepository, PostRepository postRepository,@Lazy PostService postService) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.postService = postService;
    }

    public ResponseLike handleAddLikeToAPost(User user, int postId, Like like){

        Optional<Post> optionalPost = postService.findPost(postId);
        if (optionalPost.isEmpty()) throw new NotFoundException("post not found!");

        Post post = optionalPost.get();
        if (userLikedThePost(user, post)) throw new ActionDoneBeforeException("you have already liked the post!");
        Like savedLike = addLikeToPost(like,user,post);

        if(savedLike == null){
            throw new InternalServerException("internal server problem");
        }
        return buildResponseLike(savedLike);
    }

    public void handleUnlikeAPost(User user,int postId){

        Optional<Post> optionalPost = postService.findPost(postId);
        if(optionalPost.isEmpty()) throw new NotFoundException("post not found!");
        Post post = optionalPost.get();

        if(!userLikedThePost(user,post)) throw new ActionDoneBeforeException("you have not liked the post!");
        if(!unLikeThePost(user,post)) throw new InternalServerException("internal server problem");

    }

    public Like addLikeToPost(Like like, User user, Post post){

        like.setPost(post);
        like.setUser(user);

        Like savedLike = null;
        try{
            savedLike = likeRepository.save(like);
            post.setLikesCount(post.getLikesCount() + 1);
            postRepository.save(post);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }

        logger.debug("like: "+ savedLike);
        return savedLike;
    }

    public boolean userLikedThePost(User user, Post post){

        return likeRepository.findByUserAndPost(user,post) != null;
    }

    public  boolean unLikeThePost(User user, Post post){

        LikeId likeId = new LikeId(user,post);
        try{
            likeRepository.deleteById(likeId);
            post.setLikesCount(post.getLikesCount()-1);
            postRepository.save(post);
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    public ResponseLike buildResponseLike(Like savedLike){

        ResponseLike responseLike = new ResponseLike();
        responseLike.setLikedTime(savedLike.getLikedTime());
        return responseLike;

    }

    public void deleteLikesOfAUser(User user){

        likeRepository.deleteByUser(user);
    }

    public void deleteLikesOfAPost(Post post){

        likeRepository.deleteByPost(post);
    }
}
