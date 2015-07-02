/*
 * Copyright 2011 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.qotm; 

import io.netty.buffer.ChannelBuffers; 
import io.netty.channel.Channel; 
import io.netty.channel.ChannelBootstrap; 
import io.netty.channel.ChannelInitializer; 
import io.netty.channel.ChannelOption; 
import io.netty.channel.socket.DatagramChannel; 
import io.netty.channel.socket.DatagramPacket; 
import io.netty.channel.socket.nio.NioDatagramChannel; 
import io.netty.channel.socket.nio.NioEventLoop; 
import io.netty.util.CharsetUtil; 

import java.net.InetSocketAddress; 
import java.util.concurrent.Executors; 

import io.netty.bootstrap.ConnectionlessBootstrap; 
import io.netty.channel.ChannelPipeline; 
import io.netty.channel.ChannelPipelineFactory; 
import io.netty.channel.Channels; 
import io.netty.channel.FixedReceiveBufferSizePredictorFactory; 
import io.netty.channel.socket.DatagramChannelFactory; 
import io.netty.channel.socket.nio.NioDatagramChannelFactory; 
import io.netty.handler.codec.string.StringDecoder; 
import io.netty.handler.codec.string.StringEncoder; 
import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 

/**
 * A UDP broadcast client that asks for a quote of the moment (QOTM) to
 * {@link QuoteOfTheMomentServer}.
 *
 * Inspired by <a href="http://java.sun.com/docs/books/tutorial/networking/datagrams/clientServer.html">the official Java tutorial</a>.
 */
public  class  QuoteOfTheMomentClient {
	

    private final int port;

	

    public QuoteOfTheMomentClient(int port) {
        this.port = port;
    }


	

    public void run() throws Exception {
        ChannelBootstrap b = new ChannelBootstrap();
        try {
            b.eventLoop(new NioEventLoop())
             .channel(new NioDatagramChannel())
             .localAddress(new InetSocketAddress(0))
             .option(ChannelOption.SO_BROADCAST, true)
             .initializer(new ChannelInitializer<DatagramChannel>() {
                @Override
                public void initChannel(DatagramChannel ch) throws Exception {
                    ch.pipeline().addLast(new QuoteOfTheMomentClientHandler());
                }
             });

            Channel ch = b.bind().sync().channel();

            // Broadcast the QOTM request to port 8080.
            ch.write(new DatagramPacket(
                    ChannelBuffers.copiedBuffer("QOTM?", CharsetUtil.UTF_8),
                    new InetSocketAddress("255.255.255.255", port)));

            // QuoteOfTheMomentClientHandler will close the DatagramChannel when a
            // response is received.  If the channel is not closed within 5 seconds,
            // print an error message and quit.
            if (!ch.closeFuture().await(5000)) {
                System.err.println("QOTM request timed out.");
            }
<<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772190355/fstmerge_var1_3658001080320775046
        });

        // Enable broadcast
        b.setOption("broadcast", "true");

        // Allow packets as large as up to 1024 bytes (default is 768).
        // You could increase or decrease this value to avoid truncated packets
        // or to improve memory footprint respectively.
        //
        // Please also note that a large UDP packet might be truncated or
        // dropped by your router no matter how you configured this option.
        // In UDP, a packet is truncated or dropped if it is larger than a
        // certain size, depending on router configuration.  IPv4 routers
        // truncate and IPv6 routers drop a large packet.  That's why it is
        // safe to send small packets in UDP.
        b.setOption(
                "receiveBufferSizePredictorFactory",
                new FixedReceiveBufferSizePredictorFactory(1024));

        DatagramChannel c = (DatagramChannel) b.bind(new InetSocketAddress(0));

        // Broadcast the QOTM request to port 8080.
        c.write("QOTM?", new InetSocketAddress("255.255.255.255", port));

        // QuoteOfTheMomentClientHandler will close the DatagramChannel when a
        // response is received.  If the channel is not closed within 5 seconds,
        // print an error message and quit.
        if (!c.getCloseFuture().awaitUninterruptibly(5000)) {
            logger.error("QOTM request timed out.");
            c.close().awaitUninterruptibly();
=======
        } finally {
            b.shutdown();
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772190355/fstmerge_var2_5996789687333433052
        }
    }


	

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new QuoteOfTheMomentClient(port).run();
    }


	
    
    private static final InternalLogger logger =
        InternalLoggerFactory.getInstance(QuoteOfTheMomentClient.class);


}