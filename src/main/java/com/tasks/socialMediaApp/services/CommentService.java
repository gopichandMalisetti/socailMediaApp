package com.tasks.socialMediaApp.services;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.tasks.socialMediaApp.controllers.CommentsController;
import com.tasks.socialMediaApp.model.Comment;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.CommentRepository;
import com.tasks.socialMediaApp.responseModel.ResponseComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    CommentRepository commentRepository;

    CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public Comment addComment(Comment comment, User user, Post post){

        comment.setPost(post);
        comment.setUser(user);

        Comment savedComment;
        try{
            savedComment = commentRepository.save(comment);
        }catch (Exception e){
           logger.error(e.getMessage());
            return null;
        }

        return savedComment;

    }

    public List<Comment> getAllCommentsOfAPost(Post post){

        List<Comment> comments =null;

         try{
             comments =  commentRepository.findAllByPost(post);
        }catch (Exception e){
            logger.error(e.getMessage());
             return null;
         }

         return comments;
    }

    public Comment findComment(int commentId){

        Optional<Comment> comment = null;
        try {
            comment = commentRepository.findById(commentId);
        }catch (Exception e){
           logger.error(e.getMessage());
            return null;
        }

        return comment.orElse(null);

    }

    public boolean deleteComment(Comment comment){

        try{
            commentRepository.delete(comment);
        }catch (Exception e){
           logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    public void deleteCommentsOfAUser(User user){

        commentRepository.deleteByUser(user);
    }

    public void deleteCommentsOfAPost(Post post){

        commentRepository.deleteAllByPost(post);
    }
    public boolean editComment(Comment comment){

        try {
            Comment commentEntity = commentRepository.findById(comment.getId()).get();
            commentEntity.setDescription(comment.getDescription());
            commentRepository.save(commentEntity);
        }catch (Exception e){
           logger.error(e.getMessage());
            return false;
        }

        return true;

    }

    public ResponseComment convertToResponseComment(Comment savedComment){

        ResponseComment responseComment = new ResponseComment();
        responseComment.setDescription(savedComment.getDescription());
        responseComment.setCreatedTime(savedComment.getCreatedTime());
        responseComment.setId(savedComment.getId());

        return responseComment;
    }

    public List<ResponseComment> buildResponseCommentList(List<Comment> allComments){

        return allComments.stream()
                .map(this::convertToResponseComment)
                .collect(Collectors.toList());

    }
}
