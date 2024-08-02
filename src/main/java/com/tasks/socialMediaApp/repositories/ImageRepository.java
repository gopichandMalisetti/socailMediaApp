package com.tasks.socialMediaApp.repositories;

import com.tasks.socialMediaApp.model.Image;
import com.tasks.socialMediaApp.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Integer> {

    Image findByPostAndId(Post post, int id);

    List<Image> findAllByPost(Post post);

    void deleteByPost(Post post);
}
