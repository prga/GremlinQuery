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
package io.netty.testsuite.transport.sctp; 

import static org.junit.Assert.*; 
import io.netty.bootstrap.ClientBootstrap; 
import io.netty.bootstrap.ServerBootstrap; 
import io.netty.buffer.ChannelBuffer; 
import io.netty.buffer.ChannelBuffers; 
import io.netty.channel.Channel; 
import io.netty.channel.ChannelFactory; 
import io.netty.channel.ChannelFuture; 
import io.netty.channel.ChannelHandlerContext; 
import io.netty.channel.ChannelStateEvent; 
import io.netty.channel.ExceptionEvent; 
import io.netty.channel.MessageEvent; 
import io.netty.channel.sctp.SctpChannel; 
import io.netty.channel.sctp.SctpClientSocketChannelFactory; 
import io.netty.channel.sctp.SctpNotificationEvent; 
import io.netty.channel.sctp.SctpServerChannel; 
import io.netty.channel.sctp.SctpServerSocketChannelFactory; 
import io.netty.channel.sctp.codec.SctpFrameDecoder; 
import io.netty.channel.sctp.codec.SctpFrameEncoder; 
import io.netty.channel.sctp.handler.SimpleSctpChannelHandler; 
import io.netty.testsuite.util.SctpTestUtil; 
import io.netty.util.internal.ExecutorUtil; 

import java.io.IOException; 
import java.net.InetAddress; 
import java.net.InetSocketAddress; 
import java.util.Random; 
import java.util.concurrent.Executor; 
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors; 
import java.util.concurrent.atomic.AtomicReference; 

import org.junit.AfterClass; 
import org.junit.Assume; 
import org.junit.BeforeClass; 
import org.junit.Test; 
import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 

  class  SctpMultiHomingEchoTest {
	
    
    

	
    

	//could not test ultra jumbo frames

    

	

    static {
        random.nextBytes(data);
    }

	

    

	

    

	

    

	

    

	

    

	

      class  EchoHandler  extends SimpleSctpChannelHandler {
		
        

		
        

		
        

		

        

		

        

		

        

		

        

		

        <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772207798/fstmerge_var1_3504461074745883356
@Override
        public void sctpNotificationReceived(ChannelHandlerContext ctx, SctpNotificationEvent event) {
            logger.info("SCTP notification event received :" + event);
        }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772207798/fstmerge_var2_122000116901929133



	}

	
    
    private static final InternalLogger logger =
        InternalLoggerFactory.getInstance(SctpMultiHomingEchoTest.class);

	

    static {
        random.nextBytes(data);
    }


}
