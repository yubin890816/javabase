package com.yubin.jvm.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author YUBIN
 * @create 2020-10-21
 */
public class ObjectSizeAgent {
    // java里面把对字节码文件的处理、调试统称为Instrumentation
    private static Instrumentation inst;

    /**
     * 固定方法（由jvm自动调用）
     * @param agentArgs
     * @param _inst
     */
    public static void premain(String agentArgs, Instrumentation _inst) {
        inst = _inst;
    }

    /**
     * 自定义的方法
     * @param o
     * @return
     */
    public static long sizeOf(Object o) {
        return inst.getObjectSize(o);
    }
}
