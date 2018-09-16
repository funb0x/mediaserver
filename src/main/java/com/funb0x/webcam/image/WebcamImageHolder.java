package com.funb0x.webcam.image;

import com.funb0x.webcam.ImageHolder;
import com.github.sarxos.webcam.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class WebcamImageHolder implements ImageHolder, WebcamListener, WebcamUpdater.DelayCalculator {

    private static final int DELAY = 50;
    private static WebcamImageHolder instance = new WebcamImageHolder();
    private Logger logger = LoggerFactory.getLogger(WebcamImageHolder.class);
    private BufferedImage bufferedImage;
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();

    private WebcamImageHolder() {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.addWebcamListener(this);
        webcam.open(true, this);
        bufferedImage = webcam.getImage();
    }

    public static WebcamImageHolder getInstance() {
        return instance;
    }

    @Override
    public BufferedImage getImage() {
        readLock.lock();
        try {
            return bufferedImage;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void webcamOpen(WebcamEvent webcamEvent) {
        logger.info("Event {}", webcamEvent.getType());
    }

    @Override
    public void webcamClosed(WebcamEvent webcamEvent) {
        logger.info("Event {}", webcamEvent.getType());
    }

    @Override
    public void webcamDisposed(WebcamEvent webcamEvent) {
        logger.info("Event {}", webcamEvent.getType());
    }

    @Override
    public void webcamImageObtained(WebcamEvent webcamEvent) {
        //logger.info("Event {}", webcamEvent.getType());
        writeLock.lock();
        try {
            this.bufferedImage = webcamEvent.getImage();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public long calculateDelay(long snapshotDuration, double deviceFps) {
        return Math.max(DELAY - snapshotDuration, 0);
    }
}