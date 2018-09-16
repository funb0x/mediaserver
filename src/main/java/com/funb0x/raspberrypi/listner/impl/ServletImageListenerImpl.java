package com.funb0x.raspberrypi.listner.impl;

import com.funb0x.raspberrypi.listner.ImageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ServletImageListenerImpl implements ImageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.funb0x.webcam.image.listener.impl.ServletImageListenerImpl.class);
    private static final byte[] DELIMITER = ("--frame").getBytes();
    private static final byte[] NEW_LINE = new byte[]{0x0d, 0x0a};
    private static final String RESPONSE_CONTENT_TYPE = "multipart/x-mixed-replace; boundary=frame";
    private static final byte[] FRAME_CONTENT_TYPE = "Content-Type: image/jpeg".getBytes();
    private OutputStream out;
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();


    public ServletImageListenerImpl(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(RESPONSE_CONTENT_TYPE);
        resp.setHeader("Connection", "close");
        this.out = new BufferedOutputStream(resp.getOutputStream());
    }

    @Override
    public void onImage(byte[] imageBytes) {
        byte[] content_length = ("Content-Length: " + imageBytes.length).getBytes();
        buffer.reset();
        try {
            buffer.write(DELIMITER);
            buffer.write(NEW_LINE);
            buffer.write(FRAME_CONTENT_TYPE);
            buffer.write(NEW_LINE);
            buffer.write(content_length);
            buffer.write(NEW_LINE);
            buffer.write(NEW_LINE);
            buffer.write(imageBytes);
            buffer.write(NEW_LINE);

            out.write(buffer.toByteArray());
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
