package com.tasks.socialMediaApp.controllers;

import com.tasks.socialMediaApp.model.Follow;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.responseModel.ResponseFollow;
import com.tasks.socialMediaApp.responseModel.ResponseUser;
import com.tasks.socialMediaApp.services.FollowService;
import com.tasks.socialMediaApp.services.UserService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println(userDetails.getUsername());

        Pair<List<User>,Boolean> userFollowersRes = followService.getAUserFollowers(user);

        if(!userFollowersRes.b) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server problem! please try again after sometime");

        List<User> userFollowers = userFollowersRes.a;
        List<ResponseUser> responseUsers = new ArrayList<>();

        for (User follower : userFollowers){
            ResponseUser responseUser = new ResponseUser();
            responseUser.setUserName(follower.getUserName());
            responseUser.setId(follower.getId());
            responseUsers.add(responseUser);
        }
        return ResponseEntity.ok(responseUsers);
    }

    @GetMapping("/usersIFollow")
    private ResponseEntity<?> usersIFollow(@AuthenticationPrincipal UserDetails userDetails){

        User user = userService.findUserByUserName(userDetails.getUsername());
        System.out.println(userDetails.getUsername());

        Pair<List<User>,Boolean> userFollowingRes = followService.getusersIFollow(user);

        if(!userFollowingRes.b) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server problem! please try again after sometime");

        List<User> userFollowing = userFollowingRes.a;
        List<ResponseUser> responseUsers = new ArrayList<>();

        for (User follower : userFollowing){
            ResponseUser responseUser = new ResponseUser();
            responseUser.setUserName(follower.getUserName());
            responseUser.setId(follower.getId());
            responseUsers.add(responseUser);
        }
        return ResponseEntity.ok(responseUsers);
    }

    @PostMapping("/addFollow/{userToFollowId}")
    private ResponseEntity<?> addFollow(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int userToFollowId, @RequestBody Follow follow){

        User user = userService.findUserByUserName(userDetails.getUsername());
        System.out.println(userDetails.getUsername());

        User userToFollow = userService.findUser(userToFollowId);
        if ( userToFollow == null) {
            return ResponseEntity.status(HttpStatus.OK).body("user to follow not found");
        }

        if(followService.checkBothUsersAreSame(user.getId(),userToFollowId)) return ResponseEntity.ok("u can't follow yourself");

        boolean checkIfUserAlreadyFollows;
        try {
            checkIfUserAlreadyFollows = followService.checkUserAlreadyFollows(user,userToFollow);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.ok("internal server problem");
        }

        if(checkIfUserAlreadyFollows) return ResponseEntity.ok("u already follows these user!");

        Follow savedFollow = null;
        try {
            savedFollow = followService.addFollow(user,userToFollow,follow);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server problem");
        }

        ResponseFollow responseFollow = new ResponseFollow();
        responseFollow.setFollowedUserName(savedFollow.getFollowingUser().getUserName());
        responseFollow.setUserName(user.getUserName());
        responseFollow.setFollowedTime(savedFollow.getFollowedTime());

        return ResponseEntity.ok(responseFollow);
    }

    @DeleteMapping("/unFollow/{userIFollowId}")
    private ResponseEntity<?> unFollow(@AuthenticationPrincipal UserDetails userDetails,@PathVariable int userIFollowId){

        User user = userService.findUserByUserName(userDetails.getUsername());
        System.out.println(userDetails.getUsername());

        User userIFollow = userService.findUser(userIFollowId);
        if ( userIFollow == null) {
            return ResponseEntity.status(HttpStatus.OK).body("user you follow not found");
        }

        boolean checkIfUserAlreadyFollows;
        try {
            checkIfUserAlreadyFollows = followService.checkUserAlreadyFollows(user,userIFollow);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server problem");
        }

        if(!checkIfUserAlreadyFollows) return ResponseEntity.ok("you are not following the user to unfollow!");

        try {
            followService.unFollow(user, userIFollow);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server problem");
        }

        return ResponseEntity.ok("successfully unfollowed");
    }
}