package com.funb0x.raspberrypi.image;

import com.funb0x.raspberrypi.h264stream.H264StreamFactory;
import com.funb0x.raspberrypi.listner.ImageListener;
import com.funb0x.utils.NALSeparatorBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class H264ImagePublisher implements ImagePublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(RaspberryImagePublisher.class);
    private static volatile H264ImagePublisher instance;
    private final InputStream is;
    private FileOutputStream before_splitting;
    private FileOutputStream after_splitting;
    private final List<ImageListener> imageListeners = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private H264ImagePublisher(InputStream is) {
        this.is = is;
//        try {
//            before_splitting = new FileOutputStream("d:\\before_splitting.txt");
//            after_splitting = new FileOutputStream("d:\\after_splitting.txt     ");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        executorService.submit(getProducerTask());
    }

    public static H264ImagePublisher getInstance(H264StreamFactory streamFactory) {
        if (instance == null) {
            synchronized (H264ImagePublisher.class) {
                if (instance == null) {
                    instance = new H264ImagePublisher(streamFactory.getStream());
                }
            }
        }
        return instance;
    }

    private Runnable getProducerTask() {
        return () -> {
            try {
                NALSeparatorBuffer nalSeparatorBuffer = new NALSeparatorBuffer();
                List<Byte> buffer = new LinkedList<>();
                List<Byte> frame = new ArrayList<>();
                Byte fromBuffer;

//                byte read;
//                while ((read = (byte) is.read()) != -1) {
//                    before_splitting.write(read);
//                    fromBuffer = nalSeparatorBuffer.add(read);
//                    if (fromBuffer !=  null) {
//                        buffer.add(fromBuffer);
//                        if (nalSeparatorBuffer.isSeparator()) {
//                            frame.addAll(nalSeparatorBuffer.getNALSeparator());
//                            frame.addAll(buffer);
//                            after_splitting.write(getArray(frame));
//                            executorService.submit(new PublisherTask(getArray(frame), imageListeners));
//                            buffer.clear();
//                            frame.clear();
//                        }
//                    }
//                }



                int read;
                byte[] bytes = new byte[24000];
                while ((read = is.read(bytes)) != -1) {
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    //before_splitting.write(bytes,0, read);
                    for (int i = 0; i < read; i++) {
                        fromBuffer = nalSeparatorBuffer.add(bytes[i]);
                        if (fromBuffer != null) {
                            buffer.add(fromBuffer);
                            if (nalSeparatorBuffer.isSeparator()) {
                                frame.addAll(nalSeparatorBuffer.getNALSeparator());
                                frame.addAll(buffer);
                            //    after_splitting.write(getArray(frame));
                                executorService.submit(new PublisherTask(getArray(frame), imageListeners));
                                frame.clear();
                                buffer.clear();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
//                    if (before_splitting != null) {
//                        before_splitting.close();
//                    }
//                    if (after_splitting != null) {
//                        after_splitting.close();
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void subscribe(ImageListener listener) {
        LOGGER.info("New subscriber!!!");
        imageListeners.add(listener);
    }

    @Override
    public void unSubscribe(ImageListener listener) {
        LOGGER.info("Subscriber closed!!!");
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

    public byte[] getArray(List<Byte> byteCollection) {
        byte[] bytes = new byte[byteCollection.size()];
        for (int i = 0; i < byteCollection.size(); i++) {
            bytes[i] = byteCollection.get(i);
        }
        return bytes;
    }

}
