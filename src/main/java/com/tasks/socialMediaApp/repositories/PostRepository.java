package com.tasks.socialMediaApp.repositories;

import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {

    @Query("SELECT p FROM Post p WHERE p.id = :id AND p.user.id = :userId")
    Post findByIdAndUser_Id(@Param("id") Integer id, @Param("userId") Integer userId);

    List<Post> findAllByUser(User user);
}
