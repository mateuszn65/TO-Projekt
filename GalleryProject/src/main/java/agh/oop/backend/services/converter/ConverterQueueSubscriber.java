package agh.oop.backend.services.converter;

public interface ConverterQueueSubscriber {
    void notifyToConvert(ImageConverter converter);
}
