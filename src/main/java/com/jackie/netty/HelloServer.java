package com.jackie.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.stereotype.Component;

@Component
public class HelloServer {

    private static class SingletionServer {
        static final HelloServer instance = new HelloServer();
    }

    public static HelloServer getInstance() {
        return SingletionServer.instance;
    }

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private ServerBootstrap server;
    private ChannelFuture f;

    public HelloServer() {
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.server = new ServerBootstrap();
        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("http-codec", new HttpServerCodec())
                                .addLast("mySocketHandler", new MyHelloHandler());
                    }
                });
    }

    public void start() {
        try {
            this.f = server.bind(8087);
            System.err.println("netty websocket start!!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {

    }


}
