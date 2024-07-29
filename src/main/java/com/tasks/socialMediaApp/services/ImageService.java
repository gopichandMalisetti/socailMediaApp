package com.tasks.socialMediaApp.services;

import com.tasks.socialMediaApp.model.Image;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    ImageRepository imageRepository;

    @Autowired
    ImageService(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    public Image addImageToAPost(Image image, Post post){

        Image savedEntity = null;
        try{
            image.setPost(post);
            savedEntity = imageRepository.save(image);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

        return savedEntity;

    }

    public Image findImage(int imageId){

        Optional<Image> optionalImage = imageRepository.findById(imageId);
        return optionalImage.orElse(null);
    }

    public Image findImageByPostId(int imageId, Post post){

        return imageRepository.findByPostAndId(post,imageId);
    }

    public boolean deleteImage(Image image){

        try {
            imageRepository.delete(image);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public List<Image> getImagesOfAPost(Post post){

        List<Image> allImages = null;
        try {
            allImages = post.getImages();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

        return allImages;
    }

    public List<Image> saveImages(List<Image> images,Post post){

        List<Image> savedImages = new ArrayList<>();
        for (Image image : images){
            image.setPost(post);
            savedImages.add(imageRepository.save(image));
        }

        return savedImages;
    }
}
