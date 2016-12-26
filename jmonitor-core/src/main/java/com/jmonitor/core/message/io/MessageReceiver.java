package com.jmonitor.core.message.io;

import com.jmonitor.core.message.codec.MessageDecoder;
import com.jmonitor.core.message.consume.MessageConsumer;
import com.jmonitor.core.message.type.Message;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * description
 * @author jmonitor
 * @date 2016/10/9
 */
public final class MessageReceiver implements Runnable{
    private MessageDecoder decoder = new MessageDecoder();
	private MessageConsumer consumer = new MessageConsumer();
	private ChannelFuture future;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private int port; // default port number
	private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.message.io.MessageReceiver");

    public MessageReceiver(Properties p){
		this.port = Integer.parseInt(p.getProperty("port"));
    }

	@Override
	public void run() {
		startServer(this.port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				consumer.doCheckpoint();
			}
		});
	}

	public synchronized void destroy() {
		try {
			this.future.channel().closeFuture();
			this.bossGroup.shutdownGracefully();
			this.workerGroup.shutdownGracefully();
			logger.info("shutdown socket");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	protected boolean getOSMatches(String osNamePrefix) {
		String os = System.getProperty("os.name");

		if (os == null) {
			return false;
		}
		return os.startsWith(osNamePrefix);
	}

    /**
     * @description：start the netty server with a certain port
     * @param：port
     * @return: void
     */
	public synchronized void startServer(int port){
		boolean linux = getOSMatches("Linux") || getOSMatches("LINUX");
		int threads = 24;
		ServerBootstrap bootstrap = new ServerBootstrap();

		this.bossGroup = linux ? new EpollEventLoopGroup(threads) : new NioEventLoopGroup(threads);
		this.workerGroup = linux ? new EpollEventLoopGroup(threads) : new NioEventLoopGroup(threads);
		bootstrap.group(this.bossGroup, this.workerGroup);
		bootstrap.channel(linux ? EpollServerSocketChannel.class : NioServerSocketChannel.class);

		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("decode", new SocketMessageDecoder());
			}
		});

		bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
		bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

		try {
			this.future = bootstrap.bind(port).sync();
			logger.info("start netty server OK !");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public class SocketMessageDecoder extends ByteToMessageDecoder {

		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
			if (buffer.readableBytes() < 4) {
				return;
			}
			buffer.markReaderIndex();
			int length = buffer.readInt();
			buffer.resetReaderIndex();
			if (buffer.readableBytes() < length + 4) {
				return;
			}
			try {
				if (length > 0) {
					ByteBuf readBytes = buffer.readBytes(length + 4);
					readBytes.markReaderIndex();
					readBytes.readInt();
					Message message = decoder.decode(readBytes, length);
					readBytes.resetReaderIndex();
                    consumer.consume(message);
				} else {
					buffer.readBytes(length);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

    public Object getCurrentReportByHour(String type){
        return consumer.getCurrentReportByHour(type);
    }

}