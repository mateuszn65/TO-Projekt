package agh.oop.backend;


import com.google.common.primitives.Bytes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/images")

public class GalleryController {
    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.POST)
    public ResponseEntity<Integer> postImage(@RequestBody List<Byte> imageData, @PathVariable String name) throws IOException {
//        System.out.println("received image: " + name);
        int id = galleryService.upload(imageData, name);
        return ResponseEntity.accepted().body(id);
    }

    @RequestMapping(value = "/miniatures/{id}/{width}/{height}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Byte>> getMiniature(@PathVariable int id, @PathVariable int width, @PathVariable int height){
//        System.out.println("received miniature request for image: " + id);
        try {
            List<Byte> bytes = galleryService.getMiniature(id, width, height);

            if (bytes != null){
                return ResponseEntity.ok().body(bytes);
            }
            bytes = new ArrayList<>();

            return ResponseEntity.status(202).body(bytes);
        }catch (Exception e){
            e.getCause();
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    @RequestMapping(value = "/miniatures/placeholder", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Byte>> getPlaceholder(){
        try {
            return ResponseEntity.ok().body(galleryService.getPlaceholder());

        }catch (Exception e){
            e.getCause();
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Byte>> getOriginalImage(@PathVariable int id){
//        System.out.println("Send original image: " + id);
        try {
            return ResponseEntity.ok().body(galleryService.getOriginalImage(id));
        }catch (Exception e){
            e.getCause();
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Map<Integer, String>> getInitialImages(){
        return ResponseEntity.ok().body(galleryService.getInitialImages());
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
