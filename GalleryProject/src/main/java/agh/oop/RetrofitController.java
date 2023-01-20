package agh.oop;

import agh.oop.backend.services.gallery.IGalleryService;
import agh.oop.gallery.handlers.GetMiniatureHandler;
import agh.oop.gallery.handlers.GetImagesHandler;
import agh.oop.gallery.handlers.SyncResponseHandler;
import agh.oop.gallery.handlers.UploadHandler;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import javafx.scene.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import retrofit2.Call;
import retrofit2.Response;


import java.io.*;
import java.util.List;
import java.util.Map;



@Slf4j
public class RetrofitController {
    private final IGalleryService galleryService = ServiceGenerator.createService(IGalleryService.class);

    public void upload(GalleryImage galleryImage, int desiredWidth, int desiredHeight){
        log.info("RetrofitController: uploading image, desired width {}, desired height {}", desiredWidth, desiredHeight);
        try {
            if (galleryImage.getImageData().isEmpty()) {
                throw new RuntimeException("Upload unsuccessfull - no image data found");
            }
            Map<String, String> data = prepareRequestBody(galleryImage.getImageData().get(), galleryImage.getName());
            Call<Integer> call = galleryService.postImage(data);
            call.enqueue(new UploadHandler(galleryImage, this, desiredWidth, desiredHeight));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Map<String, String> prepareRequestBody(byte[] bytes, String name) {
        String encodedStr = Base64.encodeBase64String(bytes);
        return Map.of("bytes", encodedStr, "name", name);
    }

    public void getMiniature(GalleryImage galleryImage, int miniatureWidth, int miniatureHeight){
        log.info("requesting miniature for {} miniatureWidth {}, miniatureHeight {}", galleryImage.getName(), miniatureWidth, miniatureHeight);
        Call<List<Byte>> call = galleryService.getImageMiniature(galleryImage.getId(), miniatureWidth, miniatureHeight);
        call.enqueue(new GetMiniatureHandler(galleryImage, this, miniatureWidth, miniatureHeight));
    }

    public Image getPlaceholder() throws IOException {
        Response<List<Byte>> response = galleryService.getImagePlaceholder().execute();
        return SyncResponseHandler.getImage(response);
    }

    public Image getOriginalImage(int id) throws IOException {
        log.info("Original image with id {} requested", id);
        Response<List<Byte>> response = galleryService.getOriginalImage(id).execute();
        return SyncResponseHandler.getImage(response);
    }

    public void loadMore(ImageContainer imageContainer, int miniatureWidth, int miniatureHeight, int page, int size){
        log.info("Loading more images with page {} size {}", page, size);
        log.info("Initializing model with {} miniatureWidth, {} miniatureHeight", miniatureWidth, miniatureHeight);
        Call<Map<Integer, String>> call = galleryService.getImages(page, size);
        call.enqueue(new GetImagesHandler(imageContainer, this, miniatureWidth, miniatureHeight));
    }


}
