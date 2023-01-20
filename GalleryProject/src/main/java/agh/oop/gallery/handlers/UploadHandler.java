package agh.oop.gallery.handlers;

import agh.oop.RetrofitController;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.MiniatureSize;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadHandler implements Callback<Integer> {
    private final GalleryImage galleryImage;
    private final RetrofitController retrofitController;
    private final MiniatureSize size;

    public UploadHandler(GalleryImage galleryImage, RetrofitController retrofitController, MiniatureSize size) {
        this.galleryImage = galleryImage;
        this.retrofitController = retrofitController;
        this.size = size;
    }

    @Override
    public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response) {
        if(response.isSuccessful()) {
            if(response.body() == null) {
                throw new RuntimeException("Server returned response with empty body");
            }
            int id = response.body();
            galleryImage.setId(id);
            retrofitController.getMiniature(galleryImage, size);
        } else {
            throw new RuntimeException("Request was processed unsuccessfully");
        }
    }

    @Override
    public void onFailure(@NotNull Call<Integer> call, @NotNull Throwable t) {
        t.printStackTrace();
    }

}
