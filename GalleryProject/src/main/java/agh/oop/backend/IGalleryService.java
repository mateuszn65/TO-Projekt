package agh.oop.backend;

import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;



public interface IGalleryService {
    @POST("/images/{name}")
    Call<Integer> postImage(@Body byte[] imageData, @Path("name") String name);
    @GET("/images/miniatures/{id}")
    Call<List<Byte>> getImageMiniature(@Path("id") int id);
    @GET("/images/miniatures/placeholder")
    Call<List<Byte>> getImagePlaceholder();
    @GET("/images/{id}")
    Call<List<Byte>> getOriginalImage(@Path("id") int id);

}
