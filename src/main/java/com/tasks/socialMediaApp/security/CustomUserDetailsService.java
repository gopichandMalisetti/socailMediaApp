package com.tasks.socialMediaApp.security;

import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    CustomUserDetailsService(){

    }
    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

//    @Autowired
//    CustomUserDetailsService(UserRepository userRepository){
//        this.userRepository = userRepository;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        logger.warn("hello form loadUSerByUSerNAme");
//        logger.debug("debug console");
//        logger.info("info conole");
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        System.out.println(user.getUserName() + "  " +user.getPassword());
        return org.springframework.security.core.userdetails.User.withUsername(user.getUserName())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
