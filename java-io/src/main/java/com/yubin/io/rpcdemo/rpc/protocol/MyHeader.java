package com.yubin.io.rpcdemo.rpc.protocol;

import java.io.Serializable;
import java.util.UUID;

/**
 * RPC协议消息头的封装
 *
 * @author YUBIN
 * @create 2021-01-17
 */
public class MyHeader implements Serializable {
    /**
     * 1: ooxx值(标识是什么样的协议(二进制值))
     * 2: UUID: requestID
     * 3: DATA_LEN
     */
    private int flag;  // 32bit可以设置很多信息。。。
    private long requestID; // 请求的唯一标识
    private long dataLen; // 消息体的字节数


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getRequestID() {
        return requestID;
    }

    public void setRequestID(long requestID) {
        this.requestID = requestID;
    }

    public long getDataLen() {
        return dataLen;
    }

    public void setDataLen(long dataLen) {
        this.dataLen = dataLen;
    }

    public static MyHeader createHeader(byte[] msg){
        MyHeader header = new MyHeader();
        int size = msg.length;
        int f = 0x14141414;
        long requestID =  Math.abs(UUID.randomUUID().getLeastSignificantBits());
        //0x14  0001 0100
        header.setFlag(f);
        header.setDataLen(size);
        header.setRequestID(requestID);
        return header;
    }
}
