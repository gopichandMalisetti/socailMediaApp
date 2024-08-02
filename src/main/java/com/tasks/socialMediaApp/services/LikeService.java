package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.model.Like;
import com.tasks.socialMediaApp.model.LikeId;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.LikeRepository;
import com.tasks.socialMediaApp.repositories.PostRepository;
import com.tasks.socialMediaApp.responseModel.ResponseLike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);
    LikeRepository likeRepository;
    PostRepository postRepository;

    LikeService(LikeRepository likeRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
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
