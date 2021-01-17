package com.yubin.io.rpcdemo.rpc.transport;

import com.yubin.io.rpcdemo.util.PackMsg;
import com.yubin.io.rpcdemo.rpc.ResponseMappingCallback;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端接收响应的处理类
 *
 * @author YUBIN
 * @create 2021-01-17
 */
public class ClientResponses extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackMsg responsePkg = (PackMsg) msg;
        ResponseMappingCallback.runCallback(responsePkg);
    }
}
