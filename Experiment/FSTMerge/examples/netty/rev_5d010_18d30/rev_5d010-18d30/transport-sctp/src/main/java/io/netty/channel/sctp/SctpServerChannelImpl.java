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

import static io.netty.channel.Channels.*; 

import java.io.IOException; 
import java.net.InetAddress; 
import java.net.InetSocketAddress; 
import java.net.SocketAddress; 
import java.util.Collections; 
import java.util.HashSet; 
import java.util.Iterator; 
import java.util.Set; 
import java.util.concurrent.locks.Lock; 
import java.util.concurrent.locks.ReentrantLock; 

import io.netty.channel.AbstractServerChannel; 
import io.netty.channel.ChannelException; 
import io.netty.channel.ChannelFactory; 
import io.netty.channel.ChannelFuture; 
import io.netty.channel.ChannelPipeline; 
import io.netty.channel.ChannelSink; 
import io.netty.channel.socket.nio.NioChannel; 
import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 

/**
 */
 

/**
 */
class  SctpServerChannelImpl  extends AbstractServerChannel 
                              {
	

    

	

    

	
    

	
    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    @Override
    public Set<InetSocketAddress> getAllRemoteAddresses() {
        return null; // not available for server channel
    }


}
