package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.model.Profile;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    UserRepository userRepository;

    ProfileService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public Profile editProfile(User user, Profile profile){

        try {
            logger.debug("phno:" + profile.getPhno());
            user.setProfile(profile);
            userRepository.save(user);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }

        return user.getProfile();
    }

    public Profile getProfile(User user){
        return user.getProfile();
    }
}
