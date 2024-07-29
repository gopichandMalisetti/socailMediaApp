package com.tasks.socialMediaApp.repositories;

import com.tasks.socialMediaApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    // we have direct methods for these all without using @Query.
    @Query("SELECT u FROM User u WHERE u.userName LIKE %:name%")
    List<User> findByNameContains(@Param("name") String name);

    Optional<User> findByUserName(String userName);

    @Query(" select u from User u where u.refreshToken.refreshToken = :refreshToken")
    User findByRefreshToken(@Param("refreshToken") String refreshToken);

}
