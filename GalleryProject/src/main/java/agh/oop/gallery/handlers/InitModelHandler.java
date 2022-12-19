package agh.oop.gallery.handlers;

import agh.oop.RetrofitController;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Map;

public class InitModelHandler implements Callback<Map<Integer, String>> {

    private final ImageContainer imageContainer;
    private final RetrofitController retrofitController;

    public InitModelHandler(ImageContainer imageContainer, RetrofitController retrofitController) {
        this.imageContainer = imageContainer;
        this.retrofitController = retrofitController;
    }

    @Override
    public void onResponse(@NotNull Call<Map<Integer, String>> call, @NotNull Response<Map<Integer, String>> response) {
        if(response.isSuccessful()){
            if(response.body() == null) {
                throw new RuntimeException("Server returned response with empty body");
            }
            Map<Integer, String> map = response.body();
            map.forEach((id, filename)->{
                GalleryImage galleryImage = new GalleryImage(id, filename);
                imageContainer.addToGallery(galleryImage);
                retrofitController.getMiniature(galleryImage);
            });
        }
    }

    @Override
    public void onFailure(@NotNull Call<Map<Integer, String>> call, @NotNull Throwable t) {
        t.printStackTrace();
    }
}
