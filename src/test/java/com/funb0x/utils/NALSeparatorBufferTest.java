package com.funb0x.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class NALSeparatorBufferTest {

    @Test
    public void test() {
        NALSeparatorBuffer NALSeparatorBufferInstance = new NALSeparatorBuffer();

        Byte out = NALSeparatorBufferInstance.add((byte) 100);
        assertNull(out);
        assertFalse(NALSeparatorBufferInstance.isSeparator());
        out = NALSeparatorBufferInstance.add((byte) 101);
        assertNull(out);
        assertFalse(NALSeparatorBufferInstance.isSeparator());
        out = NALSeparatorBufferInstance.add((byte) 102);
        assertNull(out);
        assertFalse(NALSeparatorBufferInstance.isSeparator());
        out = NALSeparatorBufferInstance.add((byte) 103);
        assertNull(out);
        assertFalse(NALSeparatorBufferInstance.isSeparator());

        out = NALSeparatorBufferInstance.add((byte) 0);
        assertEquals(out, (Byte)(byte) 100);
        assertFalse(NALSeparatorBufferInstance.isSeparator());
        out = NALSeparatorBufferInstance.add((byte) 0);
        assertEquals(out, (Byte)(byte) 101);
        assertFalse(NALSeparatorBufferInstance.isSeparator());
        out = NALSeparatorBufferInstance.add((byte) 0);
        assertEquals(out, (Byte)(byte) 102);
        assertFalse(NALSeparatorBufferInstance.isSeparator());
        out = NALSeparatorBufferInstance.add((byte) 1);
        assertEquals(out,(Byte)(byte) 103);
        assertTrue(NALSeparatorBufferInstance.isSeparator());

        out = NALSeparatorBufferInstance.add((byte) 200);
        assertNull(out);
        assertFalse(NALSeparatorBufferInstance.isSeparator());
        out = NALSeparatorBufferInstance.add((byte) 201);
        assertNull(out);
        assertFalse(NALSeparatorBufferInstance.isSeparator());
        out = NALSeparatorBufferInstance.add((byte) 202);
        assertNull(out);
        assertFalse(NALSeparatorBufferInstance.isSeparator());
        out = NALSeparatorBufferInstance.add((byte) 203);
        assertNull(out);
        assertFalse(NALSeparatorBufferInstance.isSeparator());
    }


}
