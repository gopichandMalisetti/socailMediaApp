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
    private ResponseEntity<?> addLike(@PathVariable int postId, @AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody Like like){

        User user = userService.findUserByUserName(userDetails.getUsername());
        ResponseLike responseLike = likeService.handleAddLikeToAPost(user,postId,like);
        return ResponseEntity.ok(responseLike);
    }


    @DeleteMapping("/unlike/{postId}")
    private ResponseEntity<?> unlike(@PathVariable int postId,@AuthenticationPrincipal UserDetails userDetails){

        User user = userService.findUserByUserName(userDetails.getUsername());
        likeService.handleUnlikeAPost(user,postId);
        return ResponseEntity.ok("successfully unliked the post!");
    }

}
