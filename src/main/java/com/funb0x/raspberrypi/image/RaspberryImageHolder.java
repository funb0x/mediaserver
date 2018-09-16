package com.funb0x.raspberrypi.image;

import com.funb0x.webcam.ImageHolder;
import com.github.sarxos.webcam.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class RaspberryImageHolder implements ImageHolder {

    private static final int DELAY = 50;
    private static RaspberryImageHolder instance = new RaspberryImageHolder();
    private Logger logger = LoggerFactory.getLogger(RaspberryImageHolder.class);
    private BufferedImage bufferedImage;
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();

    private RaspberryImageHolder() {

    }

    public static RaspberryImageHolder getInstance() {
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


}