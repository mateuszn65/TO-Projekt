package agh.oop;

import agh.oop.backend.IGalleryService;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import agh.oop.utils.ImageStatus;
import com.google.common.primitives.Bytes;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import javax.imageio.ImageIO;
import javax.persistence.criteria.CriteriaBuilder;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class RetrofitController {
    private IGalleryService galleryService = ServiceGenerator.createService(IGalleryService.class);

    public void upload(GalleryImage galleryImage){
        try {
            List<Byte> bytes = Bytes.asList(galleryImage.getImageData());
            Call<Integer> call = galleryService.postImage(bytes, galleryImage.getName());
            call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    int id = response.body();
//                    System.out.println("Image " + galleryImage.getName() + " uploaded with id: " + id);
                    galleryImage.setId(id);
                    getMiniature(galleryImage);
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Integer> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
        }catch (Exception e){
            e.getCause();
            e.printStackTrace();
        }
    }

    private void receivedMiniature(@NotNull Response<List<Byte>> response, GalleryImage galleryImage) {
        if(response.isSuccessful()) {
//            System.out.println("received miniature for image: " + galleryImage.getName());
            assert response.body() != null;
            byte[] buffer = Bytes.toArray(response.body());
            galleryImage.setMiniImage(buffer);
            galleryImage.setImageStatusProperty(ImageStatus.RECEIVED);
        } else {
            System.out.println(response.errorBody());
        }
    }

    public void getMiniature(GalleryImage galleryImage){
        Call<List<Byte>> call = galleryService.getImageMiniature(galleryImage.getId(), GalleryImage.miniWidth, GalleryImage.miniHeight);
        call.enqueue(new Callback<List<Byte>>() {
            @Override
            public void onResponse(Call<List<Byte>> call, Response<List<Byte>> response) {
                if (response.code() == 202){
                    galleryImage.setImageStatusProperty(ImageStatus.LOADING);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    getMiniature(galleryImage);
                }else receivedMiniature(response, galleryImage);
            }

            @Override
            public void onFailure(@NotNull Call<List<Byte>> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void initModel(ImageContainer imageContainer){
        Call<Map<Integer, String>> call = galleryService.getInitialImages();
        call.enqueue(new Callback<Map<Integer, String>>() {
            @Override
            public void onResponse(Call<Map<Integer, String>> call, Response<Map<Integer, String>> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    Map<Integer, String> map = response.body();
                    map.forEach((id, filename)->{
                        GalleryImage galleryImage = new GalleryImage(id, filename);
                        imageContainer.addToGallery(galleryImage);
                        getMiniature(galleryImage);
                    });
                }
            }

            @Override
            public void onFailure(Call<Map<Integer, String>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public Image getPlaceholder() throws IOException {
        Response<List<Byte>> response = galleryService.getImagePlaceholder().execute();
        if (!response.isSuccessful()){
            throw new RuntimeException();
        }
        assert response.body() != null;
        byte[] buffer = Bytes.toArray(response.body());
        return new Image(new ByteArrayInputStream(buffer));
    }
    public Image getOriginalImage(int id) throws IOException {
        Response<List<Byte>> response = galleryService.getOriginalImage(id).execute();
        if (!response.isSuccessful()){
            throw new RuntimeException();
        }
        assert response.body() != null;
        byte[] buffer = Bytes.toArray(response.body());
        return new Image(new ByteArrayInputStream(buffer));
    }
}
