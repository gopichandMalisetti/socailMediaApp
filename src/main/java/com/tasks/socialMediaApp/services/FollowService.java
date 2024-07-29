package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.model.Follow;
import com.tasks.socialMediaApp.model.FollowId;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.FollowRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    FollowRepository followRepository;
    UserService userService;

    @Autowired
    FollowService(FollowRepository followRepository, UserService userService){
        this.followRepository = followRepository;
        this.userService = userService;
    }

    public Pair<List<User>,Boolean> getAUserFollowers(User user){

        List<User> userFollowers;
        try{
            userFollowers = userService.findAUserFollowers(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new Pair<>(null,false);
        }

        return new Pair<>(userFollowers,true);
    }

    public Pair<List<User>,Boolean> getusersIFollow(User user){

        List<User> userFollowing;
        try{
            userFollowing = userService.findUsersIFollow(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new Pair<>(null,false);
        }

        return new Pair<>(userFollowing,true);
    }

    public boolean checkBothUsersAreSame(int userId, int userToFollowId){

        return userId == userToFollowId;
    }

    public boolean checkUserAlreadyFollows(User user, User userToFollow){

        Optional<Follow> optionalFollow = null;
        FollowId followId = new FollowId(user, userToFollow);
        optionalFollow = followRepository.findById(followId);

        return optionalFollow.isPresent();
    }

    public Follow addFollow(User user, User userToFollow, Follow follow){

        FollowId followId = new FollowId(user, userToFollow);
        follow.setFollowId(followId);
        follow.setFollowedUser(user);
        follow.setFollowingUser(userToFollow);

        return followRepository.save(follow);
    }

    public void unFollow(User user, User userIFollow){

        FollowId followId = new FollowId(user, userIFollow);

        followRepository.deleteById(followId);
    }
}
