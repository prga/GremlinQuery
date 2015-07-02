/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.sctp; 

import io.netty.bootstrap.ServerBootstrap; 
import io.netty.channel.ChannelFuture; 
import io.netty.channel.ChannelInitializer; 
import io.netty.channel.ChannelOption; 
import io.netty.channel.socket.SctpChannel; 
import io.netty.channel.socket.SocketChannel; 
import io.netty.channel.socket.nio.NioEventLoopGroup; 
import io.netty.channel.socket.nio.NioSctpServerChannel; 
import io.netty.channel.socket.nio.NioServerSocketChannel; 
import io.netty.handler.logging.LogLevel; 
import io.netty.handler.logging.LoggingHandler; 

import java.net.InetSocketAddress; 

/**
 * Echoes back any received data from a SCTP client.
 */
  class  SctpEchoServer {
	

    

	

    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1391034059387/fstmerge_var1_4719532926274887958
public void run() throws Exception {
        // Configure the server.
        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(new NioEventLoopGroup(), new NioEventLoopGroup())
             .channel(NioSctpServerChannel.class)
             .option(ChannelOption.SO_BACKLOG, 100)
             .localAddress(new InetSocketAddress(port))
             .childOption(ChannelOption.SCTP_NODELAY, true)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SctpChannel>() {
                 @Override
                 public void initChannel(SctpChannel ch) throws Exception {
                     ch.pipeline().addLast(
                             new LoggingHandler(LogLevel.INFO),
                             new SctpEchoServerHandler());
                 }
             });

            // Start the server.
            ChannelFuture f = b.bind().sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            b.shutdown();
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1391034059387/fstmerge_var2_3409060881581965345


	

    


}