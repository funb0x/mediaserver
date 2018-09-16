package com.funb0x.webcam.web;

import com.funb0x.webcam.image.WebcamImageHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ImageServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("image/jpeg");
        BufferedImage image = WebcamImageHolder.getInstance().getImage();
        OutputStream outputStream = resp.getOutputStream();
        ImageIO.write(image, "jpeg", outputStream);
    }
}
