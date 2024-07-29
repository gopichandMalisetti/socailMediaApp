package com.tasks.socialMediaApp.repositories;

import com.tasks.socialMediaApp.model.Follow;
import com.tasks.socialMediaApp.model.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
}
