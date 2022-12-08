package agh.oop.backend;


import org.springframework.web.bind.annotation.*;

@RestController
public class GalleryController {

    private final GalleryService galleryService;
    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }
    @RequestMapping(value = "/addGalleryImage", method = RequestMethod.POST)
    public int addGalleryImage(@RequestBody byte[] imageData){
        System.out.println("received image");
        return 10;
        //galleryService.upload();
    }
}
