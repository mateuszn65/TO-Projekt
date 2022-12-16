package agh.oop.backend.services.converter;

import agh.oop.backend.services.converter.ImageConverter;

public interface ConverterQueueSubscriber {
    void notifyToConvert(ImageConverter converter);
}
