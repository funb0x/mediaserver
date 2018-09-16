package com.funb0x.raspberrypi.image;

import com.funb0x.raspberrypi.listner.ImageListener;
import com.funb0x.utils.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class VideoFileImagePublisher implements ImagePublisher {


    private static volatile VideoFileImagePublisher instance = new VideoFileImagePublisher();
    private final List<ImageListener> imageListeners = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private VideoFileImagePublisher() {
        executorService.submit(getProducerTask());
    }

    public static VideoFileImagePublisher getInstance() {
        return instance;
    }

    private Runnable getProducerTask() {
        return () -> {

            BufferedInputStream bis = null;
            try {
                //inputStream = new FileInputStream(new File("admiral.264"));
                for (int i = 0; i < 10; i++) {

                    bis = new BufferedInputStream(FileUtils.getResourceStream("admiral.264"));
                    byte[] bytes = new byte[960 * 540];
                    final int[] read = new int[1];
                    while ((read[0] = bis.read(bytes)) != -1) {
                        //executorService.submit(new PublisherTask(Arrays.copyOf(bytes, read), imageListeners));
                        imageListeners.forEach(listener -> listener.onImage(Arrays.copyOf(bytes, read[0])));
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
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
            System.out.println(imageBytes.length);
            queues.forEach(listener -> listener.onImage(imageBytes));
        }
    }

}
