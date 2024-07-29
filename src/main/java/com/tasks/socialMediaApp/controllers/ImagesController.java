package com.tasks.socialMediaApp.controllers;


import com.tasks.socialMediaApp.model.Image;
import com.tasks.socialMediaApp.model.Post;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.responseModel.ResponseImage;
import com.tasks.socialMediaApp.services.ImageService;
import com.tasks.socialMediaApp.services.PostService;
import com.tasks.socialMediaApp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/socialMediaApp/images")
public class ImagesController {

    UserService userService;
    PostService postService;
    ImageService imageService;

    ImagesController(UserService userService, PostService postService, ImageService imageService){
        this.postService = postService;
        this.userService = userService;
        this.imageService = imageService;
    }


    @PostMapping("/addImage/{postId}")
      private ResponseEntity<?> addImage(@PathVariable int postId, @AuthenticationPrincipal UserDetails userDetails, @RequestBody Image image){

        User user = userService.findUserByUserName(userDetails.getUsername());
        System.out.println(userDetails.getUsername());

        Optional<Post> optionalPost = postService.findPost(postId);
        if (optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found");

        Post post = optionalPost.get();

        if(postService.checkPostByUserId(postId, user.getId())) return ResponseEntity.ok("user doesn't access these " +
                "post so user can't add image to these post");

        Image savedImage = imageService.addImageToAPost(image, post);
        if(savedImage == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server problem! please try again after sometime");

        ResponseImage responseImage = new ResponseImage();
        responseImage.setId(savedImage.getId());
        responseImage.setUrl(savedImage.getUrl());

        return ResponseEntity.ok(responseImage);
      }


      @DeleteMapping("/deleteImage/{postId}/{imageId}")
      private ResponseEntity<?> deleteImage(@PathVariable int imageId, @PathVariable int postId, @AuthenticationPrincipal UserDetails userDetails){

          User user = userService.findUserByUserName(userDetails.getUsername());
          System.out.println(userDetails.getUsername());

          Optional<Post> optionalPost = postService.findPost(postId);
          if (optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found");

          Post post = optionalPost.get();

          if(postService.checkPostByUserId(postId, user.getId())) return ResponseEntity.ok("user doesn't access these " +
                  "post so user can't delete image to these post");

          Image image = imageService.findImageByPostId(imageId,post);
          if(image == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found! enter valid image id");

          if(imageService.deleteImage(image)) return ResponseEntity.ok("successfully deleted image");

          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body("internal server problem! please try again after sometime");


      }

      @GetMapping("/getImages/{postId}")
    private ResponseEntity<?> getImages(@PathVariable int postId){

          Optional<Post> optionalPost = postService.findPost(postId);
          if (optionalPost.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found");

          Post post = optionalPost.get();

          List<Image> allImages = imageService.getImagesOfAPost(post);

          List<ResponseImage> allResponseImages = new ArrayList<>();
          for (Image image : allImages){
              ResponseImage responseImage = new ResponseImage();
              responseImage.setUrl(image.getUrl());
              responseImage.setId(image.getId());
              allResponseImages.add(responseImage);
          }
          return ResponseEntity.ok(allResponseImages);

      }
}
