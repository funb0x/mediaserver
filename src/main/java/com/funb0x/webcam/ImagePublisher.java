package com.funb0x.webcam;

import com.funb0x.webcam.image.listener.ImageListener;

public interface ImagePublisher {

    void subscribe(ImageListener listener);

    void unSubscribe(ImageListener listener);

}
