package com.yubin.tank;

import java.awt.*;
import java.awt.event.*;

/**
 * 定义坦克窗口继承Frame类
 *
 * @author YUBIN
 * @create 2020-09-23
 */
public class TankFrame extends Frame {

    int x=200, y = 200;

    public TankFrame() {
        // 设置窗口的大小
        this.setSize(800, 600);
        // 设置是否可以人为改变窗口大小(false表示不可以)
        this.setResizable(false);
        // 设置窗口的标题
        this.setTitle("tank war");

        // 将窗口设置为可见
        this.setVisible(true);

        // 添加键盘的监听
        this.addKeyListener(new MyKeyListener());

        // 添加window监听器
        this.addWindowListener(new WindowAdapter() {
            // 点击窗口的红色关闭按钮的时候,退出系统
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * 重写父类的paint方法(该方法是被系统自动调用的 什么时候调用呢？ 窗口打开的时候，窗口发生变化的时候，窗口被唤起的时候)
     * @param g 画笔，系统调用该方法的时候会传递一支画笔
     */
    @Override
    public void paint(Graphics g) {
        System.out.println("paint");
        // 绘制一个矩形 x轴 y轴 宽度 高度（原点在左上角,往右x轴越变越大,向下y轴越变越大）
        g.fillRect(x, y, 50, 50);
        // 让黑框框动起来 x轴方向偏移10 y轴方向偏移10
        x += 20;
        y += 20;
    }

    /**
     * 匿名内部类,添加键盘的监听
     */
    private class MyKeyListener extends KeyAdapter {

        /**
         * 键被按下去的时候调用
         * @param e
         */
        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("key pressed");
        }

        /**
         * 键弹起来的时候调用
         * @param e
         */
        @Override
        public void keyReleased(KeyEvent e) {
            System.out.println("key released");
        }
    }

}