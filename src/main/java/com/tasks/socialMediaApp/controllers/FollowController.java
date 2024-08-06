package com.tasks.socialMediaApp.controllers;

import com.tasks.socialMediaApp.model.Follow;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.responseModel.ResponseFollow;
import com.tasks.socialMediaApp.responseModel.ResponseUser;
import com.tasks.socialMediaApp.services.FollowService;
import com.tasks.socialMediaApp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/followers")
    private ResponseEntity<?> myFollowers(@AuthenticationPrincipal UserDetails userDetails){

        User user = userService.findUserByUserName(userDetails.getUsername());
        List<ResponseUser> responseUsers = followService.fetchFollowers(user);
        return ResponseEntity.ok(responseUsers);
    }

    @GetMapping("/following")
    private ResponseEntity<?> usersIFollow(@AuthenticationPrincipal UserDetails userDetails){

        User user = userService.findUserByUserName(userDetails.getUsername());
        List<ResponseUser> responseUsers = followService.fetchFollowing(user);
        return ResponseEntity.ok(responseUsers);
    }

    @PostMapping("/addFollow/{targetUserId}")
    private ResponseEntity<?> addFollow(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int targetUserId,
                                        @RequestBody Follow follow){

        User user = userService.findUserByUserName(userDetails.getUsername());
        ResponseFollow responseFollow = followService.handleAddFollow(targetUserId,follow,user);
        return ResponseEntity.ok(responseFollow);
    }

    @DeleteMapping("/unFollow/{followedUserId}")
    private ResponseEntity<?> unFollow(@AuthenticationPrincipal UserDetails userDetails,@PathVariable int followedUserId){

        User user = userService.findUserByUserName(userDetails.getUsername());
        followService.handleUnFollowingAUser(user,followedUserId);
        return ResponseEntity.ok("successfully unfollowed");
    }
}