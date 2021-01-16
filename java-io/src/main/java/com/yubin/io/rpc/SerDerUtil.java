package com.yubin.io.rpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * 序列化/反序列化工具类
 *
 * @author YUBIN
 * @create 2021-01-16
 */
public class SerDerUtil {
    private static ByteArrayOutputStream out = new ByteArrayOutputStream();

    public synchronized static byte[] ser(Object msg) {
        out.reset();
        byte[] bytes = null;
        try {
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(msg);
            bytes = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
