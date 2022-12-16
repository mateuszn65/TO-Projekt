package agh.oop.backend.services.converter;


import java.util.ArrayList;
import java.util.List;

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

    public void addImage(int id, byte[] imageData, int dest_width, int dest_height){
        ImageConverter converter = new ImageConverter(id, imageData, dest_width, dest_height);
        notifySubscribers(converter);
    }
}
