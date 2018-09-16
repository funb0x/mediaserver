package com.funb0x.webcam.image.listener.impl;

import com.funb0x.webcam.image.WebcamImagePublisher;
import com.funb0x.webcam.image.listener.ImageListener;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class SocketImageListenerImpl implements ImageListener {

    private Socket socket;
    private OutputStream outputStream;
    private PrintWriter writer;

    public SocketImageListenerImpl(Socket socket) {
        this.socket = socket;
        try {
            this.socket.setKeepAlive(true);
            this.outputStream = socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        this.writer = new PrintWriter(new OutputStreamWriter(outputStream));

        writer.println("HTTP/1.1 200 OK");
     //   writer.println("Cache-Control: no-cache, no-store, max-age=0, must-revalidate");
        writer.println("Connection: keep-alive");
        writer.println("Content-Type: multipart/x-mixed-replace; boundary=frame");
//        writer.println("");
//        try {
//            socket.setKeepAlive(true);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
        writer.flush();
    }

    @Override
    public void onImage(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", baos);

            outputStream.write("--frame\r\n".getBytes());
            outputStream.write("Content-type: image/jpeg\r\n".getBytes());
            outputStream.write(("Content-length: " + baos.size() + "\r\n\r\n").getBytes());
            outputStream.write(baos.toByteArray());
            outputStream.write("\r\n\r\n".getBytes());
            //    outputStream.flush();

//            writer.println("--frame");
//            writer.println("Content-type: image/jpeg");
//            writer.println("Content-length: " + baos.size());
//            writer.println();
//            writer.flush();
//
//            outputStream.write(baos.toByteArray());
//
//            writer.println();
//            writer.println();
//            //outputStream.flush();
//            writer.flush();
        } catch (Exception e) {
            WebcamImagePublisher.getInstance().unSubscribe(this);
            writer.close();
            try {
                outputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
