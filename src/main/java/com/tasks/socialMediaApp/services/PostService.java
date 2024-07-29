package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.model.Image;
import com.tasks.socialMediaApp.model.Like;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.PostRepository;
import com.tasks.socialMediaApp.responseModel.ResponseImage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    UserService userService;
    PostRepository postRepository;
    ImageService imageService;

    @Autowired
    PostService(PostRepository postRepository, UserService userService, ImageService imageService){
        this.postRepository = postRepository;
        this.userService = userService;
        this.imageService = imageService;
    }

    @Transactional
    public Post addPost(Post post, Post requestPost){


        Post savedPost = null;
        try {
            savedPost = postRepository.save(post);
            List<Image> imagesAddToPost = imageService.saveImages(requestPost.getImages(),savedPost);
            savedPost.setImages(imagesAddToPost);
            postRepository.save(savedPost);
        }catch (Exception e){
            System.out.println("Exception Found :");
            System.out.println(e.getMessage());
        }

        return savedPost;

    }

    public Optional<Post> findPost(int postId){

        return postRepository.findById(postId);
    }

    public Optional<Post> findPostOfAUser(int id,int userId){

        return Optional.ofNullable(postRepository.findByIdAndUser_Id(id, userId));
    }

    public boolean deletePost(int postId){

         try{
             postRepository.deleteById(postId);
         }catch (Exception e){
             System.out.println("Exception Found:");
             System.out.println(e.getMessage());
             return false;
         }
         return true;
    }

    public boolean checkPostByUserId(int postId, int userId){

        Post post = postRepository.findByIdAndUser_Id(postId,userId);

        return post == null;
    }

    public boolean editPost(Post post){

        Optional<Post> optionalPost = postRepository.findById(post.getId());

        Post postEntity = null;
        if(optionalPost.isPresent()) {
            postEntity = optionalPost.get();
        }

        System.out.println(post.getDetails());
        postEntity.setDetails(post.getDetails());

        try {
            postRepository.save(postEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public List<Post> getAllPosts(){

        return postRepository.findAll();
    }

    public List<List<Post>> getPostsFromUserIFollow(User user){

        List<User> usersIFollow = userService.findUsersIFollow(user);

        List<List<Post>> listOfPosts = new ArrayList<>();
        for(User u : usersIFollow){

            List<Post> currPosts = u.getPosts();
            listOfPosts.add(currPosts);
        }

        return listOfPosts;
    }

    public List<Post> getPostFromAUser(User user){

        return user.getPosts();
    }

    public List<ResponseImage> getResponseImagesOfAPost(Post post){

        List<Image> images = post.getImages();
        List<ResponseImage> responseImages = new ArrayList<>();
        for(Image image : images){
            ResponseImage responseImage = new ResponseImage();
            responseImage.setId(image.getId());
            responseImage.setUrl(image.getUrl());
            responseImages.add(responseImage);
        }

        return responseImages;
    }
}
