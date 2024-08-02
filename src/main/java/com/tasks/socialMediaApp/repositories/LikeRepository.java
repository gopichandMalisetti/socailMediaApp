package com.tasks.socialMediaApp.repositories;

import com.tasks.socialMediaApp.model.Like;
import com.tasks.socialMediaApp.model.LikeId;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

    Like findByUserAndPost(User user, Post post);

    void deleteByUser(User user);

    void deleteByPost(Post post);
}
