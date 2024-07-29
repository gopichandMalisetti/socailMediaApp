package com.tasks.socialMediaApp.controllers;

import com.tasks.socialMediaApp.model.Profile;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.services.ProfileService;
import com.tasks.socialMediaApp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/socialMediaApp/profile")
public class ProfileController {

    UserService userService;
    ProfileService profileService;

    ProfileController(UserService userService, ProfileService profileService){
        this.userService = userService;
        this.profileService = profileService;
    }

    @PutMapping("/editProfile")
    private ResponseEntity<?> editProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Profile profile){

        User user = userService.findUserByUserName(userDetails.getUsername());
        System.out.println(userDetails.getUsername());

        Profile savedProfile = profileService.editProfile(user,profile);
        if(savedProfile == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server problem! please try again after sometime");

        return ResponseEntity.ok( savedProfile);
    }

    @GetMapping("/getProfile")
    private ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails){

        User user = userService.findUserByUserName(userDetails.getUsername());
        System.out.println(userDetails.getUsername());


        Profile profile = null;
        try {
            profile = profileService.getProfile(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("internal server problem! please try again after sometime");
        }

        return ResponseEntity.ok(profile);
    }
}
