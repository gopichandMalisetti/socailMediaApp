package com.tasks.socialMediaApp.repositories;

import com.tasks.socialMediaApp.model.Image;
import com.tasks.socialMediaApp.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Integer> {

    Image findByPostAndId(Post post, int id);
}
