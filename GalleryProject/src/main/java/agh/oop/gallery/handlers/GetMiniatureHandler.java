package agh.oop.gallery.handlers;

import agh.oop.RetrofitController;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageStatus;
import agh.oop.gallery.model.MiniatureSize;
import com.google.common.primitives.Bytes;
import javafx.application.Platform;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class GetMiniatureHandler implements Callback<List<Byte>> {

    private final GalleryImage galleryImage;
    private final RetrofitController retrofitController;
    private final MiniatureSize size;

    public GetMiniatureHandler(GalleryImage galleryImage, RetrofitController retrofitController, MiniatureSize size) {
        this.galleryImage = galleryImage;
        this.retrofitController = retrofitController;
        this.size = size;
    }

    @Override
    public void onResponse(@NotNull Call<List<Byte>> call, @NotNull Response<List<Byte>> response) {
        if (response.code() == 202){
            Platform.runLater(()-> galleryImage.setImageStatusProperty(ImageStatus.LOADING));
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            retrofitController.getMiniature(galleryImage, size);
        } else if (response.isSuccessful()) {
            if(response.body() == null) {
                throw new RuntimeException("Server returned response with empty body");
            }
            handleReceivedMiniature(response.body(), galleryImage);
        } else {
            throw new RuntimeException("Request was processed unsuccessfully");
        }
    }

    @Override
    public void onFailure(@NotNull Call<List<Byte>> call, @NotNull Throwable t) {
        t.printStackTrace();
    }

    private void handleReceivedMiniature(List<Byte> imgBytes, GalleryImage galleryImage) {
        Platform.runLater(() -> galleryImage.setImageStatusProperty(ImageStatus.LOADING));
        byte[] buffer = Bytes.toArray(imgBytes);
        galleryImage.setMiniImage(buffer);
        Platform.runLater(()-> galleryImage.setImageStatusProperty(ImageStatus.RECEIVED));
    }
}
