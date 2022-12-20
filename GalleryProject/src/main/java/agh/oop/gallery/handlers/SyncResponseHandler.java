package agh.oop.gallery.handlers;

import com.google.common.primitives.Bytes;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;

import java.io.ByteArrayInputStream;
import java.util.List;

public class SyncResponseHandler {
    @NotNull
    public static Image getImage(Response<List<Byte>> response) {
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
