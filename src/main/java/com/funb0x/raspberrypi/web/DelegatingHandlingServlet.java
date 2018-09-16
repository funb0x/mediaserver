package com.funb0x.raspberrypi.web;

import com.funb0x.webcam.image.WebcamImagePublisher;
import com.funb0x.webcam.image.listener.impl.ServletImageListenerImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet
public class DelegatingHandlingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebcamImagePublisher.getInstance().subscribe(new ServletImageListenerImpl(resp));
        while(true);
    }
}
