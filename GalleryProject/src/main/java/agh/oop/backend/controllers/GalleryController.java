package agh.oop.backend.controllers;


import agh.oop.backend.services.gallery.GalleryService;
import com.google.common.primitives.Bytes;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/images")

public class GalleryController {
    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<Integer> postImage(@RequestBody Map<String, String> imgData) {
        String bytesStr = imgData.get("bytes");
        String imgName = imgData.get("name");
        List<Byte> listOfBytes = Bytes.asList(Base64.decodeBase64(bytesStr));
        int id = galleryService.upload(listOfBytes, imgName);
        if (id != -1)
            return ResponseEntity.ok().body(id);
        return ResponseEntity.status(500).body(id);
    }

    @RequestMapping(value = "/miniatures/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Byte>> getMiniature(@PathVariable int id, @RequestParam int width, @RequestParam int height){
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
    public ResponseEntity<Map<Integer, String>> getBooks(@PageableDefault(page = 0, size = 4) Pageable pageable) {
        return ResponseEntity.ok().body(galleryService.getImages(pageable));
    }

}
