package com.tasks.socialMediaApp.controllers;

import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.responseModel.ResponseImage;
import com.tasks.socialMediaApp.services.PostService;
import com.tasks.socialMediaApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/socialMediaApp/posts")
public class PostsController {

    PostService postService;
    UserService userService;

    @Autowired
    PostsController(PostService postService,UserService userService){
        this.postService = postService;
        this.userService = userService;

    }

      @PostMapping("/addPost")
      private ResponseEntity<?> addPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Post post){

          User user = userService.findUserByUserName(userDetails.getUsername());
          System.out.println(userDetails.getUsername());

          Post postToSave = new Post(post.getDetails(), post.getId(),user);
          Post savedPost = postService.addPost(postToSave,post);

          if( savedPost != null){

              com.tasks.socialMediaApp.responseModel.Post responsePost = new com.tasks.socialMediaApp.responseModel.Post();
              responsePost.setDetials(savedPost.getDetails());
              responsePost.setId(savedPost.getId());
              responsePost.setLikesCount(savedPost.getLikesCount());
              List<ResponseImage> responseImages = postService.getResponseImagesOfAPost(savedPost);
              responsePost.setResponseImages(responseImages);
              return ResponseEntity.status(HttpStatus.CREATED).body(responsePost);
          }

          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body("internal server problem! please try again after sometime");
      }

      @GetMapping("/getPost/{postId}")
      private ResponseEntity<?> getPost(@AuthenticationPrincipal UserDetails userDetails,@PathVariable int postId){

          User user = userService.findUserByUserName(userDetails.getUsername());
          System.out.println(userDetails.getUsername());

          Optional<Post> optionalPost;
          try {
              optionalPost = postService.findPostOfAUser(postId,user.getId());
          }catch (Exception e){
              System.out.println(e.getMessage());
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body("internal server problem! please try again after sometime");
          }

          if(optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found! please enter valid post id");

          Post post = optionalPost.get();
          com.tasks.socialMediaApp.responseModel.Post responsePost = new com.tasks.socialMediaApp.responseModel.Post();
          responsePost.setDetials(post.getDetails());
          responsePost.setId(post.getId());
          responsePost.setLikesCount(post.getLikesCount());
          List<ResponseImage> responseImages = postService.getResponseImagesOfAPost(post);
          responsePost.setResponseImages(responseImages);

          return ResponseEntity.ok(responsePost);

      }

      @DeleteMapping("/deletePost/{postId}")
      private ResponseEntity<String> deletePost(@PathVariable int postId,@AuthenticationPrincipal UserDetails userDetails){

          User user = userService.findUserByUserName(userDetails.getUsername());
          System.out.println(userDetails.getUsername());

          Optional<Post> optionalPost;
          try {
              optionalPost = postService.findPostOfAUser(postId,user.getId());
          }catch (Exception e){
              System.out.println(e.getMessage());
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body("internal server problem! please try again after sometime");
          }

          if(optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found! please enter valid post id");

        if(postService.deletePost(postId)){
              return ResponseEntity.status(HttpStatus.OK).body("successfully deleted");
          }
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body("internal server problem! please try again after sometime");

      }

      @PutMapping("/editPost")
      private ResponseEntity<String> editPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Post post){

          User user = userService.findUserByUserName(userDetails.getUsername());
          System.out.println(userDetails.getUsername());

          Optional<Post> optionalPost;
          try {
              optionalPost = postService.findPostOfAUser(post.getId(), user.getId());
          }catch (Exception e){
              System.out.println(e.getMessage());
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body("internal server problem! please try again after sometime");
          }

          if(optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found! please enter valid post id");

          if(postService.editPost(post)){
              return ResponseEntity.ok("edit was successful");
          }
          return ResponseEntity.ok("edit was unsucessful");
      }

      @GetMapping("/getPostsFromAllUsers")
      private ResponseEntity<List<com.tasks.socialMediaApp.responseModel.Post>> getPostsFromAllUsers(){

          List<Post> allPosts =   postService.getAllPosts();

          List<com.tasks.socialMediaApp.responseModel.Post> responsePosts = new ArrayList<>();
          for (Post post : allPosts){

              com.tasks.socialMediaApp.responseModel.Post responsePost = new com.tasks.socialMediaApp.responseModel.Post();
              responsePost.setDetials(post.getDetails());
              responsePost.setId(post.getId());
              responsePost.setLikesCount(post.getLikesCount());
              List<ResponseImage> responseImages = postService.getResponseImagesOfAPost(post);
              responsePost.setResponseImages(responseImages);
              responsePosts.add(responsePost);
          }

          return ResponseEntity.ok(responsePosts);
      }

      @GetMapping("/getPostFromUsersIFollow")
      private ResponseEntity<?> getPostFromUsersIFollow(@AuthenticationPrincipal UserDetails userDetails){

          User user = userService.findUserByUserName(userDetails.getUsername());
          System.out.println(userDetails.getUsername());

          List<List<Post>> allPosts =  postService.getPostsFromUserIFollow(user);

          List<com.tasks.socialMediaApp.responseModel.Post> responsePosts = new ArrayList<>();

          for (List<Post> posts : allPosts){
              for (Post post : posts){

                  com.tasks.socialMediaApp.responseModel.Post responsePost = new com.tasks.socialMediaApp.responseModel.Post();
                  responsePost.setDetials(post.getDetails());
                  responsePost.setId(post.getId());
                  responsePost.setLikesCount(post.getLikesCount());
                  List<ResponseImage> responseImages = postService.getResponseImagesOfAPost(post);
                  responsePost.setResponseImages(responseImages);
                  responsePosts.add(responsePost);
              }
          }


          return ResponseEntity.ok(responsePosts);
      }

      @GetMapping("/getPostsOfAUser")
    private  ResponseEntity<?> getPostsOfAUser(@AuthenticationPrincipal UserDetails userDetails){

          User user = userService.findUserByUserName(userDetails.getUsername());
          System.out.println(userDetails.getUsername());

          List<com.tasks.socialMediaApp.responseModel.Post> responsePosts = new ArrayList<>();
          List<Post> allPosts = postService.getPostFromAUser(user);

          for (Post post : allPosts){

              com.tasks.socialMediaApp.responseModel.Post responsePost = new com.tasks.socialMediaApp.responseModel.Post();
              responsePost.setDetials(post.getDetails());
              responsePost.setId(post.getId());
              responsePost.setLikesCount(post.getLikesCount());
              List<ResponseImage> responseImages = postService.getResponseImagesOfAPost(post);
              responsePost.setResponseImages(responseImages);
              responsePosts.add(responsePost);
          }

          return ResponseEntity.ok(responsePosts);
      }

}
