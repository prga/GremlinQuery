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
package io.netty.channel.socket.nio; 

import static io.netty.channel.Channels.*; 

import java.net.SocketAddress; 


import io.netty.channel.Channel; 
import io.netty.channel.ChannelEvent; 
import io.netty.channel.ChannelFuture; 
import io.netty.channel.ChannelPipeline; 
import io.netty.channel.ChannelState; 
import io.netty.channel.ChannelStateEvent; 
import io.netty.channel.MessageEvent; 
import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 

 

class  NioServerSocketPipelineSink  extends AbstractNioChannelSink {
	

    

	

    private final WorkerPool<NioWorker> workerPool;

	

    NioServerSocketPipelineSink(WorkerPool<NioWorker> workerPool) {
        this.workerPool = workerPool;
    }

	

    

	

    

	

    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772200316/fstmerge_var1_2533984982726865893
private void bind(
            NioServerSocketChannel channel, ChannelFuture future,
            SocketAddress localAddress) {
        boolean bound = false;
        try {
            channel.socket.socket().bind(localAddress, channel.getConfig().getBacklog());
            bound = true;

            future.setSuccess();
            fireChannelBound(channel, channel.getLocalAddress());

            channel.getWorker().registerWithWorker(channel, future);
            
        } catch (Throwable t) {
            future.setFailure(t);
            fireExceptionCaught(channel, t);
        } finally {
            if (!bound) {
                channel.getWorker().close(channel, future);
            }
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772200316/fstmerge_var2_3824895874527680184



}
