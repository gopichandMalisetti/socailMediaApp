package com.tasks.socialMediaApp.controllers;

import com.tasks.socialMediaApp.jwt.JwtUtils;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.requestModel.LoginRequest;
import com.tasks.socialMediaApp.requestModel.RefreshTokenRequest;
import com.tasks.socialMediaApp.responseModel.LoginResponse;
import com.tasks.socialMediaApp.responseModel.ResponseUserDetails;
import com.tasks.socialMediaApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/socialMediaApp/users")
public class UserController {

    JwtUtils jwtUtils;
    AuthenticationManager authenticationManager;
    UserService userService;

    @Autowired
    UserController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager){
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/addUser")
    private ResponseEntity<?> addUser(@RequestBody User user){

        User createdUser = userService.findUserByUserName(user.getUserName());
        if(createdUser != null) return ResponseEntity.ok("username already exists");


        if(!userService.validatePassword(user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please enter valid password");
//        else return ResponseEntity.ok("valid password");

        User savedUser = userService.userRegistration(user);
        if(savedUser != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server problem! please try again after sometime");
    }

    @DeleteMapping("/deleteUser")
    private ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails){

        User createdUser = userService.findUserByUserName(userDetails.getUsername());
        System.out.println(userDetails.getUsername());
        if(userService.deleteUser(createdUser.getId())){
            return ResponseEntity.status(HttpStatus.OK).body("successfully deleted!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not Found");
    }

//    @PutMapping("/editUser/{userId}")
//    private String editUser(@PathVariable int userId, User user){
//        return null;
//    }

    @GetMapping("/searchUser/{userName}")
    private ResponseEntity<?> searchUser(@PathVariable String userName){

        List<User> users = null;
        try {
            users = userService.searchUser(userName);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("users with these name not found");
        }

        List<ResponseUserDetails> responseUsersDetails = getResponseUserDetails(users);

        return ResponseEntity.ok(responseUsersDetails);
    }

    private static List<ResponseUserDetails> getResponseUserDetails(List<User> users) {
        List<ResponseUserDetails> responseUsersDetails = new ArrayList<>();
        for(User user : users){
            ResponseUserDetails responseUserDetails = new ResponseUserDetails();
            responseUserDetails.setUserName(user.getUserName());
            responseUserDetails.setBio(user.getProfile().getBio());
            responseUserDetails.setFullName(user.getProfile().getFullName());
            responseUserDetails.setPhotoUrl(user.getProfile().getPhotoUrl());
            responseUsersDetails.add(responseUserDetails);
        }
        return responseUsersDetails;
    }

    @PostMapping("/userLogin")
    private ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){

        System.out.println("userLogin :");
        Authentication authentication;
        try{
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword()));
        }catch (AuthenticationException authenticationException){
            Map<String, Object> map = new HashMap<>();
            map.put("message","Bad credentials");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
        String refreshToken = jwtUtils.generateRefreshTokenFromUserName(userDetails.getUsername());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwtToken(jwtToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setUserName(userDetails.getUsername());

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/refresh-token")
    private ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){

        System.out.println(" @@@@@@@@@@@@@@@@@  refresh token " + refreshTokenRequest.getRefreshToken());
        User user = null;
        try {
             user = userService.findByRefreshToken(refreshTokenRequest.getRefreshToken());
        }catch (Exception e){
            System.out.println("Exception : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server problem");
        }

        if(user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");


        String jwtToken = jwtUtils.generateTokenFromUsername(user.getUserName());

        if(jwtUtils.checkExpiryOfRefreshToken(user)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("refresh token expired, please login again");

        jwtUtils.setNewExpiryTime(user);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwtToken(jwtToken);
        loginResponse.setRefreshToken(refreshTokenRequest.getRefreshToken());
        loginResponse.setUserName(user.getUserName());

        return ResponseEntity.ok(loginResponse);

    }
}
