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
import org.springframework.context.annotation.Lazy;
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
          logger.debug(userDetails.getUsername());
          Post savedPost = postService.addPost(requestPost,user);

          if( savedPost != null){
              ResponsePost responsePost = postService.buildResponsePost(savedPost);
              return ResponseEntity.status(HttpStatus.CREATED).body(responsePost);
          }
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body("internal server problem! please try again after sometime");
      }

      @GetMapping("/getPostOfAUser/{postId}")
      private ResponseEntity<?> getPostOfAUser(@AuthenticationPrincipal UserDetails userDetails,@PathVariable int postId){

          User user = userService.findUserByUserName(userDetails.getUsername());
          System.out.println(userDetails.getUsername());
          Optional<Post> optionalPost;
          try {
              optionalPost = postService.findPostOfAUser(postId,user.getId());
          }catch (Exception e){
              logger.error(e.getMessage());
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body("internal server problem! please try again after sometime");
          }

          if(optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found for these user! please enter valid post id");
          Post post = optionalPost.get();
          ResponsePost responsePost = postService.buildResponsePost(post);

          return ResponseEntity.ok(responsePost);

      }

      @DeleteMapping("/deletePost/{postId}")
      private ResponseEntity<String> deletePost(@PathVariable int postId,@AuthenticationPrincipal UserDetails userDetails){

          User user = userService.findUserByUserName(userDetails.getUsername());
          Optional<Post> optionalPost;
          try {
              optionalPost = postService.findPostOfAUser(postId,user.getId());
          }catch (Exception e){
              logger.error(e.getMessage());
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body("internal server problem! please try again after sometime");
          }

          if(optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The user has no post with this post ID! please enter valid post id");
          try {
              postService.deletePost(optionalPost.get());
              return ResponseEntity.status(HttpStatus.OK).body("successfully deleted");
          }catch (Exception exception){
              logger.error(exception.getMessage());
          }

          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body("internal server problem! please try again after sometime");

      }

      @PutMapping("/editPost")
      private ResponseEntity<String> editPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Post post){

          User user = userService.findUserByUserName(userDetails.getUsername());

          Optional<Post> optionalPost;
          try {
              optionalPost = postService.findPostOfAUser(post.getId(), user.getId());
          }catch (Exception e){
              logger.error(e.getMessage());
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
      private ResponseEntity<List<ResponsePost>> getPostsFromAllUsers(){

          List<Post> allPosts =   postService.getAllPosts();
          List<ResponsePost> responsePosts = postService.buildResponsePostList(allPosts);
          return ResponseEntity.ok(responsePosts);
      }

      @GetMapping("/getPostFromUsersIFollow")
      private ResponseEntity<?> getPostFromUsersIFollow(@AuthenticationPrincipal UserDetails userDetails){

          User user = userService.findUserByUserName(userDetails.getUsername());
          System.out.println(userDetails.getUsername());
          List<List<Post>> allPosts =  postService.getPostsFromUserIFollow(user);
          List<ResponsePost> responsePosts = postService.buildResponsePostListFromNestedPostList(allPosts);
          return ResponseEntity.ok(responsePosts);
      }

      @GetMapping("/getPostsOfAUser")
    private  ResponseEntity<?> getPostsOfAUser(@AuthenticationPrincipal UserDetails userDetails){

          User user = userService.findUserByUserName(userDetails.getUsername());
          List<Post> allPosts = postService.getPostsOfAUser(user);
          List<ResponsePost> responsePosts = postService.buildResponsePostList(allPosts);
          return ResponseEntity.ok(responsePosts);
      }

}
