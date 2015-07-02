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
package io.netty.channel.sctp; 

import static io.netty.channel.Channels.fireChannelBound; 
import static io.netty.channel.Channels.fireChannelClosed; 
import static io.netty.channel.Channels.fireChannelConnected; 
import static io.netty.channel.Channels.fireChannelUnbound; 
import static io.netty.channel.Channels.fireExceptionCaught; 
import static io.netty.channel.Channels.fireMessageReceived; 
import static io.netty.channel.Channels.fireWriteComplete; 
import static io.netty.channel.Channels.succeededFuture; 
import io.netty.buffer.ChannelBuffer; 
import io.netty.buffer.ChannelBufferFactory; 
import io.netty.channel.Channel; 
import io.netty.channel.ChannelException; 
import io.netty.channel.ChannelFuture; 
import io.netty.channel.ChannelPipeline; 
import io.netty.channel.MessageEvent; 
import io.netty.channel.ReceiveBufferSizePredictor; 
import io.netty.channel.sctp.SctpSendBufferPool.SctpSendBuffer; 
import io.netty.channel.socket.nio.AbstractNioChannel; 
import io.netty.channel.socket.nio.NioWorker; 

import java.io.IOException; 
import java.net.ConnectException; 
import java.net.SocketAddress; 
import java.net.SocketTimeoutException; 
import java.nio.ByteBuffer; 
import java.nio.channels.AsynchronousCloseException; 
import java.nio.channels.CancelledKeyException; 
import java.nio.channels.ClosedChannelException; 
import java.nio.channels.ClosedSelectorException; 
import java.nio.channels.SelectionKey; 
import java.nio.channels.Selector; 
import java.util.Queue; 
import java.util.Set; 
import java.util.concurrent.Executor; 

import com.sun.nio.sctp.MessageInfo; 
import com.sun.nio.sctp.SctpChannel; 

/**
 */
  class  SctpWorker  extends NioWorker {
	

    

	

    

	
    
    

	

    

	
    
    

	


    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772205390/fstmerge_var1_2023500771037394437
@Override
    protected void connect(SelectionKey k) {
        final SctpClientChannel ch = (SctpClientChannel) k.attachment();
        try {
            if (ch.getJdkChannel().finishConnect()) {
                registerTask(ch, ch.connectFuture);
            }
        } catch (Throwable t) {
            ch.connectFuture.setFailure(t);
            fireExceptionCaught(ch, t);
            k.cancel(); // Some JDK implementations run into an infinite loop without this.
            ch.getWorker().close(ch, succeededFuture(ch));
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772205390/fstmerge_var2_5744825664091249000


	
    
    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772205500/fstmerge_var1_2959678307726217480
@Override
    protected void registerTask(AbstractNioChannel ch, ChannelFuture future) {
        boolean server = !(ch instanceof SctpClientChannel);
        SctpChannelImpl channel = (SctpChannelImpl) ch;
        
        SocketAddress localAddress = channel.getLocalAddress();
        SocketAddress remoteAddress = channel.getRemoteAddress();
        if (localAddress == null || remoteAddress == null) {
            if (future != null) {
                future.setFailure(new ClosedChannelException());
            }
            close(channel, succeededFuture(channel));
            return;
        }

        try {
            if (server) {
                channel.getJdkChannel().configureBlocking(false);
            }
            
            boolean registered = channel.getJdkChannel().isRegistered();
            if (!registered) {
                synchronized (channel.getInterestedOpsLock()) {
                    channel.getJdkChannel().register(
                            selector, channel.getRawInterestOps(), channel);
                }
                
            } else {
                setInterestOps(channel, succeededFuture(channel), channel.getRawInterestOps());
            }
            if (future != null) {
                ((SctpChannelImpl) channel).setConnected();
                future.setSuccess();
            }

            if (!server) {
                if (!((SctpClientChannel) channel).boundManually) {
                    fireChannelBound(channel, localAddress);
                }
                fireChannelConnected(channel, remoteAddress);
            }
        } catch (IOException e) {
            if (future != null) {
                future.setFailure(e);
            }
            close(channel, succeededFuture(channel));
            if (!(e instanceof ClosedChannelException)) {
                throw new ChannelException(
                        "Failed to register a socket to the selector.", e);
            }
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772205500/fstmerge_var2_6791347927722709460


	

    

	

    

	
    
    


}
