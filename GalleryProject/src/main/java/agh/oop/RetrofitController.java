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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


public class RetrofitController {
    private IGalleryService galleryService = ServiceGenerator.createService(IGalleryService.class);

    public void upload(GalleryImage galleryImage){
        try {
            Call<Integer> call = galleryService.postImage(galleryImage.getImageData(), galleryImage.getName());
            call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    int id = response.body();
                    System.out.println("Image " + galleryImage.getName() + " uploaded with id: " + id);
                    galleryImage.setId(id);
                    galleryImage.setImageStatusProperty(ImageStatus.LOADING);
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
    public void getMiniature(GalleryImage galleryImage){

        Call<List<Byte>> call = galleryService.getImageMiniature(galleryImage.getId());
        call.enqueue(new Callback<List<Byte>>() {
            @Override
            public void onResponse(@NotNull Call<List<Byte>> call, @NotNull Response<List<Byte>> response) {
                if(response.isSuccessful()) {
                    System.out.println("received miniature for image: " + galleryImage.getName());

                    assert response.body() != null;
                    byte[] buffer = Bytes.toArray(response.body());
                    galleryImage.setMiniImage(buffer);
                    galleryImage.setImageStatusProperty(ImageStatus.RECEIVED);
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Byte>> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void initModel(ImageContainer imageContainer){

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
