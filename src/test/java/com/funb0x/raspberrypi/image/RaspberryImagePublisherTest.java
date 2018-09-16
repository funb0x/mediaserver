package com.funb0x.raspberrypi.image;


import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RaspberryImagePublisherTest {

    @Test
    public void test() throws InterruptedException {
        RaspberryImagePublisher.getInstance();
        Thread.sleep(100000);
    }

}
