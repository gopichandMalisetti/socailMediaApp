package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.model.Follow;
import com.tasks.socialMediaApp.model.FollowId;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.FollowRepository;
import com.tasks.socialMediaApp.responseModel.ResponseFollow;
import org.antlr.v4.runtime.misc.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    private static final Logger logger = LoggerFactory.getLogger(FollowService.class);
    FollowRepository followRepository;
    UserService userService;

    @Autowired
    FollowService(FollowRepository followRepository,@Lazy UserService userService){
        this.followRepository = followRepository;
        this.userService = userService;
    }

    public List<Follow> findFollowsByFollowedUser(User user){

        return followRepository.findAllByFollowedUser(user);
    }

    public List<Follow> findFollowsByFollowingUser(User user){

        return followRepository.findAllByFollowingUser(user);
    }

    public Pair<List<User>,Boolean> getUserFollowersWithFlag(User user){

        List<User> userFollowers;
        try{
            userFollowers = userService.findAUserFollowers(user);
        }catch (Exception e){
           logger.error(e.getMessage());
            return new Pair<>(null,false);
        }

        return new Pair<>(userFollowers,true);
    }

    public Pair<List<User>,Boolean> getFollowedUsersWithFlag(User user){

        List<User> userFollowing;
        try{
            userFollowing = userService.findUsersIFollow(user);
        }catch (Exception e){
           logger.error(e.getMessage());
            return new Pair<>(null,false);
        }

        return new Pair<>(userFollowing,true);
    }

    public boolean bothUsersAreSame(int userId, int userToFollowId){

        return userId == userToFollowId;
    }

    public boolean isUserAlreadyFollowing(User user, User userToFollow){

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

    public ResponseFollow buildResponseFollow(Follow followData, User user){

        ResponseFollow responseFollow = new ResponseFollow();
        responseFollow.setFollowedUserName(followData.getFollowingUser().getUserName());
        responseFollow.setUserName(user.getUserName());
        responseFollow.setFollowedTime(followData.getFollowedTime());

        return responseFollow;
    }

    public void deleteFollowsByUser(User user){

        followRepository.deleteByFollowingUser(user);
        followRepository.deleteByFollowedUser(user);
    }

}
