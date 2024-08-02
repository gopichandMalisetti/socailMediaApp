package com.tasks.socialMediaApp.controllers;

import com.tasks.socialMediaApp.model.Follow;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.responseModel.ResponseFollow;
import com.tasks.socialMediaApp.responseModel.ResponseUser;
import com.tasks.socialMediaApp.services.FollowService;
import com.tasks.socialMediaApp.services.UserService;
import org.antlr.v4.runtime.misc.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/socialMediaApp/follows")
public class FollowController {

    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);
    FollowService followService;
    UserService userService;

    @Autowired
    FollowController(FollowService followService, UserService userService){
        this.followService = followService;
        this.userService = userService;
    }

    @GetMapping("/myFollowers")
    private ResponseEntity<?> myFollowers(@AuthenticationPrincipal UserDetails userDetails){

        User user = userService.findUserByUserName(userDetails.getUsername());
        Pair<List<User>,Boolean> followersWithFlag = followService.getUserFollowersWithFlag(user);
        if(!followersWithFlag.b) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server problem! please try again after sometime");

        List<User> userFollowers = followersWithFlag.a;
        List<ResponseUser> responseUsers = userService.buildResponseUserList(userFollowers);
        return ResponseEntity.ok(responseUsers);
    }

    @GetMapping("/usersIFollow")
    private ResponseEntity<?> usersIFollow(@AuthenticationPrincipal UserDetails userDetails){

        User user = userService.findUserByUserName(userDetails.getUsername());
        Pair<List<User>,Boolean> followedUsersWithFlag = followService.getFollowedUsersWithFlag(user);
        if(!followedUsersWithFlag.b) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server problem! please try again after sometime");

        List<User> followedUsers = followedUsersWithFlag.a;
        List<ResponseUser> responseUsers = userService.buildResponseUserList(followedUsers);
        return ResponseEntity.ok(responseUsers);
    }

    @PostMapping("/addFollow/{targetUserId}")
    private ResponseEntity<?> addFollow(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int targetUserId, @RequestBody Follow follow){

        User user = userService.findUserByUserName(userDetails.getUsername());
        User userToFollow = userService.findUser(targetUserId);
        if ( userToFollow == null) {
            return ResponseEntity.status(HttpStatus.OK).body("user to follow not found");
        }

        if(followService.bothUsersAreSame(user.getId(),targetUserId)) return ResponseEntity.ok("u can't follow yourself");

        boolean userAlreadyFollowing;
        try {
            userAlreadyFollowing = followService.isUserAlreadyFollowing(user,userToFollow);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.ok("internal server problem");
        }

        if(userAlreadyFollowing) return ResponseEntity.ok("u already follows these user!");
        Follow savedFollow = null;
        try {
            savedFollow = followService.addFollow(user,userToFollow,follow);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server problem");
        }
        ResponseFollow responseFollow = followService.buildResponseFollow(savedFollow,user);
        return ResponseEntity.ok(responseFollow);
    }

    @DeleteMapping("/unFollow/{followedUserId}")
    private ResponseEntity<?> unFollow(@AuthenticationPrincipal UserDetails userDetails,@PathVariable int followedUserId){

        User user = userService.findUserByUserName(userDetails.getUsername());
        User followedUser = userService.findUser(followedUserId);
        if ( followedUser == null) {
            return ResponseEntity.status(HttpStatus.OK).body("user you follow not found");
        }
        boolean userAlreadyFollowing;
        try {
            userAlreadyFollowing = followService.isUserAlreadyFollowing(user,followedUser);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server problem");
        }

        if(!userAlreadyFollowing) return ResponseEntity.ok("you are not following the user to unfollow!");
        try {
            followService.unFollow(user, followedUser);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server problem");
        }
        return ResponseEntity.ok("successfully unfollowed");
    }
}