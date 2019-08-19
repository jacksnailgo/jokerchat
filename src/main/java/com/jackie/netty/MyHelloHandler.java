package com.jackie.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class MyHelloHandler extends SimpleChannelInboundHandler<Object> {

    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        Channel channel = ctx.channel();

        System.out.println(channel.remoteAddress());

        ByteBuf content = Unpooled.copiedBuffer("hello-netty!", CharsetUtil.UTF_8);

        //构建HttpResponse
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST, content);

        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.writableBytes());
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//        response.headers().set(HttpHeaderNames.CONTENT_BASE,"11111");
//        response.headers().set(HttpHeaderNames.CONTENT_LANGUAGE,"chinese");

        //把响应冲刷到客户端
        channel.writeAndFlush(response);


    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        System.out.println("注册");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println("解除注册");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("channel活跃");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("channel不活跃了");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel读完了");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("读写改变了");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}
