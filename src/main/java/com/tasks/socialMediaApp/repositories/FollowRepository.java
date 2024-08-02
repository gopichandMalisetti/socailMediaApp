package com.tasks.socialMediaApp.repositories;

import com.tasks.socialMediaApp.model.Follow;
import com.tasks.socialMediaApp.model.FollowId;
import com.tasks.socialMediaApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    List<Follow> findAllByFollowedUser(User user);

    List<Follow> findAllByFollowingUser(User user);
    void deleteByFollowingUser(User user);
    void deleteByFollowedUser(User user);
}
