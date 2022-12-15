package agh.oop.backend;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ImageConverterRunner implements ConverterQueueSubscriber {

    private ExecutorService executor;
    private GalleryService galleryService;

    public ImageConverterRunner(int threadPool, GalleryService galleryService) {
        this.galleryService = galleryService;
        executor = Executors.newFixedThreadPool(threadPool);
    }

    public void notifyToConvert(ImageConverter converter) {
        converter.setGalleryService(galleryService);
        executor.submit(converter);
    }
}
