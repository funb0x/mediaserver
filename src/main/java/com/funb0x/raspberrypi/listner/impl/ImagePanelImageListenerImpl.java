package com.funb0x.raspberrypi.listner.impl;

import com.funb0x.raspberrypi.image.SwingRaspberrypiExample;
import com.funb0x.raspberrypi.listner.ImageListener;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImagePanelImageListenerImpl implements ImageListener {

    private SwingRaspberrypiExample.ImagePanel panel;

    public ImagePanelImageListenerImpl(SwingRaspberrypiExample.ImagePanel panel) {
        this.panel = panel;
    }

    @Override
    public void onImage(byte[] imageBytes) {
        try {
            panel.updateImage(ImageIO.read(new ByteArrayInputStream(imageBytes)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
