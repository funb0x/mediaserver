package com.funb0x.raspberrypi.image;

import com.funb0x.raspberrypi.listner.ImageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.funb0x.utils.NALSeparatorBuffer;

public final class RaspberryImagePublisher implements ImagePublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaspberryImagePublisher.class);
    private static volatile RaspberryImagePublisher instance = new RaspberryImagePublisher();
    private final List<ImageListener> imageListeners = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private RaspberryImagePublisher() {
        executorService.submit(getProducerTask());
    }

    public static RaspberryImagePublisher getInstance() {
        return instance;
    }

    private Runnable getProducerTask() {
        return () -> {
            LOGGER.info("Producer task!!!");
            ProcessBuilder processBuilder;
            Process process = null;

            List<String> command = new ArrayList<>();
            command.add("raspivid");
            command.add("-n");
            command.add("-t");
            command.add("" + 0);
            command.add("-fps");
            command.add("" + 12);
            command.add("-w");
            command.add("" + 640);
            command.add("-h");
            command.add("" + 480);
            command.add("-o");
            command.add("-");

            processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            InputStream bis = null;
            try {
                process = processBuilder.start();
                bis = process.getInputStream();
                NALSeparatorBuffer nalSeparatorBuffer = new NALSeparatorBuffer();
                List<Byte> buffer = new LinkedList<>();
                List<Byte> frame = new ArrayList<>();
                Byte fromBuffer;



                byte read;
                while ((read = (byte) bis.read()) != -1) {
                    System.out.println(read);
                    fromBuffer = nalSeparatorBuffer.add(read);
                    if (fromBuffer !=  null) {
                        buffer.add(fromBuffer);
                        if (nalSeparatorBuffer.isSeparator()) {
                            frame.addAll(nalSeparatorBuffer.getNALSeparator());
                            frame.addAll(buffer);
                            executorService.submit(new PublisherTask(getArray(frame), imageListeners));
                            buffer.clear();
                            frame.clear();
                        }
                    }
                }



//                int read;
//                byte[] bytes = new byte[24000];
//                while ((read = bis.read(bytes)) != -1) {
//                    System.out.println("read = " + read);
//                    for (int i = 0; i < read; i++) {
//                        fromBuffer = nalSeparatorBuffer.add(bytes[i]);
//                        if (fromBuffer != null) {
//                            buffer.add(fromBuffer);
//                            if (nalSeparatorBuffer.isSeparator()) {
//                                System.out.println("NAL sep i = " + i);
//                                frame.addAll(nalSeparatorBuffer.getNALSeparator());
//                                frame.addAll(buffer);
//                                System.out.println("frame size = " + frame.size());
//                                executorService.submit(new PublisherTask(getArray(frame), imageListeners));
//                                frame.clear();
//                                buffer.clear();
//                            }
//                        }
//                    }
//                }
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
                if (process != null) {
                    process.destroy();
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

