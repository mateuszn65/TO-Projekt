package agh.oop;

import agh.oop.backend.IGalleryService;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RetrofitController {
    private IGalleryService galleryService = ServiceGenerator.createService(IGalleryService.class);

    public void upload(GalleryImage galleryImage){
        try {
            Call<Integer> call = galleryService.uploadGalleryImage(galleryImage.getImageData());
            call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()) {
                    int i = response.body();
                    System.out.println("Image sent: " + i);
//                    getMiniature(galleryImage);
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
    public void getMiniature(GalleryImage galleryImage){
        Call<byte[]> call = galleryService.getImageMiniature(galleryImage.getName());
        call.enqueue(new Callback<byte[]>() {
            @Override
            public void onResponse(Call<byte[]> call, Response<byte[]> response) {
                if(response.isSuccessful()) {
                    byte[] buffer = response.body();
                    galleryImage.setMiniImage(buffer);
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<byte[]> call, Throwable t) {

            }
        });
    }

    public void initModel(ImageContainer imageContainer){

    }


}
