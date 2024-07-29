package com.tasks.socialMediaApp.services;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.tasks.socialMediaApp.model.Comment;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

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
            System.out.println(e.getMessage());
            return null;
        }

        return savedComment;

    }

    public List<Comment> getAllComments(Post post){

        List<Comment> comments =null;

         try{
             comments =  post.getComments();
        }catch (Exception e){
             System.out.println(e.getMessage());
             return null;
         }

         return comments;
    }

    public Comment findComment(int commentId){

        Optional<Comment> comment = null;
        try {
            comment = commentRepository.findById(commentId);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

        return comment.orElse(null);

    }

    public boolean deleteComment(Comment comment){

        try{
            commentRepository.delete(comment);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public boolean editComment(Comment comment){

        try {
            Comment commentEntity = commentRepository.findById(comment.getId()).get();

            commentEntity.setDescription(comment.getDescription());
            commentRepository.save(commentEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;

    }
}
