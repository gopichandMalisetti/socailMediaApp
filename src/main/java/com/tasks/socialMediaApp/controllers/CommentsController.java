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
    PostService postService;
    CommentService commentService;

    CommentsController( UserService userService,PostService postService,CommentService commentService){
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping("/addComment/{postId}")
    private ResponseEntity<?> addComment(@PathVariable int postId, @AuthenticationPrincipal UserDetails userDetails, @RequestBody Comment comment){

        User user = userService.findUserByUserName(userDetails.getUsername());
        logger.debug("user name " + userDetails.getUsername());
        Optional<Post> optionalPost = postService.findPost(postId);
        if (optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found");

        Post post = optionalPost.get();
        Comment savedComment = commentService.addComment(comment,user,post);
        if(savedComment == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server problem! please try again after sometime");
        ResponseComment responseComment = commentService.convertToResponseComment(savedComment);
        return ResponseEntity.ok(responseComment);

    }

    @GetMapping("/getCommentsOfAPost/{postId}")
    private ResponseEntity<?> getCommentsOfAPost(@PathVariable int postId){

        Optional<Post> optionalPost = postService.findPost(postId);
        if (optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found");
        Post post = optionalPost.get();
        List<Comment> allComments = commentService.getAllCommentsOfAPost(post);

        if (allComments == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server problem! please try again after sometime");
        List<ResponseComment> allResponseComments = commentService.buildResponseCommentList(allComments);
        return ResponseEntity.ok(allResponseComments);
    }

    @DeleteMapping("/deleteComment/{commentId}")
    private ResponseEntity<?> deleteComment(@PathVariable int commentId,@AuthenticationPrincipal UserDetails userDetails){

        User user = userService.findUserByUserName(userDetails.getUsername());
        Comment comment = commentService.findComment(commentId);
        if(comment == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("comment not found");
        if(!Objects.equals(comment.getUser().getId(), user.getId())) return ResponseEntity.ok("u can't delete the comment because u not created it");

        if(commentService.deleteComment(comment)) return ResponseEntity.ok("comment deleted successfully");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server problem! please try again after sometime");
    }

    @PutMapping("/editComment")
    private ResponseEntity<?> editComment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Comment editComment){
        User user = userService.findUserByUserName(userDetails.getUsername());
        Comment comment = commentService.findComment(editComment.getId());
        if(comment == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("comment not found");

        if(!Objects.equals(comment.getUser().getId(), user.getId())) return ResponseEntity.ok("u can't edit the comment because u not created it");
        if(commentService.editComment(editComment)) return ResponseEntity.ok("successfully edited comment");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server problem! please try again after sometime");
    }
}
