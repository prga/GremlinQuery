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
package io.netty.testsuite.transport; 

import com.sun.nio.sctp.SctpChannel; 
import com.sun.nio.sctp.SctpServerChannel; 
import com.sun.nio.sctp.SctpStandardSocketOptions; 
import io.netty.bootstrap.ClientBootstrap; 
import io.netty.bootstrap.ServerBootstrap; 
import io.netty.channel.*; 
import io.netty.channel.sctp.SctpChannelConfig; 
import io.netty.testsuite.util.DummyHandler; 
import io.netty.testsuite.util.SctpTestUtil; 
import io.netty.util.internal.ExecutorUtil; 
import org.easymock.EasyMock; 
import org.junit.AfterClass; 
import org.junit.Assume; 
import org.junit.BeforeClass; 
import org.junit.Test; 

import java.io.IOException; 
import java.net.InetSocketAddress; 
import java.net.SocketAddress; 
import java.util.Iterator; 
import java.util.concurrent.Executor; 
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors; 

import static org.junit.Assert.assertEquals; 
import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 


/**
 * An abstract test class to test server socket bootstraps
 */
  class  AbstractSocketServerBootstrapTest {
	

    

	

    static {
        boolean bufSizeModifiable = true;

        SctpChannel s = null;
        try {
            s = SctpChannel.open();
            bufSizeModifiable = s.supportedOptions().contains(SctpStandardSocketOptions.SO_RCVBUF);
        } catch (Exception e) {
            bufSizeModifiable = false;
            System.err.println(
                    "SCTP SO_RCVBUF does not work: " + e);
        } finally {
            BUFSIZE_MODIFIABLE = bufSizeModifiable;
            try {
                if (s != null) {
                    s.close();
                }
            } catch (IOException e) {
                // Ignore.
            }
        }
    }

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	


    

	

      class  ParentChannelHandler  extends SimpleChannelUpstreamHandler {
		

        

		
        

		

        

		

        

		

        


	}

	
    
    private static final InternalLogger logger =
        InternalLoggerFactory.getInstance(AbstractSocketServerBootstrapTest.class);

	

    static {
        boolean bufSizeModifiable = true;

        SctpChannel s = null;
        try {
            s = SctpChannel.open();
            bufSizeModifiable = s.supportedOptions().contains(SctpStandardSocketOptions.SO_RCVBUF);
        } catch (Throwable e) {
            bufSizeModifiable = false;
            logger.error("SCTP SO_RCVBUF does not work: " + e);
        } finally {
            BUFSIZE_MODIFIABLE = bufSizeModifiable;
            try {
                if (s != null) {
                    s.close();
                }
            } catch (IOException e) {
                // Ignore.
            }
        }
    }


}
