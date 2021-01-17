package com.yubin.io.rpcdemo.rpc.transport;

import com.yubin.io.rpcdemo.util.PackMsg;
import com.yubin.io.rpcdemo.rpc.protocol.MyContent;
import com.yubin.io.rpcdemo.rpc.protocol.MyHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.UUID;

/**
 * 解码器(本质上也是一个Handler)
 * 父类里一定有channelRead方法(老的buf在方法执行前拼到新的buf中; 新的buf执行剩下的留存到下一个channelRead里面去; 同时将读取到的每一个message放到list中去)
 *
 * @author YUBIN
 * @create 2021-01-17
 */
public class ServerDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> out) throws Exception {
        String logId = UUID.randomUUID().toString();
        System.out.println(String.format("logId:%s server channel accept start 可读字节数:%s", logId, buf.readableBytes()));
        // 这个95的数据是在客户端发送数据的时候,我打印得到的
        //if (buf.readableBytes() >= 95) {
        while (buf.readableBytes() >= 112) { // 修改成while, 因为从buffer中读取到的可能不止一个客户端线程发送的数据
            byte[] bytes = new byte[112];
            buf.getBytes(buf.readerIndex(), bytes); // 从哪里开始读,读多少 且使用getBytes不会移动指针
            //buf.readBytes(bytes);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream oin = new ObjectInputStream(in);
            MyHeader header = (MyHeader) oin.readObject();
            //System.out.println(String.format("logId:%s server accept @ requestId:%s,header length is 95, body dataLen:%s", logId, header.getRequestID(), header.getDataLen()));
            // decode 在两个方向都使用
            // 通信的协议
            if (buf.readableBytes() >= (header.getDataLen() + 112)) {
                // 代码执行到这里说明buffer里面剩余的内容够一个body
                // 首先移动指针到body开始的位置
                buf.readBytes(112);
                byte[] data = new byte[(int) header.getDataLen()];
                buf.readBytes(data);
                ByteArrayInputStream din = new ByteArrayInputStream(data);
                ObjectInputStream doin = new ObjectInputStream(din);
                //MyContent content = (MyContent) doin.readObject();
                //System.out.println(String.format("logId:%s 从body中取出的name为%s", logId, content.getName()));
                //out.add(new PackMsg(header, content));
                if (header.getFlag() == 0x14141414) { // 接收的
                    MyContent content = (MyContent) doin.readObject();
                    System.out.println(String.format("logId:%s 从body中取出的name为%s", logId, content.getName()));
                    out.add(new PackMsg(header, content));
                } else if (header.getFlag() == 0x14141424) { // 返回的
                    MyContent content = (MyContent) doin.readObject();
                    out.add(new PackMsg(header, content));
                }
            } else {
                System.out.println(String.format("logId:%s else server body 剩余可读字节数:%s ,消息体应该的字节数:%s", logId, buf.readableBytes(), header.getDataLen()));
                break; // 留余的数据会被放到下一个channelRead
            }
        }
    }
}
