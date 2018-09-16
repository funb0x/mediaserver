package com.funb0x.raspberrypi.web;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funb0x.raspberrypi.h264stream.impl.FileStreamFactory;
import com.funb0x.raspberrypi.h264stream.impl.RaspividStremFactory;
import com.funb0x.raspberrypi.image.H264ImagePublisher;
import com.funb0x.raspberrypi.listner.ImageListener;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket
public class WebSocketHandler implements ImageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);
    private final ObjectMapper MAPPER = new ObjectMapper();
    private Session session;
    private H264ImagePublisher publisher;

    private void tearDown() {
            session.close();
            session = null;
            publisher.unSubscribe(this);
    }

    @OnWebSocketClose
    public void onClose(int status, String reason) {
        System.out.println("WebSocket closed, status = " + status + ", reason = " + reason);
        tearDown();
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("WebSocket error " + t);
        tearDown();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        Map<String, Object> message = new HashMap<>();
        message.put("action", "init");
//        message.put("width", "960");
//        message.put("height", "540");
        message.put("width", "640");
        message.put("height", "480");

        this.session = session;
        send(message);
        //RaspberryImagePublisher.getInstance().subscribe(this);
//        publisher = H264ImagePublisher.getInstance(new FileStreamFactory());
        publisher = H264ImagePublisher.getInstance(new RaspividStremFactory());
        publisher.subscribe(this);
        System.out.println("WebSocket connect, from = " + session.getRemoteAddress().getAddress());
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        LOGGER.info("Websocket reseived message {}", message);
        //carController.onControl(message);
    }

    @Override
    public void onImage(byte[] frame) {
        ByteBuffer buf = ByteBuffer.wrap(frame);
        send(buf);
    }

    private void send(Map<String, Object> message) {
        try {
            send(MAPPER.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void send(ByteBuffer buf) {
        try {
            session.getRemote().sendBytes(buf);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void send(String message) {
        session.getRemote().sendStringByFuture(message);
    }

}
