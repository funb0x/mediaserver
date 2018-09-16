package com.funb0x.utils;

import java.io.InputStream;

public final class FileUtils {

    private FileUtils () {}

    public static InputStream getResourceStream(String resource) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResourceAsStream(resource);
    }


}
