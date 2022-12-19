package agh.oop;

import agh.oop.backend.services.gallery.IGalleryService;
import agh.oop.gallery.handlers.GetMiniatureHandler;
import agh.oop.gallery.handlers.InitModelHandler;
import agh.oop.gallery.handlers.UploadHandler;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import com.google.common.primitives.Bytes;
import javafx.scene.image.Image;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
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
            JSONObject data = prepareRequestBody(galleryImage.getImageData().get(), galleryImage.getName());
            Call<Integer> call = galleryService.postImage(data);
            call.enqueue(new UploadHandler(galleryImage, this));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private JSONObject prepareRequestBody(byte[] bytes, String name) {
        JSONObject data = new JSONObject();
        String encodedStr = Base64.encodeBase64String(bytes);
        data.append("bytes", encodedStr);
        data.append("name", name);
        return data;
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
        if (!response.isSuccessful()){
            throw new RuntimeException("Request was processed unsuccessfully");
        }
        if(response.body() == null) {
            throw new RuntimeException("Server returned response with empty body");
        }
        byte[] buffer = Bytes.toArray(response.body());
        return new Image(new ByteArrayInputStream(buffer));
    }
    public Image getOriginalImage(int id) throws IOException {
        Response<List<Byte>> response = galleryService.getOriginalImage(id).execute();
        if (!response.isSuccessful()){
            throw new RuntimeException("Request was processed unsuccessfully");
        }
        if(response.body() == null) {
            throw new RuntimeException("Server returned response with empty body");
        }
        byte[] buffer = Bytes.toArray(response.body());
        return new Image(new ByteArrayInputStream(buffer));
    }
}
