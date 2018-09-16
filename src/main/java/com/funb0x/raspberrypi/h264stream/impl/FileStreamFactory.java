package com.funb0x.raspberrypi.h264stream.impl;


import com.funb0x.raspberrypi.h264stream.H264StreamFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileStreamFactory implements H264StreamFactory {

    @Override
    public InputStream getStream() {
        try {
            //return new FileInputStream("d:\\admiral.h264");
            return new FileInputStream("d:\\out.h264");
            //return new FileInputStream("d:\\video.h264");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
