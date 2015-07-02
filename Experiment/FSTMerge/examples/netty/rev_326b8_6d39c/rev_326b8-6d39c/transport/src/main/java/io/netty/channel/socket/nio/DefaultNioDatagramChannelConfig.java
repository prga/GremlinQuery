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

import io.netty.channel.ChannelException; 
import io.netty.channel.socket.DefaultDatagramChannelConfig; 
import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 
import io.netty.util.internal.ConversionUtil; 
import io.netty.util.internal.DetectionUtil; 

import java.io.IOException; 
import java.net.NetworkInterface; 
import java.net.StandardSocketOptions; 
import java.nio.channels.DatagramChannel; 
import java.util.Map; 
import java.net.InetAddress; 
import java.net.SocketException; 
import java.util.Enumeration; 

/**
 * The default {@link NioSocketChannelConfig} implementation.
 */
 

/**
 * The default {@link NioSocketChannelConfig} implementation.
 */
class  DefaultNioDatagramChannelConfig  extends DefaultDatagramChannelConfig 
         {
	

    

	

    

	
    

	
    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	
    
    

	

    

	

    @Override
    public int getTimeToLive() {
        if (DetectionUtil.javaVersion() < 7) {
            throw new UnsupportedOperationException();
        } else {
            try {
                return (int) channel.getOption(StandardSocketOptions.IP_MULTICAST_TTL);
            } catch (IOException e) {
                throw new ChannelException(e);
            }
        }
    }

	

    @Override
    public void setTimeToLive(int ttl) {
        if (DetectionUtil.javaVersion() < 7) {
            throw new UnsupportedOperationException();
        } else {
            try {
                channel.setOption(StandardSocketOptions.IP_MULTICAST_TTL, ttl);
            } catch (IOException e) {
                throw new ChannelException(e);
            }
        }
       
    }

	
    
    @Override
    public InetAddress getInterface() {
        NetworkInterface inf = getNetworkInterface();
        if (inf == null) {
            return null;
        } else {
            Enumeration<InetAddress> addresses = inf.getInetAddresses();
            if (addresses.hasMoreElements()) {
                return addresses.nextElement();
            }
            return null;
        }
    }

	

    @Override
    public void setInterface(InetAddress interfaceAddress) {
        try {
            setNetworkInterface(NetworkInterface.getByInetAddress(interfaceAddress));
        } catch (SocketException e) {
            throw new ChannelException(e);
        }
    }

	

    @Override
    public boolean isLoopbackModeDisabled() {
        if (DetectionUtil.javaVersion() < 7) {
            throw new UnsupportedOperationException();
        } else {
            try {
                return (Boolean) channel.getOption(StandardSocketOptions.IP_MULTICAST_LOOP);
            } catch (IOException e) {
                throw new ChannelException(e);
            }
        }
    }

	

    @Override
    public void setLoopbackModeDisabled(boolean loopbackModeDisabled) {
        if (DetectionUtil.javaVersion() < 7) {
            throw new UnsupportedOperationException();
        } else {
            try {
                channel.setOption(StandardSocketOptions.IP_MULTICAST_LOOP, loopbackModeDisabled);
            } catch (IOException e) {
                throw new ChannelException(e);
            }
        }
    }


}
