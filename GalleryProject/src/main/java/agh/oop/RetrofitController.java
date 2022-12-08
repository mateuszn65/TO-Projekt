package agh.oop;

import agh.oop.backend.GalleryService;
import agh.oop.backend.IGalleryService;
import agh.oop.gallery.model.GalleryImage;
import javafx.scene.image.Image;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;

public class RetrofitController {
    private IGalleryService galleryService = ServiceGenerator.createService(IGalleryService.class);

    public void upload(byte[] imageData){
        try {
            Call<Integer> call = galleryService.uploadGalleryImage(imageData);
            call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()) {
                    int i = response.body();
                    System.out.println("Image sent: " + i);
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
        }catch (Exception e){
            e.getCause();
            e.printStackTrace();
        }
    }


}
