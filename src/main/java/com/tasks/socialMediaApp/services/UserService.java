package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.jwt.JwtUtils;
import com.tasks.socialMediaApp.model.Follow;
import com.tasks.socialMediaApp.model.RefreshToken;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.UserRepository;
import com.tasks.socialMediaApp.requestModel.LoginRequest;
import com.tasks.socialMediaApp.responseModel.LoginResponse;
import com.tasks.socialMediaApp.responseModel.ResponseUser;
import com.tasks.socialMediaApp.responseModel.ResponseUserDetails;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;
    AuthenticationManager authenticationManager;
    FollowService followService;
    PostService postService;
    LikeService likeService;
    CommentService commentService;

    @Value("${passwordRegex.regexp}")
    private String passwordRegex;

    @Autowired
    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                   JwtUtils jwtUtils, AuthenticationManager authenticationManager,FollowService followService,
                PostService postService,LikeService likeService,CommentService commentService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.followService = followService;
        this.postService = postService;
        this.likeService = likeService;
        this.commentService = commentService;
    }

//    Breaking down its components:
//            ^: indicates the string’s beginning
//            (?=.*[a-z]): makes sure that there is at least one small letter
//            (?=.*[A-Z]): needs at least one capital letter
//            (?=.*\\d): requires at least one digit
//            (?=.*[@#$%^&+=]): provides a guarantee of at least one special symbol
//            .{8,20}: imposes the minimum length of 8 characters and the maximum length of 20 characters
//    $: terminates the string

    public void saveRefreshToken(String userName, RefreshToken refreshToken){
        User user = this.findUserByUserName(userName);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    public boolean validatePassword(String password){

        logger.error(passwordRegex);

        Pattern pattern =  Pattern.compile(passwordRegex);
        Matcher matcher =  pattern.matcher(password);
        logger.error(pattern.pattern());
        logger.error(password);
        boolean matches =  matcher.matches();
        logger.debug("matches " + matches);
        return matches;
    }

    public User userRegistration(User user){

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = null;
        try {
            savedUser = userRepository.save(user);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
        return savedUser;
    }

    @Transactional
    public boolean deleteUser(User user){

        if(!userRepository.findById(user.getId()).isPresent()){
            return false;
        }

        try {
            postService.deletePostsOfAUser(user);
            commentService.deleteCommentsOfAUser(user);
            likeService.deleteLikesOfAUser(user);
            followService.deleteFollowsByUser(user);
            userRepository.deleteById(user.getId());
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public User findUser(int userId){

       Optional<User> user =  userRepository.findById(userId);
        return user.orElse(null);

    }

    List<User> findUsersIFollow(User user){

        List<User> users;
        List<Follow> followedUsers =  followService.findFollowsByFollowedUser(user);
        users = followedUsers.stream()
                .map(Follow::getFollowingUser)
                .collect(Collectors.toList());

        return users;
    }

    List<User> findAUserFollowers(User user){

        List<Follow> followingUsers =  followService.findFollowsByFollowingUser(user);
        return followingUsers.stream()
                .map(Follow::getFollowedUser)
                .collect(Collectors.toList());

    }

    public List<User> searchUser(String userName){

        return userRepository.findByNameContains(userName);
    }

    public User findUserByUserName(String userName){

        Optional<User> optionalUser =  userRepository.findByUserName(userName);
        return optionalUser.orElse(null);
    }

    public User findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }

    public List<ResponseUser> buildResponseUserList(List<User> allUsers){

        return allUsers.stream()
                .map(user -> {
                    ResponseUser responseUser = new ResponseUser();
                    responseUser.setUserName(user.getUserName());
                    responseUser.setId(user.getId());
                    return responseUser;
                })
                .collect(Collectors.toList());
    }

    public List<ResponseUserDetails> buildResponseUserDetails(List<User> users) {

        return users.stream()
                .map(user -> {
                    ResponseUserDetails responseUserDetails = new ResponseUserDetails();
                    responseUserDetails.setUserName(user.getUserName());
                    responseUserDetails.setBio(user.getProfile().getBio());
                    responseUserDetails.setFullName(user.getProfile().getFullName());
                    responseUserDetails.setPhotoUrl(user.getProfile().getPhotoUrl());
                    return responseUserDetails;
                })
                .collect(Collectors.toList());
    }

    public LoginResponse buildLoginResponse(String jwtToken, String refreshToken, String userName){

        return new LoginResponse(jwtToken,refreshToken,userName);
    }

    public LoginResponse validateUserLoginAndBuildLoginResponse(LoginRequest loginRequest){

        Authentication authentication;
        authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
        String refreshToken = jwtUtils.generateRefreshTokenFromUserName(userDetails.getUsername());

        return buildLoginResponse(jwtToken,refreshToken,userDetails.getUsername());
    }

    public LoginResponse setNewExpiryTimeForRefreshTokenAndBuildLoginResponse(User user,String refreshToken){

        jwtUtils.setNewExpiryTime(user);
        String jwtToken = jwtUtils.generateTokenFromUsername(user.getUserName());
        return buildLoginResponse(jwtToken,refreshToken,user.getUserName());
    }
}

//        List<Follow> followedUsers = u2.getFollowedUsers();
//        List<Follow> followingUsers = u2.getFollowingUsers();
//
//        logger.error("--------- user names of whom i am following -------------- ");
//        for (Follow follow : followedUsers){
//            User followedUser = follow.getFollowingUser();
//            logger.error(followedUser.getUserName());
//        }

//        logger.error("----------- my followers user name -----------------");
//        for (Follow follow : followingUsers){
//            User followingUser = follow.getFollowedUser();
//            logger.error(followingUser.getUserName());
//        }