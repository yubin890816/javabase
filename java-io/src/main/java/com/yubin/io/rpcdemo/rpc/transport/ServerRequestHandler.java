package com.yubin.io.rpcdemo.rpc.transport;

import com.yubin.io.rpcdemo.rpc.Dispatcher;
import com.yubin.io.rpcdemo.rpc.protocol.MyContent;
import com.yubin.io.rpcdemo.rpc.protocol.MyHeader;
import com.yubin.io.rpcdemo.util.PackMsg;
import com.yubin.io.rpcdemo.util.SerDerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

/**
 * 服务端处理请求的Handler
 *
 * @author YUBIN
 * @create 2021-01-17
 */
public class ServerRequestHandler extends ChannelInboundHandlerAdapter {

    private Dispatcher dispatcher;

    public ServerRequestHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    // provider端
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackMsg requestPkg = (PackMsg) msg;
        //System.out.println("server accept arg " + requestPkg.getContent().getArgs()[0]);

        // 如果假设处理完了,要给客户端返回了~ 你需要注意哪些环节？？？
        // bytebuf
        // 因为是RPC,你得有一个requestId
        // 在client那一侧也要解决解码问题
        // 关注rpc通信协议,  来的时候flag是0x14141414

        // 有新的header + content
        String ioThreadName = Thread.currentThread().getName();
        // 方式一、直接在当前方法处理IO和业务以及返回
        // 方式三、自己创建线程池
        // 方式二、使用netty自己的eventLoop来处理业务及返回 (这种方式 IO线程和exec线程还是同一个)
        //ctx.executor().execute(new Runnable() {
        // 方式四、使用group中的其它线程处理
        ctx.executor().parent().next().execute( new Runnable() { // 使用group中的一个线程来处理业务及响应
            @Override
            public void run() {
                String serviceName = requestPkg.getContent().getName();
                String methodName = requestPkg.getContent().getMethodName();
                Object obj = dispatcher.get(serviceName);
                Class<?> clazz = obj.getClass();
                Object result = null;
                try {
                    Method method = clazz.getMethod(methodName, requestPkg.getContent().getParameterTypes());
                    result = method.invoke(obj, requestPkg.getContent().getArgs());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //String execThreadName = Thread.currentThread().getName();
                MyContent resContent = new MyContent();
                //String res = String.format("io thread: %s, exec thread: %s, from args: %s", ioThreadName, execThreadName, requestPkg.getContent().getArgs()[0]);
                //System.out.println(String.format("server response: %s", res));
                resContent.setRes(result);
                byte[] msgBody = SerDerUtil.ser(resContent);

                MyHeader resHeader = new MyHeader();
                resHeader.setRequestID(requestPkg.getHeader().getRequestID());
                resHeader.setFlag(0x14141424);
                resHeader.setDataLen(msgBody.length);
                byte[] msgHeader = SerDerUtil.ser(resHeader);
                ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);
                buf.writeBytes(msgHeader);
                buf.writeBytes(msgBody);

                ctx.writeAndFlush(buf);
            }
        });

    }

}
