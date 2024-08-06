package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.Exceptions.AccessException;
import com.tasks.socialMediaApp.Exceptions.InternalServerException;
import com.tasks.socialMediaApp.Exceptions.NotFoundException;
import com.tasks.socialMediaApp.model.Follow;
import com.tasks.socialMediaApp.model.FollowId;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.FollowRepository;
import com.tasks.socialMediaApp.responseModel.ResponseFollow;
import com.tasks.socialMediaApp.responseModel.ResponseUser;
import org.antlr.v4.runtime.misc.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public List<ResponseUser> fetchFollowers(User user){

        Pair<List<User>,Boolean> followersWithFlag = getUserFollowersWithFlag(user);
        if(!followersWithFlag.b) throw new InternalServerException("internal server problem");

        List<User> userFollowers = followersWithFlag.a;
        return userService.buildResponseUserList(userFollowers);
    }

    public List<ResponseUser> fetchFollowing(User user){

        Pair<List<User>,Boolean> followedUsersWithFlag = getFollowedUsersWithFlag(user);
        if(!followedUsersWithFlag.b) throw new InternalServerException("internal server problem");

        List<User> followedUsers = followedUsersWithFlag.a;
        return userService.buildResponseUserList(followedUsers);
    }

    public ResponseFollow handleAddFollow(int targetUserId, Follow follow, User user){
        User userToFollow = userService.findUser(targetUserId);
        if ( userToFollow == null) {
            throw new NotFoundException("user to follow not found");
        }

        if(bothUsersAreSame(user.getId(),targetUserId)) throw new AccessException("you can't follow yourself");

        boolean userAlreadyFollowing;
        try {
            userAlreadyFollowing = isUserAlreadyFollowing(user,userToFollow);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new InternalServerException("internal server problem");
        }

        if(userAlreadyFollowing) throw new AccessException("you have already following the user");
        Follow savedFollow = null;
        try {
            savedFollow = addFollow(user,userToFollow,follow);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new InternalServerException("internal server problem");
        }
        return buildResponseFollow(savedFollow,user);
    }

    public void handleUnFollowingAUser(User user, int followedUserId){

        User followedUser = userService.findUser(followedUserId);
        if ( followedUser == null) {
            throw new NotFoundException("user you're following not found");
        }
        boolean userAlreadyFollowing;
        try {
            userAlreadyFollowing = isUserAlreadyFollowing(user,followedUser);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new InternalServerException("internal server problem");
        }

        if(!userAlreadyFollowing) throw new AccessException("you are not following the user to unfollow");
        try {
            unFollow(user, followedUser);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new InternalServerException("internal server problem");
        }
    }
}
