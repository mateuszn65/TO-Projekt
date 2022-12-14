package agh.oop.backend;


import com.google.common.primitives.Bytes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping(path = "/images")

public class GalleryController {

    private final GalleryService galleryService;
    private final GalleryRepository galleryRepository;

    public GalleryController(GalleryService galleryService, GalleryRepository galleryRepository) {
        this.galleryService = galleryService;
        this.galleryRepository = galleryRepository;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.POST)
    public ResponseEntity<Integer> postImage(@RequestBody byte[] imageData, @PathVariable String name){
        System.out.println("received image: " + name);
        return ResponseEntity.accepted().body(4);
        //galleryService.upload();
    }

    @RequestMapping(value = "/miniatures/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Byte>> getMiniature(@PathVariable int id){
        System.out.println("received miniature request for image: " + id);
        try {
//            File file = new File("miniCat.png");
//            FileInputStream fileInputStream = new FileInputStream(file);
//            byte[] buffer = ZipUtils.getImageData(fileInputStream);

            return ResponseEntity.ok().body(getImageData("miniCat.png"));
        }catch (Exception e){
            e.getCause();
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    @RequestMapping(value = "/miniatures/placeholder", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Byte>> getPlaceholder(){
        try {
            return ResponseEntity.ok().body(getImageData("loading.jpg"));

        }catch (Exception e){
            e.getCause();
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Byte>> getOriginalImage(@PathVariable int id){
        System.out.println("Send original image: " + id);
        try {
            return ResponseEntity.ok().body(getImageData("cat.jpg"));
        }catch (Exception e){
            e.getCause();
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    private List<Byte> getImageData(String filename) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(filename));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        byte[] buffer = byteArrayOutputStream.toByteArray();
        List<Byte> res = Bytes.asList(buffer);
        byteArrayOutputStream.close();
        return res;
    }

}
