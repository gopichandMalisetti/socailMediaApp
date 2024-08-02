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

        User createdUser = userService.findUserByUserName(user.getUserName());
        if(createdUser != null) return ResponseEntity.ok("username already exists");
        if(!userService.validatePassword(user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please enter strong password");

        User savedUser = userService.userRegistration(user);
        if(savedUser != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server problem! please try again after sometime");
    }

    @DeleteMapping("/deleteUser")
    private ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails){

        User savedUser = userService.findUserByUserName(userDetails.getUsername());
        if(userService.deleteUser(savedUser)){
            return ResponseEntity.status(HttpStatus.OK).body("successfully deleted!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not Found");
    }

    @GetMapping("/searchUser/{userName}")
    private ResponseEntity<?> searchUser(@PathVariable String userName){

        List<User> users = null;
        try {
            users = userService.searchUser(userName);
        }catch (Exception e){
            logger.debug(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("users with these name not found");
        }

        List<ResponseUserDetails> responseUsersDetails = userService.buildResponseUserDetails(users);
        return ResponseEntity.ok(responseUsersDetails);
    }


    @PostMapping("/userLogin")
    private ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){

        logger.debug("userLogin process started:");
        LoginResponse loginResponse;
        try{
            loginResponse = userService.validateUserLoginAndBuildLoginResponse(loginRequest);
        }catch (AuthenticationException authenticationException){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("bad credentials");
        }

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/refresh-token")
    private ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){

        String refreshToken = refreshTokenRequest.getRefreshToken();
        logger.debug(" refresh token " + refreshToken);
        User user = null;
        try {
             user = userService.findByRefreshToken(refreshToken);
        }catch (Exception e){
            System.out.println("Exception : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server problem");
        }

        if(user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
        if(jwtUtils.checkExpiryOfRefreshToken(user)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("refresh token expired, please login again");

        LoginResponse loginResponse = userService.setNewExpiryTimeForRefreshTokenAndBuildLoginResponse(user,refreshToken);
        return ResponseEntity.ok(loginResponse);

    }
}
