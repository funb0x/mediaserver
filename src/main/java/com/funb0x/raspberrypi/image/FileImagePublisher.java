package com.funb0x.raspberrypi.image;

import com.funb0x.raspberrypi.listner.ImageListener;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class FileImagePublisher implements ImagePublisher {

    private static volatile FileImagePublisher instance = new FileImagePublisher();
    private final List<ImageListener> imageListeners = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private FileImagePublisher() {
        executorService.submit(getProducerTask());
    }

    public static FileImagePublisher getInstance() {
        return instance;
    }

    private Runnable getProducerTask() {
        return () -> {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("testvid.h264");
                byte[] bytes = new byte[32768 * 2];
                int read;
                while ((read = fis.read(bytes)) != -1) {
                        executorService.submit(new PublisherTask(Arrays.copyOf(bytes, read), imageListeners));
                    }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void subscribe(ImageListener listener) {
        imageListeners.add(listener);
    }

    @Override
    public void unSubscribe(ImageListener listener) {
        imageListeners.remove(listener);
    }

    private static class PublisherTask implements Runnable {

        private byte[] imageBytes;
        private List<ImageListener> queues;

        public PublisherTask(byte[] imageBytes, List<ImageListener> queues) {
            this.imageBytes = imageBytes;
            this.queues = queues;
        }

        @Override
        public void run() {
            queues.forEach(listener -> listener.onImage(imageBytes));
        }
    }

}
