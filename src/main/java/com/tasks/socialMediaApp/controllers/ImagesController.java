package com.tasks.socialMediaApp.controllers;


import com.tasks.socialMediaApp.model.Image;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.responseModel.ResponseImage;
import com.tasks.socialMediaApp.services.ImageService;
import com.tasks.socialMediaApp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(value = "/socialMediaApp/images")
public class ImagesController {

    UserService userService;
    ImageService imageService;

    ImagesController( UserService userService, ImageService imageService){
        this.userService = userService;
        this.imageService = imageService;
    }

    @PostMapping("/addImageToExistingPost/{postId}")
      private ResponseEntity<?> addImageToExistingPost(@PathVariable int postId, @AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestBody Image image){

        User user = userService.findUserByUserName(userDetails.getUsername());
        ResponseImage responseImage = imageService.handleAddImageToExistingPost(user,postId,image);
        return ResponseEntity.ok(responseImage);
      }


      @DeleteMapping("/deleteImageOfAPost/{postId}/{imageId}")
      private ResponseEntity<?> deleteImageOfAPost(@PathVariable int imageId, @PathVariable int postId,
                                                   @AuthenticationPrincipal UserDetails userDetails){

          User user = userService.findUserByUserName(userDetails.getUsername());
          imageService.handleDeleteImageOfAPost(user,postId,imageId);
          return ResponseEntity.ok("successfully deleted the image");
      }

      @GetMapping("/getImages/{postId}")
      private ResponseEntity<?> getImages(@PathVariable int postId){

          List<ResponseImage> allResponseImages = imageService.handleGetImages(postId);
          return ResponseEntity.ok(allResponseImages);
      }
}
