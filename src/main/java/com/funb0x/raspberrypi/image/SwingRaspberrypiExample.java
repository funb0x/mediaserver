package com.funb0x.raspberrypi.image;

import com.funb0x.raspberrypi.listner.impl.ImagePanelImageListenerImpl;

import javax.swing.*;
import java.awt.*;

// TODO refactor this class
public class SwingRaspberrypiExample extends JFrame {

    private ImagePublisher imagePublisher;

    public static void main(String[] args) {
        SwingRaspberrypiExample swingCameraExample = new SwingRaspberrypiExample();
    }

    public SwingRaspberrypiExample() {
        super("Camera Image");

        imagePublisher = RaspberryImagePublisher.getInstance();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);

        ImagePanel panel = new ImagePanel(640, 480, true);

        imagePublisher.subscribe(new ImagePanelImageListenerImpl(panel));

        add(panel);
        pack();
        setVisible(true);
    }

    public static class ImagePanel extends JPanel {

        private Image image;

        public ImagePanel() {}

        public ImagePanel(int width, int height, boolean opaque) {
            super.setPreferredSize(new Dimension(width, height));
            super.setOpaque(opaque);
        }

        public void updateImage(Image image) {
            this.image = image;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, 640, 480, null);
        }
    }
}
