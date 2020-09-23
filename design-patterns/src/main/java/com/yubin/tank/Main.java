package com.yubin.tank;

/**
 * 坦克大战程序入口类
 *
 * @author YUBIN
 * @create 2020-09-22
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个坦克窗口对象
        TankFrame frame = new TankFrame();
        // 每隔50毫秒重新绘制一下窗口
        while (true) {
            Thread.sleep(50);
            frame.repaint();
        }
    }
}
