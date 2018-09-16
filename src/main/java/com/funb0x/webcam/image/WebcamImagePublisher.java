package com.funb0x.webcam.image;

import com.funb0x.webcam.ImagePublisher;
import com.funb0x.webcam.image.listener.ImageListener;
import com.github.sarxos.webcam.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public final class WebcamImagePublisher implements ImagePublisher, WebcamListener, WebcamUpdater.DelayCalculator {

    private static final int DELAY = 50;
    private static WebcamImagePublisher instance = new WebcamImagePublisher();
    private Logger logger = LoggerFactory.getLogger(WebcamImagePublisher.class);
    private List<ImageListener> imageListeners = new ArrayList<>();

    private WebcamImagePublisher() {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.addWebcamListener(this);
        webcam.open(true, this);
    }

    public static WebcamImagePublisher getInstance() {
        return instance;
    }


    @Override
    public void webcamOpen(WebcamEvent webcamEvent) {
        logger.info("Event {}", webcamEvent.getType());
    }

    @Override
    public void webcamClosed(WebcamEvent webcamEvent) {
        logger.info("Event {}", webcamEvent.getType());
        System.out.println("camera has been closed");
    }

    @Override
    public void webcamDisposed(WebcamEvent webcamEvent) {
        logger.info("Event {}", webcamEvent.getType());
    }

    @Override
    public void webcamImageObtained(WebcamEvent webcamEvent) {
  //      logger.info("Event {}", webcamEvent.getType());
        imageListeners.forEach(imageListener -> imageListener.onImage(webcamEvent.getImage()));
    }

    @Override
    public long calculateDelay(long snapshotDuration, double deviceFps) {
        return Math.max(DELAY - snapshotDuration, 0);
    }

    @Override
    public void subscribe(ImageListener listener) {
        imageListeners.add(listener);
    }

    @Override
    public void unSubscribe(ImageListener listener) {
        imageListeners.remove(listener);
    }
}
