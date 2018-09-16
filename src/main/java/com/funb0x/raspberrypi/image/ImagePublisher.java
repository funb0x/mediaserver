package com.funb0x.raspberrypi.image;


import com.funb0x.raspberrypi.listner.ImageListener;

public interface ImagePublisher {

    void subscribe(ImageListener listener);

    void unSubscribe(ImageListener listener);

}
