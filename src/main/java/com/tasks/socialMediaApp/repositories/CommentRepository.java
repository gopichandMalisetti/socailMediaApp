package com.tasks.socialMediaApp.repositories;

import com.tasks.socialMediaApp.model.Comment;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    List<Comment> findAllByPost(Post post);

    void deleteByUser(User user);

    void deleteAllByPost(Post post);
}