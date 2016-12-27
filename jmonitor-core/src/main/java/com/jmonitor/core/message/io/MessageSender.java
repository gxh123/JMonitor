package com.jmonitor.core.message.io;

import com.jmonitor.core.configuration.ClientConfig;
import com.jmonitor.core.message.MessageQueue;
import com.jmonitor.core.message.codec.MessageEncoder;
import com.jmonitor.core.message.type.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class MessageSender implements Runnable{
	public static final int SIZE = 5000;

	private MessageEncoder codec = new MessageEncoder();
	private MessageQueue queue = new MessageQueue(SIZE);
	private InetSocketAddress serverAddress;
	private boolean active;
	private Bootstrap bootstrap;
	private ChannelFuture channelFuture;
	private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.message.io.MessageSender");

	public MessageSender(ClientConfig config){
		InetSocketAddress address = new InetSocketAddress(config.getServerIp(), config.getServerPort());
		this.serverAddress = address;

		EventLoopGroup group = new NioEventLoopGroup(1, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			}
		});
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				// p.addLast(new LoggingHandler(LogLevel.INFO));
			}
		});
		this.bootstrap = bootstrap;
		this.channelFuture = createChannel(serverAddress);

		if (this.channelFuture == null){
			logger.error("error when create Channel");
			throw new RuntimeException("error when createChannel");
		}

		this.active = true;
		new Thread(this).start();
	}

	private ChannelFuture createChannel(InetSocketAddress address) {
		ChannelFuture future = null;
		try {
			future = bootstrap.connect(address);
			future.awaitUninterruptibly(100, TimeUnit.MILLISECONDS); // 100 ms

			if (!future.isSuccess()) {
				logger.error("Error when try connecting to " + address);
				closeChannel(future);
			} else {
				logger.info("Connected to server at " + address);
				return future;
			}
		} catch (Throwable e) {
			logger.error("Error when connect server " + address.getAddress());
			if (future != null) {
				closeChannel(future);
			}
		}
		return null;
	}

	private void closeChannel(ChannelFuture channel) {
		try {
			if (channel != null) {
				channel.channel().close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void run() {
		while (active) {
			if (this.channelFuture != null) {
				try {
					Message message = queue.poll();
					if (message != null) {
						sendInternal(message);
					}
				} catch (Throwable t) {
					logger.error("Error when sending message over TCP socket!");
				}
			} else {
				try {
					Thread.sleep(5);
				} catch (Exception e) {
					logger.error(e.getMessage());
					active = false;
				}
			}
		}
	}

	public void send(Message message) {
		queue.offer(message);
	}

	private void sendInternal(Message message) {
		ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(10 * 1024); // 10K
		codec.encode(message, buf);
		Channel channel = this.channelFuture.channel();
		channel.writeAndFlush(buf);
	}

}
