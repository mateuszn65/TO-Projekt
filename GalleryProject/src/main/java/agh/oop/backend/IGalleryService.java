package agh.oop.backend;

import agh.oop.gallery.model.GalleryImage;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javafx.scene.image.Image;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.concurrent.CompletableFuture;


public interface IGalleryService {
    Observable<Image> getImages(int noImages);
    CompletableFuture<Image> upload(Image image, int dest_height, int dest_width);
    //TO DO add name
    @POST("/addGalleryImage")
    Call<Integer> uploadGalleryImage(@Body byte[] imageData);

    @GET("/getImageMiniature")
    Call<byte[]> getImageMiniature(@Body String name);

    @GET("/getOriginalImage")
    Call<byte[]> getOriginalImage(@Body String name);

}
