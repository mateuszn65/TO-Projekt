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
    public ResponseEntity<Integer> postImage(@RequestBody List<Byte> imageData, @PathVariable String name) {
        int id = galleryService.upload(imageData, name);
        if (id != -1)
            return ResponseEntity.ok().body(id);
        return ResponseEntity.status(500).body(id);
    }

    @RequestMapping(value = "/miniatures/{id}/{width}/{height}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Byte>> getMiniature(@PathVariable int id, @PathVariable int width, @PathVariable int height){
        try {
            List<Byte> bytes = galleryService.getMiniature(id, width, height);
            if (bytes != null){
                return ResponseEntity.ok().body(bytes);
            }

            return ResponseEntity.accepted().body(List.of());
        }catch (Exception e){
            return ResponseEntity.status(500).body(List.of());
        }
    }
    @RequestMapping(value = "/miniatures/placeholder", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Byte>> getPlaceholder(){
        try {
            return ResponseEntity.ok().body(galleryService.getPlaceholder());
        }catch (Exception e){
            return ResponseEntity.status(500).body(List.of());
        }
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Byte>> getOriginalImage(@PathVariable int id){
        try {
            return ResponseEntity.ok().body(galleryService.getOriginalImage(id));
        }catch (Exception e){
            return ResponseEntity.status(500).body(List.of());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Map<Integer, String>> getInitialImages(){
        return ResponseEntity.ok().body(galleryService.getInitialImages());
    }

}
