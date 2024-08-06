package com.tasks.socialMediaApp.controllers;


import com.tasks.socialMediaApp.model.Comment;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.responseModel.ResponseComment;
import com.tasks.socialMediaApp.services.CommentService;
import com.tasks.socialMediaApp.services.PostService;
import com.tasks.socialMediaApp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/socialMediaApp/comments")
public class CommentsController {

    private static final Logger logger = LoggerFactory.getLogger(CommentsController.class);
    UserService userService;
    CommentService commentService;

    CommentsController( UserService userService,CommentService commentService){
        this.userService = userService;
        this.commentService = commentService;
    }

    @PostMapping("/addComment/{postId}")
    private ResponseEntity<?> addComment(@PathVariable int postId, @AuthenticationPrincipal UserDetails userDetails,
                                         @RequestBody Comment comment){

        User user = userService.findUserByUserName(userDetails.getUsername());
        logger.debug("user name " + userDetails.getUsername());
        ResponseComment responseComment = commentService.handleAddComment(postId,comment,user);
        return ResponseEntity.ok(responseComment);
    }

    @GetMapping("/getCommentsOfAPost/{postId}")
    private ResponseEntity<?> getCommentsOfAPost(@PathVariable int postId){

        List<ResponseComment> allResponseComments = commentService.handleGetAllCommentsOfAPost(postId);
        return ResponseEntity.ok(allResponseComments);
    }

    @DeleteMapping("/deleteComment/{commentId}")
    private ResponseEntity<?> deleteComment(@PathVariable int commentId,@AuthenticationPrincipal UserDetails userDetails){

        User user = userService.findUserByUserName(userDetails.getUsername());
        commentService.handleDeleteAComment(user,commentId);
        return ResponseEntity.ok("successfully deleted comment!");
    }

    @PutMapping("/editComment")
    private ResponseEntity<?> editComment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Comment editComment){

        User user = userService.findUserByUserName(userDetails.getUsername());
        commentService.handleEditComment(user,editComment);
        return ResponseEntity.ok("successfully edited comment");
    }
}
