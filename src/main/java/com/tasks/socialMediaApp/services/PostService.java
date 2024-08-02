package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.model.Image;
import com.tasks.socialMediaApp.model.Like;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.PostRepository;
import com.tasks.socialMediaApp.requestModel.RequestPost;
import com.tasks.socialMediaApp.responseModel.ResponseImage;
import com.tasks.socialMediaApp.responseModel.ResponsePost;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    UserService userService;
    PostRepository postRepository;
    ImageService imageService;
    CommentService commentService;
    LikeService likeService;

    @Autowired
    PostService(PostRepository postRepository,@Lazy UserService userService, ImageService imageService,
                CommentService commentService, LikeService likeService){
        this.postRepository = postRepository;
        this.userService = userService;
        this.imageService = imageService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @Transactional
    public Post addPost(RequestPost requestPost, User user){

        Post postToSave = new Post(requestPost.getDetails(),user);
        Post savedPost = null;
        try {
            savedPost = postRepository.save(postToSave);
            imageService.saveImages(requestPost.getImages(),savedPost);
        }catch (Exception e){
           logger.error("Exception Found :");
           logger.error(e.getMessage());
        }

        return savedPost;

    }

    public Optional<Post> findPost(int postId){

        return postRepository.findById(postId);
    }

    public Optional<Post> findPostOfAUser(int id,int userId){

        return Optional.ofNullable(postRepository.findByIdAndUser_Id(id, userId));
    }

//    public boolean deletePost(int postId){
//
//         try{
//             postRepository.deleteById(postId);
//         }catch (Exception e){
//            logger.error(e.getMessage());
//             return false;
//         }
//         return true;
//    }

    public boolean isPostOwnedByUser(int postId, int userId){

        Post post = postRepository.findByIdAndUser_Id(postId,userId);

        return post == null;
    }

    public boolean editPost(Post post){

        Optional<Post> optionalPost = postRepository.findById(post.getId());

        Post postEntity = null;
        if(optionalPost.isPresent()) {
            postEntity = optionalPost.get();
        }

       logger.error(post.getDetails());
        postEntity.setDetails(post.getDetails());

        try {
            postRepository.save(postEntity);
        }catch (Exception e){
           logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    public List<Post> getAllPosts(){

        return postRepository.findAll();
    }

    public List<List<Post>> getPostsFromUserIFollow(User user){

        List<User> usersIFollow = userService.findUsersIFollow(user);
        return usersIFollow.stream()
                .map(this::getPostsOfAUser)
                .collect(Collectors.toList());
    }

    public List<Post> getPostsOfAUser(User user){

        return postRepository.findAllByUser(user);
    }

    public List<ResponseImage> buildResponseImagesOfAPost(Post post){

        List<Image> images = imageService.getImagesOfAPost(post);
        return images.stream()
                .map(image -> {
                            ResponseImage responseImage = new ResponseImage();
                            responseImage.setId(image.getId());
                            responseImage.setUrl(image.getUrl());
                            return responseImage;
                        }
                ).collect(Collectors.toList());
    }

    public ResponsePost buildResponsePost(Post post){
        ResponsePost responsePost = new ResponsePost();
        responsePost.setDetials(post.getDetails());
        responsePost.setId(post.getId());
        responsePost.setLikesCount(post.getLikesCount());
        List<ResponseImage> responseImages = buildResponseImagesOfAPost(post);
        responsePost.setResponseImages(responseImages);

        return responsePost;
    }

    public List<ResponsePost> buildResponsePostList(List<Post> allPosts){

        return allPosts.stream()
                .map(this::buildResponsePost)
                .collect(Collectors.toList());

    }

    public List<ResponsePost> buildResponsePostListFromNestedPostList(List<List<Post>> allPosts){

        return allPosts.stream()
                .flatMap(List::stream)
                .map(this::buildResponsePost)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(Post post){

        commentService.deleteCommentsOfAPost(post);
        imageService.deleteImagesOfAPost(post);
        likeService.deleteLikesOfAPost(post);
        postRepository.deleteById(post.getId());
    }

    @Transactional
    public void deletePostsOfAUser(User user){

        List<Post> postsOfAUser = postRepository.findAllByUser(user);
        postsOfAUser
                .forEach(this::deletePost);

    }
}
