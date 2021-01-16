package com.yubin.io.rpc;

/**
 * @author YUBIN
 * @create 2021-01-16
 */
public class PackMsg {

    private MyHeader header;

    private MyContent content;

    public PackMsg(MyHeader header, MyContent content) {

        this.header = header;
        this.content = content;
    }

    public MyHeader getHeader() {
        return header;
    }

    public void setHeader(MyHeader header) {
        this.header = header;
    }

    public MyContent getContent() {
        return content;
    }

    public void setContent(MyContent content) {
        this.content = content;
    }
}
