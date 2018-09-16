package com.funb0x.raspberrypi.h264stream.impl;

import com.funb0x.raspberrypi.h264stream.H264StreamFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RaspividStremFactory implements H264StreamFactory {

    @Override
    public InputStream getStream() {
        ProcessBuilder processBuilder;

        List<String> command = new ArrayList<>();
        command.add("raspivid");
        command.add("-n");
        command.add("-t");
        command.add("" + 0);
        command.add("-fps");
        command.add("" + 12);
        command.add("-w");
        command.add("" + 640);
        command.add("-h");
        command.add("" + 480);
        command.add("-o");
        command.add("-");
        command.add("-pf");
        command.add("baseline");

        processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return process.getInputStream();
    }
}
