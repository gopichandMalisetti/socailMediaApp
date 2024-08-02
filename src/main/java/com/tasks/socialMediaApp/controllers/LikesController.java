package com.tasks.socialMediaApp.controllers;

import com.tasks.socialMediaApp.model.Like;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.responseModel.ResponseLike;
import com.tasks.socialMediaApp.services.LikeService;
import com.tasks.socialMediaApp.services.PostService;
import com.tasks.socialMediaApp.services.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "socialMediaApp/likes/")
public class LikesController {

    UserService userService;
    PostService postService;
    LikeService likeService;

    LikesController( UserService userService, PostService postService, LikeService likeService){
        this.postService = postService;
        this.userService = userService;
        this.likeService = likeService;
    }

    @PostMapping("/addLike/{postId}")
    private ResponseEntity<?> addLike(@PathVariable int postId, @AuthenticationPrincipal UserDetails userDetails, @RequestBody Like like){

        User user = userService.findUserByUserName(userDetails.getUsername());
        Optional<Post> optionalPost = postService.findPost(postId);
        if (optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found! please enter valid postId");

        Post post = optionalPost.get();
        if (likeService.userLikedThePost(user, post)) return ResponseEntity.ok("you have already liked the post");
        Like savedLike = likeService.addLikeToPost(like,user,post);

        if(savedLike != null){
            ResponseLike responseLike = likeService.buildResponseLike(savedLike);
           return ResponseEntity.ok(responseLike);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server problem! please try again after sometime");
    }


    @DeleteMapping("/unlike/{postId}")
    private ResponseEntity<?> unlike(@PathVariable int postId,@AuthenticationPrincipal UserDetails userDetails){

        User user = userService.findUserByUserName(userDetails.getUsername());
        System.out.println(userDetails.getUsername());

        Optional<Post> optionalPost = postService.findPost(postId);
        if(optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found! please enter valid postId");
        Post post = optionalPost.get();

        if(!likeService.userLikedThePost(user,post)) return ResponseEntity.ok("you have not liked the post to unlike it");
        if(likeService.unLikeThePost(user,post)) return ResponseEntity.ok("unliked the post");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server problem! please try again after sometime");
    }

}
