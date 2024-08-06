package com.tasks.socialMediaApp.services;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.tasks.socialMediaApp.Exceptions.InternalServerException;
import com.tasks.socialMediaApp.Exceptions.NotFoundException;
import com.tasks.socialMediaApp.model.Image;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    PostService(PostRepository postRepository,@Lazy UserService userService,@Lazy ImageService imageService,
                @Lazy CommentService commentService,@Lazy LikeService likeService){
        this.postRepository = postRepository;
        this.userService = userService;
        this.imageService = imageService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    public ResponsePost handleAddPost(RequestPost requestPost, User user){

        Post savedPost = addPost(requestPost,user);
        if( savedPost == null){
             throw new InternalServerException("internal server problem");
        }
        return buildResponsePost(savedPost);

    }

    public ResponsePost handleGetAPostOfAUser(User user, int postId){

        Optional<Post> optionalPost;
        try {
            optionalPost = findPostOfAUser(postId,user.getId());
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new InternalServerException("internal server problem");
        }

        if(optionalPost.isEmpty()) throw new NotFoundException("post not found");
        Post post = optionalPost.get();
        return buildResponsePost(post);
    }

    public void handleDeleteAPost(User user, int postId){

        Optional<Post> optionalPost;
        try {
            optionalPost = findPostOfAUser(postId,user.getId());
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new InternalServerException("internal server problem");
        }

        if(optionalPost.isEmpty()) throw new NotFoundException("post not found");

        try {
            deletePost(optionalPost.get());
        }catch (Exception e){
            throw new InternalServerException("internal server problem");
        }

    }

    public void handleEditPost(User user, Post post){

        Optional<Post> optionalPost;
        try {
            optionalPost = findPostOfAUser(post.getId(), user.getId());
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new InternalServerException("internal server problem");
        }

        if(optionalPost.isEmpty()) throw new NotFoundException("post not found");
        if(!editPost(post)){
            throw new InternalServerException("internal server problem");
        }
    }

    public List<ResponsePost> fetchAllPosts(){
        List<Post> allPosts = getAllPosts();
        return buildResponsePostList(allPosts);
    }

    public List<ResponsePost> fetchAllPostsFromUserFollows(User user){

        List<List<Post>> allPosts =  getPostsFromUserIFollow(user);
        return buildResponsePostListFromNestedPostList(allPosts);
    }

    public List<ResponsePost> fetchPostsOfAUser(User user){
        List<Post> allPosts = getPostsOfAUser(user);
        return buildResponsePostList(allPosts);
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
