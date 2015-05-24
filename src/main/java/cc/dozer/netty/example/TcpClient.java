package cc.dozer.netty.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Dozer @ 5/24/15
 * mail@dozer.cc
 * http://www.dozer.cc
 */
public class TcpClient {
    private volatile EventLoopGroup workerGroup;

    private volatile Bootstrap bootstrap;

    private volatile boolean closed = false;

    private final String remoteHost;

    private final int remotePort;

    public TcpClient(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public void close() {
        closed = true;
        workerGroup.shutdownGracefully();
        System.out.println("Stopped Tcp Client: " + getServerInfo());
    }

    public void init() {
        closed = false;

        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addFirst(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        super.channelInactive(ctx);
                        ctx.channel().eventLoop().schedule(() -> doConnect(), 1, TimeUnit.SECONDS);
                    }
                });

                //todo: add more handler
            }
        });

        doConnect();
    }

    private void doConnect() {
        if (closed) {
            return;
        }

        ChannelFuture future = bootstrap.connect(new InetSocketAddress(remoteHost, remotePort));

        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    System.out.println("Started Tcp Client: " + getServerInfo());
                } else {
                    System.out.println("Started Tcp Client Failed: " + getServerInfo());
                    f.channel().eventLoop().schedule(() -> doConnect(), 1, TimeUnit.SECONDS);
                }
            }
        });
    }

    private String getServerInfo() {
        return String.format("RemoteHost=%s RemotePort=%d",
                remotePort,
                remotePort);
    }
}
