package com.yubin.jvm.classloader;

import java.io.*;

/**
 * 热加载的演示
 *
 * @author YUBIN
 * @create 2020-10-17
 */
public class T011_ClassReloading {

    private static class MyClassLoader extends ClassLoader {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            File f = new File("E:/git_project/javabase/jvm/out/production/jvm/" + name.replace(".", "/").concat(".class"));
            if (!f.exists()) {
                return super.loadClass(name);
            }
            try {
                InputStream is = new FileInputStream(f);
                byte[] buf = new byte[is.available()];
                is.read(buf);
                return defineClass(name, buf, 0, buf.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return super.loadClass(name);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        MyClassLoader classLoader = new MyClassLoader();
        Class<?> clazz1 = classLoader.loadClass("com.yubin.jvm.classloader.T002_ClassLoaderLevel");
        classLoader = new MyClassLoader();
        Class<?> clazz2 = classLoader.loadClass("com.yubin.jvm.classloader.T002_ClassLoaderLevel");
        System.out.println(clazz1 == clazz2);
    }
}
