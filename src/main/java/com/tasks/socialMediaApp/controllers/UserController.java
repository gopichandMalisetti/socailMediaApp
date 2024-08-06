package com.tasks.socialMediaApp.controllers;

import com.tasks.socialMediaApp.jwt.JwtUtils;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.requestModel.LoginRequest;
import com.tasks.socialMediaApp.requestModel.RefreshTokenRequest;
import com.tasks.socialMediaApp.responseModel.LoginResponse;
import com.tasks.socialMediaApp.responseModel.ResponseUserDetails;
import com.tasks.socialMediaApp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(value = "/socialMediaApp/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    JwtUtils jwtUtils;
    UserService userService;

    @Autowired
    UserController(UserService userService, JwtUtils jwtUtils){
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/addUser")
    private ResponseEntity<?> addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @DeleteMapping("/deleteUser")
    private ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails){

        userService.handleDeleteUser(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("successfully deleted!");
    }

    @GetMapping("/searchUser/{userName}")
    private ResponseEntity<?> searchUser(@PathVariable String userName){

        List<ResponseUserDetails> responseUserDetails = userService.findUsers(userName);
        return ResponseEntity.ok(responseUserDetails);
    }


    @PostMapping("/userLogin")
    private ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){

        LoginResponse loginResponse = userService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/refresh-token")
    private ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){

        LoginResponse loginResponse = userService.createAccessTokenWithRefreshToken(refreshTokenRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
