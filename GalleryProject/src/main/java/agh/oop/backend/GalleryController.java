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
    @RequestMapping(value = "/getImageMiniature", method = RequestMethod.GET)
    public byte[] addGalleryImage(@RequestBody String name){
        System.out.println("sending miniature image");
        return null ;
    }

    @RequestMapping(value = "/getOriginalImage", method = RequestMethod.GET)
    public byte[] getOriginalImage(@RequestBody String name){
        System.out.println("sending miniature image");
        return null ;
    }

}
