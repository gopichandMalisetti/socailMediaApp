package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.Exceptions.AccessException;
import com.tasks.socialMediaApp.Exceptions.InternalServerException;
import com.tasks.socialMediaApp.Exceptions.NotFoundException;
import com.tasks.socialMediaApp.model.Comment;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.CommentRepository;
import com.tasks.socialMediaApp.responseModel.ResponseComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    CommentRepository commentRepository;
    PostService postService;

    CommentService(CommentRepository commentRepository,@Lazy PostService postService){
        this.commentRepository = commentRepository;
        this.postService = postService;
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

    public ResponseComment handleAddComment(int postId,Comment comment,User user){

        Optional<Post> optionalPost = postService.findPost(postId);
        if (optionalPost.isEmpty()) throw new NotFoundException("post not Found!");

        Post post = optionalPost.get();
        Comment savedComment = addComment(comment,user,post);
        if(savedComment == null) throw new InternalServerException("internal server problem! please try again after sometime");
        return convertToResponseComment(savedComment);

    }

    public List<ResponseComment> handleGetAllCommentsOfAPost(int postId){

        Optional<Post> optionalPost = postService.findPost(postId);
        if (optionalPost.isEmpty()) throw new NotFoundException("post not Found!");
        Post post = optionalPost.get();

        List<Comment> allComments = getAllCommentsOfAPost(post);
        if (allComments == null) throw new InternalServerException("internal server problem! please try again after sometime");
        return buildResponseCommentList(allComments);
    }

    public void handleDeleteAComment(User user,int commentId){

        Comment comment = findComment(commentId);
        if(comment == null) throw new NotFoundException("comment not found");
        if(!Objects.equals(comment.getUser().getId(), user.getId())) throw new AccessException("you can't access these comment");

        if(!deleteComment(comment)) throw new InternalServerException("internal server problem! please try again after sometime");
    }

    public void handleEditComment(User user, Comment editComment){

        Comment comment = findComment(editComment.getId());
        if(comment == null) throw new NotFoundException("comment not found");

        if(!Objects.equals(comment.getUser().getId(), user.getId())) throw new AccessException("you can't access these comment");
        if(!editComment(editComment)) throw new InternalServerException("internal server problem! please try again after sometime");
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
