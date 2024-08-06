package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.Exceptions.InternalServerException;
import com.tasks.socialMediaApp.model.Profile;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    UserRepository userRepository;

    ProfileService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Profile handleEditProfile(User user, Profile profile){
        Profile savedProfile = editProfile(user,profile);
        if(savedProfile == null) throw new InternalServerException("internal server problem");
        return savedProfile;
    }

    public Profile fetchProfile(User user){
        Profile profile = null;
        try {
            profile = getProfile(user);
        }catch (Exception e){
            throw new InternalServerException("internal server problem");
        }
        return profile;
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
