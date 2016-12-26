package com.jmonitor.core.util;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by jmonitor on 2016/12/11.
 */
public class StreamUtil {

    public static void writeDate(DataOutputStream out, java.util.Date value) {
        try {
            writeVarint(out, value.getTime());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeDouble(DataOutputStream out, double value) {
        try {
            out.writeDouble(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeInt(DataOutputStream out, int value) {
        try {
            writeVarint(out, value & 0xFFFFFFFFL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeLong(DataOutputStream out, long value) {
        try {
            writeVarint(out, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeString(DataOutputStream out, String value) {
        try {
            out.writeUTF(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeTag(DataOutputStream out, int field, int type) {
        try {
            out.writeByte((field << 2) + type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeVarint(DataOutputStream out, long value) throws IOException {
        while (true) {
            if ((value & ~0x7FL) == 0) {
                out.writeByte((byte) value);
                return;
            } else {
                out.writeByte(((byte) value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

}
