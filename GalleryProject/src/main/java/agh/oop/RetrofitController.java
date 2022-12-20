package agh.oop;

import agh.oop.backend.services.gallery.IGalleryService;
import agh.oop.gallery.handlers.GetMiniatureHandler;
import agh.oop.gallery.handlers.InitModelHandler;
import agh.oop.gallery.handlers.SyncResponseHandler;
import agh.oop.gallery.handlers.UploadHandler;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import javafx.scene.image.Image;
import org.apache.commons.codec.binary.Base64;
import retrofit2.Call;
import retrofit2.Response;


import java.io.*;
import java.util.List;
import java.util.Map;


public class RetrofitController {
    private final IGalleryService galleryService = ServiceGenerator.createService(IGalleryService.class);

    public void upload(GalleryImage galleryImage){
        try {
            if (galleryImage.getImageData().isEmpty()) {
                throw new RuntimeException("Upload unsuccessfull - no image data found");
            }
            Map<String, String> data = prepareRequestBody(galleryImage.getImageData().get(), galleryImage.getName());
            Call<Integer> call = galleryService.postImage(data);
            call.enqueue(new UploadHandler(galleryImage, this));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Map<String, String> prepareRequestBody(byte[] bytes, String name) {
        String encodedStr = Base64.encodeBase64String(bytes);
        return Map.of("bytes", encodedStr, "name", name);
    }

    public void getMiniature(GalleryImage galleryImage){
        Call<List<Byte>> call = galleryService.getImageMiniature(galleryImage.getId(), GalleryImage.miniatureWidth, GalleryImage.miniatureHeight);
        call.enqueue(new GetMiniatureHandler(galleryImage, this));
    }

    public void initModel(ImageContainer imageContainer){
        Call<Map<Integer, String>> call = galleryService.getInitialImages();
        call.enqueue(new InitModelHandler(imageContainer, this));
    }

    public Image getPlaceholder() throws IOException {
        Response<List<Byte>> response = galleryService.getImagePlaceholder().execute();
        return SyncResponseHandler.getImage(response);
    }

    public Image getOriginalImage(int id) throws IOException {
        Response<List<Byte>> response = galleryService.getOriginalImage(id).execute();
        return SyncResponseHandler.getImage(response);
    }


}
