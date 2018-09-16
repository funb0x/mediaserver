package com.funb0x.utils;

import java.util.*;

public class NALSeparatorBuffer {

    private List<Byte> NALSeparator = Collections.unmodifiableList(Arrays.asList(
            (byte) 0, (byte) 0, (byte) 0, (byte) 1));
    private Deque<Byte> queue = new LinkedList<>();

    public Byte add(byte el) {
        queue.add(el);
        if (queue.size() > 4) {
            return queue.pollFirst();
        }
        return null;
    }

    public boolean isSeparator() {
        if (NALSeparator.equals(queue)) {
            clear();
            return true;
        } else {
            return false;
        }
    }

    private void clear() {
        queue.clear();
    }

    public List<Byte> getNALSeparator() {
        return NALSeparator;
    }

}