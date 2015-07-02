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
package io.netty.example.sctp; 

import io.netty.bootstrap.ClientBootstrap; 
import io.netty.channel.ChannelFuture; 
import io.netty.channel.ChannelPipeline; 
import io.netty.channel.ChannelPipelineFactory; 
import io.netty.channel.Channels; 
import io.netty.channel.sctp.SctpClientSocketChannelFactory; 
import io.netty.handler.execution.ExecutionHandler; 
import io.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor; 

import java.net.InetSocketAddress; 
import java.util.concurrent.Executors; 
import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 

/**
 * Simple SCTP Echo Client
 */
  class  SctpClient {
	

    

	
    

	
    
    

	

    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772190939/fstmerge_var1_2277982465966302922
public static void main(String[] args) throws Exception {
        // Print usage if no argument is specified.
        if (args.length != 2) {
            logger.error(
                    "Usage: " + SctpClient.class.getSimpleName() +
                    " <host> <port>");
            return;
        }

        // Parse options.
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);

        new SctpClient(host, port).run();
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772190939/fstmerge_var2_7836910666723665557


	
    
    private static final InternalLogger logger =
        InternalLoggerFactory.getInstance(SctpClient.class);


}
