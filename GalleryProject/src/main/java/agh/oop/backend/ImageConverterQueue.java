package agh.oop.backend;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public class ImageConverterQueue {
    private List<ConverterQueueSubscriber> subscribers = new ArrayList<>();
    public ImageConverterQueue(){
    }

    private void notifySubscribers(ImageConverter converter) {
        for(ConverterQueueSubscriber subscriber : subscribers) {
            subscriber.notifyToConvert(converter);
        }
    }

    public void subscribe(ConverterQueueSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void addImage(int id, byte[] imageData, int dest_width, int dest_height){
        ImageConverter converter = new ImageConverter(id, imageData, dest_width, dest_height);
        notifySubscribers(converter);
    }
}
