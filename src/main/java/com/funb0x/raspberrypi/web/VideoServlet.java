package com.funb0x.raspberrypi.web;

import com.funb0x.utils.ImageUtils;
import com.funb0x.webcam.image.WebcamImageHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class VideoServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoServlet.class);
    private static final byte[] DELIMITER = ("--frame").getBytes();
    private static final byte[] NEW_LINE = new byte[]{0x0d, 0x0a};
    private static final String RESPONSE_CONTENT_TYPE = "multipart/x-mixed-replace; boundary=frame";
    private static final byte[] FRAME_CONTENT_TYPE = "Content-Type: image/jpeg".getBytes();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String remote = req.getRemoteAddr() + ":" + req.getRemotePort();
        LOGGER.info("Accept HTTP connection from {}", remote);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(RESPONSE_CONTENT_TYPE);
        resp.setHeader("Connection", "close");
        WebcamImageHolder imageHolder = WebcamImageHolder.getInstance();
        OutputStream out = new BufferedOutputStream(resp.getOutputStream());
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        while (true) {
            buffer.reset();
            byte[] imageBytes = ImageUtils.getImageBytes(imageHolder.getImage());
            byte[] content_length = ("Content-Length: " + imageBytes.length).getBytes();

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
        }
    }

}
