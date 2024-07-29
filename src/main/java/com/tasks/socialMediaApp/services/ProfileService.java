package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.model.Profile;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    UserRepository userRepository;

    ProfileService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public Profile editProfile(User user, Profile profile){

        try {
            System.out.println("............." + profile.getPhno());
            user.setProfile(profile);
            userRepository.save(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

        return user.getProfile();
    }

    public Profile getProfile(User user){
        return user.getProfile();
    }
}
