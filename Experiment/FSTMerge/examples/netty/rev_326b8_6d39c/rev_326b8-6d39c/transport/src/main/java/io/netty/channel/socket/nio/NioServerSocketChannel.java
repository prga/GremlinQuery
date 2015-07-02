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

import io.netty.channel.ChannelBufferHolder; 
import io.netty.channel.ChannelBufferHolders; 
import io.netty.channel.ChannelException; 
import io.netty.channel.socket.DefaultServerSocketChannelConfig; 
import io.netty.channel.socket.ServerSocketChannelConfig; 

import java.io.IOException; 
import java.net.InetSocketAddress; 
import java.net.SocketAddress; 
import java.nio.channels.SelectionKey; 
import java.nio.channels.ServerSocketChannel; 
import java.util.Queue; 

import static io.netty.channel.Channels.*; 
import java.util.concurrent.locks.Lock; 
import java.util.concurrent.locks.ReentrantLock; 

import io.netty.channel.AbstractServerChannel; 
import io.netty.channel.ChannelFactory; 
import io.netty.channel.ChannelPipeline; 
import io.netty.channel.ChannelSink; 
import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 

public  class  NioServerSocketChannel  extends AbstractServerChannel 
                             implements io.netty.channel.socket.ServerSocketChannel {
	

    private static ServerSocketChannel newSocket() {
        try {
            return ServerSocketChannel.open();
        } catch (IOException e) {
            throw new ChannelException(
                    "Failed to open a server socket.", e);
        }
    }

	
    
    
    private final ServerSocketChannelConfig config;

	

    public NioServerSocketChannel() {
        super(null, null, newSocket(), SelectionKey.OP_ACCEPT);
        config = new DefaultServerSocketChannelConfig(javaChannel().socket());
    }

	

    @Override
    public ServerSocketChannelConfig config() {
        return config;
    }

	

    @Override
    public boolean isActive() {
        return javaChannel().socket().isBound();
    }

	

    @Override
    public InetSocketAddress remoteAddress() {
        return null;
    }

	

    @Override
    protected java.nio.channels.ServerSocketChannel javaChannel() {
        return (ServerSocketChannel) super.javaChannel();
    }

	

    @Override
    protected SocketAddress localAddress0() {
        return javaChannel().socket().getLocalSocketAddress();
    }

	

    @Override
    protected void doBind(SocketAddress localAddress) throws Exception {
        javaChannel().socket().bind(localAddress);
        SelectionKey selectionKey = selectionKey();
        selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_ACCEPT);
    }

	

    @Override
    protected void doClose() throws Exception {
        javaChannel().close();
    }

	

    @Override
    protected int doReadMessages(Queue<Object> buf) throws Exception {
        java.nio.channels.SocketChannel ch = javaChannel().accept();
        if (ch == null) {
            return 0;
        }
        buf.add(new NioSocketChannel(this, null, ch));
        return 1;
    }

	

    // Unnecessary stuff
    @Override
    protected boolean doConnect(
            SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }

	

    @Override
    protected void doFinishConnect() throws Exception {
        throw new UnsupportedOperationException();
    }

	

    @Override
    protected ChannelBufferHolder<Object> firstOut() {
        return ChannelBufferHolders.discardBuffer();
    }

	

    @Override
    protected SocketAddress remoteAddress0() {
        return null;
    }

	

    @Override
    protected void doDisconnect() throws Exception {
        throw new UnsupportedOperationException();
    }

	

    @Override
    protected int doWriteMessages(Queue<Object> buf, boolean lastSpin) throws Exception {
        throw new UnsupportedOperationException();
    }

	

    

	

    

	
    

	
    

	

    

	

    

	

    

	

    

	

    

	

    

	
    final WorkerPool<NioWorker> workers;

	

    static NioServerSocketChannel create(ChannelFactory factory,
            ChannelPipeline pipeline, ChannelSink sink, NioWorker worker, WorkerPool<NioWorker> workers) {
        NioServerSocketChannel instance =
                new NioServerSocketChannel(factory, pipeline, sink, worker, workers);
        fireChannelOpen(instance);
        return instance;
    }

	

    private NioServerSocketChannel(
            ChannelFactory factory,
            ChannelPipeline pipeline,
            ChannelSink sink, NioWorker worker, WorkerPool<NioWorker> workers) {

        super(factory, pipeline, sink);
        this.worker = worker;
        this.workers = workers;
        try {
            socket = ServerSocketChannel.open();
        } catch (IOException e) {
            throw new ChannelException(
                    "Failed to open a server socket.", e);
        }

        try {
            socket.configureBlocking(false);
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e2) {
                if (logger.isWarnEnabled()) {
                    logger.warn(
                            "Failed to close a partially initialized socket.", e2);
                }

            }

            throw new ChannelException("Failed to enter non-blocking mode.", e);
        }

        config = new DefaultServerSocketChannelConfig(socket.socket());
    }


}
