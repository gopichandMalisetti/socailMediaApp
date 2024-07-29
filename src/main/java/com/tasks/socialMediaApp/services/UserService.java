package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.model.Follow;
import com.tasks.socialMediaApp.model.RefreshToken;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Value("${passwordRegex.regexp}")
    private String passwordRegex;

    @Autowired
    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        System.out.println(passwordRegex);

       Pattern pattern =  Pattern.compile(passwordRegex);
       Matcher matcher =  pattern.matcher(password);
        System.out.println(pattern.pattern());
        System.out.println(password);
        boolean matches =  matcher.matches();
        System.out.println(matches);
        return matches;
    }
    public User userRegistration(User user){

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = null;
        try {
            savedUser = userRepository.save(user);
        }catch (Exception e){
            System.out.println("EXCEPTION FOUND :");
            System.out.println(e.getMessage());
            return null;
        }
        return savedUser;
    }

    public boolean deleteUser(int userId){

        if(userRepository.findById(userId).isEmpty()){
            return false;
        }

        try {
            userRepository.deleteById(userId);
        }catch (Exception e){
            System.out.println("EXCEPTION FOUND :");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public User findUser(int userId){

       Optional<User> user =  userRepository.findById(userId);
        return user.orElse(null);

    }

    List<User> findUsersIFollow(User user){

        List<User> users = new ArrayList<>();

        List<Follow> followedUsers =  user.getFollowedUsers();
        for (Follow follow : followedUsers){
            User followedUser = follow.getFollowingUser();
            System.out.println(followedUser.getUserName());
            users.add(followedUser);
        }

        return users;
    }

    List<User> findAUserFollowers(User user){

        List<User> users = new ArrayList<>();

        List<Follow> followingUsers =  user.getFollowingUsers();
        for (Follow follow : followingUsers){
            User followingUser = follow.getFollowedUser();
            System.out.println(followingUser.getUserName());
            users.add(followingUser);
        }

        return users;
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
}

//        List<Follow> followedUsers = u2.getFollowedUsers();
//        List<Follow> followingUsers = u2.getFollowingUsers();
//
//        System.out.println("--------- user names of whom i am following -------------- ");
//        for (Follow follow : followedUsers){
//            User followedUser = follow.getFollowingUser();
//            System.out.println(followedUser.getUserName());
//        }

//        System.out.println("----------- my followers user name -----------------");
//        for (Follow follow : followingUsers){
//            User followingUser = follow.getFollowedUser();
//            System.out.println(followingUser.getUserName());
//        }