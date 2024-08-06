package com.tasks.socialMediaApp.controllers;

import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.requestModel.RequestPost;
import com.tasks.socialMediaApp.responseModel.ResponsePost;
import com.tasks.socialMediaApp.services.PostService;
import com.tasks.socialMediaApp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/socialMediaApp/posts")
public class PostsController {

    private static final Logger logger = LoggerFactory.getLogger(PostsController.class);
    PostService postService;
    UserService userService;

    @Autowired
    PostsController(PostService postService, UserService userService){
        this.postService = postService;
        this.userService = userService;

    }

      @PostMapping("/addPost")
      private ResponseEntity<?> addPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody RequestPost requestPost){

          User user = userService.findUserByUserName(userDetails.getUsername());
          ResponsePost responsePost = postService.handleAddPost(requestPost,user);
          return ResponseEntity.status(HttpStatus.CREATED).body(responsePost);
      }

      @GetMapping("/getPostOfAUser/{postId}")
      private ResponseEntity<?> getPostOfAUser(@AuthenticationPrincipal UserDetails userDetails,@PathVariable int postId){

          User user = userService.findUserByUserName(userDetails.getUsername());
          ResponsePost responsePost = postService.handleGetAPostOfAUser(user,postId);
          return ResponseEntity.ok(responsePost);

      }

      @DeleteMapping("/deletePost/{postId}")
      private ResponseEntity<String> deletePost(@PathVariable int postId,@AuthenticationPrincipal UserDetails userDetails){

          User user = userService.findUserByUserName(userDetails.getUsername());
          postService.handleDeleteAPost(user, postId);
          return ResponseEntity.ok("successfully deleted the post");
      }

      @PutMapping("/editPost")
      private ResponseEntity<String> editPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Post post){

          User user = userService.findUserByUserName(userDetails.getUsername());
          postService.handleEditPost(user, post);
          return ResponseEntity.ok("edit was successful");
      }

      @GetMapping("/getPostsFromAllUsers")
      private ResponseEntity<List<ResponsePost>> getPostsFromAllUsers(){

          List<ResponsePost> responsePosts = postService.fetchAllPosts();
          return ResponseEntity.ok(responsePosts);
      }

      @GetMapping("/getPostFromUsersIFollow")
      private ResponseEntity<?> getPostFromUserFollows(@AuthenticationPrincipal UserDetails userDetails){

          User user = userService.findUserByUserName(userDetails.getUsername());
          List<ResponsePost> responsePosts = postService.fetchAllPostsFromUserFollows(user);
          return ResponseEntity.ok(responsePosts);
      }

      @GetMapping("/getPostsOfAUser")
    private  ResponseEntity<?> getPostsOfAUser(@AuthenticationPrincipal UserDetails userDetails){

          User user = userService.findUserByUserName(userDetails.getUsername());
          List<ResponsePost> responsePosts = postService.fetchPostsOfAUser(user);
          return ResponseEntity.ok(responsePosts);
      }

}
