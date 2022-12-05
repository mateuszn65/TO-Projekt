package agh.oop.backend;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ImageConverterRunner implements ConverterQueueSubscriber {

    private ExecutorService executor;

    public ImageConverterRunner(int threadPool) {
        executor = Executors.newFixedThreadPool(threadPool);
    }

    public void notifyToConvert(ImageConverter converter) {
        executor.submit(converter);
    }
}
