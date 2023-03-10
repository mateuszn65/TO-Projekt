package agh.oop.backend.services.converter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ImageConverterRunner implements ConverterQueueSubscriber {
    private final ExecutorService executor;
    private final ImageConverterService imageConverterService;

    public ImageConverterRunner(int threadPool, ImageConverterService imageConverterService) {
        this.imageConverterService = imageConverterService;
        executor = Executors.newFixedThreadPool(threadPool);
    }

    public void notifyToConvert(ImageConverter converter) {
        converter.setImageConverterService(imageConverterService);
        executor.submit(converter);
    }
}
