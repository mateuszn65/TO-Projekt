package agh.oop.backend;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public class ImageConverterQueue {
    private List<ConverterQueueSubscriber> subscribers = new ArrayList<>();

    private void notifySubscribers(ImageConverter converter) {
        for(ConverterQueueSubscriber subscriber : subscribers) {
            subscriber.notifyToConvert(converter);
        }
    }

    public void subscribe(ConverterQueueSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void addImage(Image img, CompletableFuture<Image> future, int dest_height, int dest_width) {
        ImageConverter converter = new ImageConverter(img, dest_width, dest_height, future);
        notifySubscribers(converter);
    }
}
