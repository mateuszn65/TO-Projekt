package agh.oop.backend.services.gallery;

import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.springframework.data.repository.query.Param;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface IGalleryService {
    @POST("/images/upload")
    Call<Integer> postImage(@Body Map<String, String> imageData);
    @GET("/images/miniatures/{id}/{width}/{height}")
    Call<List<Byte>> getImageMiniature(@Path("id") int id, @Path("width") int width, @Path("height") int height);
    @GET("/images/miniatures/placeholder")
    Call<List<Byte>> getImagePlaceholder();
    @GET("/images/{id}")
    Call<List<Byte>> getOriginalImage(@Path("id") int id);
    @GET("/images")
    Call<Map<Integer, String>> getInitialImages();

}
