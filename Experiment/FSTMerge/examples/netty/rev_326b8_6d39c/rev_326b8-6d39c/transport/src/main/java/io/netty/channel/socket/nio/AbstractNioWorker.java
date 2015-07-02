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

import io.netty.channel.Channel; 
import io.netty.channel.ChannelException; 
import io.netty.channel.ChannelFuture; 
import io.netty.channel.ChannelPipeline; 
import io.netty.channel.MessageEvent; 
import io.netty.channel.socket.Worker; 
import io.netty.channel.socket.nio.SendBufferPool.SendBuffer; 
import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 
import io.netty.util.internal.DeadLockProofWorker; 
import io.netty.util.internal.QueueFactory; 

import java.io.IOException; 
import java.net.ConnectException; 
import java.net.SocketTimeoutException; 
import java.nio.channels.AsynchronousCloseException; 
import java.nio.channels.CancelledKeyException; 
import java.nio.channels.ClosedChannelException; 
import java.nio.channels.ClosedSelectorException; 
import java.nio.channels.NotYetConnectedException; 
import java.nio.channels.SelectionKey; 
import java.nio.channels.Selector; 
import java.nio.channels.SocketChannel; 
import java.nio.channels.WritableByteChannel; 
import java.util.Iterator; 
import java.util.Queue; 
import java.util.Set; 
import java.util.concurrent.Executor; 
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.atomic.AtomicBoolean; 
import java.util.concurrent.locks.ReadWriteLock; 
import java.util.concurrent.locks.ReentrantReadWriteLock; 

  class  AbstractNioWorker   {
	
    /**
     * Internal Netty logger.
     */
    

	

    

	

    

	 // XXX Hard-coded value, but won't need customization.

    
    /**
     * Executor used to execute {@link Runnable}s such as registration task.
     */
    

	

    /**
     * Boolean to indicate if this worker has been started.
     */
    

	

    /**
     * If this worker has been started thread will be a reference to the thread
     * used when starting. i.e. the current thread when the run method is executed.
     */
    

	

    /**
     * The NIO {@link Selector}.
     */
    

	

    /**
     * Boolean that controls determines if a blocked Selector.select should
     * break out of its selection process. In our case we use a timeone for
     * the select method and the select method will block for that time unless
     * waken up.
     */
    

	

    /**
     * Lock for this workers Selector.
     */
    

	

    /**
     * Monitor object used to synchronize selector open/close.
     */
    

	

    /**
     * Queue of channel registration tasks.
     */
    

	

    /**
     * Queue of WriteTasks
     */
    

	

    

	

    
    

	 // should use AtomicInteger but we just need approximation

    

	

    

	

    

	

    

	

    

	
    
    /**
     * Start the {@link AbstractNioWorker} and return the {@link Selector} that will be used for the {@link AbstractNioChannel}'s when they get registered
     * 
     * @return selector
     */
    

	

    

	
    
    

	
    
    /**
     * Execute the {@link Runnable} in a IO-Thread
     * 
     * @param task the {@link Runnable} to execute
     * @param alwaysAsync <code>true</code> if the {@link Runnable} should be executed in an async
     *                    fashion even if the current Thread == IO Thread
     */
    

	
    
    

	

    

	
    
    

	
    
    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772197449/fstmerge_var1_3620095690855170901
private void processSelectedKeys(Set<SelectionKey> selectedKeys) throws IOException {
        for (Iterator<SelectionKey> i = selectedKeys.iterator(); i.hasNext();) {
            SelectionKey k = i.next();
            boolean removeKey = true;
            try {

                int readyOps = k.readyOps();
                if ((readyOps & SelectionKey.OP_READ) != 0 || readyOps == 0) {
                    if (!read(k)) {
                        // Connection already closed - no need to handle write / accept / connect.
                        continue;
                    }
                }
                if ((readyOps & SelectionKey.OP_WRITE) != 0) {
                    writeFromSelectorLoop(k);
                }
                
                if ((readyOps & SelectionKey.OP_ACCEPT) != 0) {
                    removeKey = accept(k);
                }
                if ((readyOps & SelectionKey.OP_CONNECT) != 0) {
                    connect(k);
                }

            } catch (CancelledKeyException e) {
                close(k);
            } finally {
                if (removeKey) {
                    i.remove();
                }
            }
            


            if (cleanUpCancelledKeys()) {
                break; // break the loop to avoid ConcurrentModificationException
            }
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772197449/fstmerge_var2_4816883603287370831


	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772197501/fstmerge_var1_5749785591172221774
protected boolean accept(SelectionKey key) {
        NioServerSocketChannel channel = (NioServerSocketChannel) key.attachment();
        try {
            boolean handled = false;
            
            // accept all sockets that are waiting atm
            for (;;) {
                SocketChannel acceptedSocket = channel.socket.accept();
                if (acceptedSocket == null) {
                    break;
                }
                ChannelPipeline pipeline =
                        channel.getConfig().getPipelineFactory().getPipeline();
                NioWorker worker = channel.workers.nextWorker();
                
                worker.registerWithWorker(NioAcceptedSocketChannel.create(channel.getFactory(), pipeline, channel,
                        channel.getPipeline().getSink(), acceptedSocket, worker), null);
                handled = true;
            }
            return handled;
        } catch (SocketTimeoutException e) {
            // Thrown every second to get ClosedChannelException
            // raised.
        } catch (CancelledKeyException e) {
            // Raised by accept() when the server socket was closed.
        } catch (ClosedSelectorException e) {
            // Raised by accept() when the server socket was closed.
        } catch (ClosedChannelException e) {
            // Closed as requested.
        } catch (Throwable e) {
            if (logger.isWarnEnabled()) {
                logger.warn(
                        "Failed to accept a connection.", e);
            }
        }
        return true;
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772197501/fstmerge_var2_4875289576366363061


	
    
    
    

	

    

	
    
    

	
    

    
    

	

    

	

    

	
    
    

	

    
    

	       

    

	

    /**
     * Return <code>true</code> if the current executing thread is the same as the one that runs the {@link #run()} method
     * 
     */
    

	
    
    

	

    

	
    

    

	
    
    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772198217/fstmerge_var1_3394629293011147277
public void close(AbstractNioChannel channel, ChannelFuture future) {
        boolean connected = channel.isConnected();
        boolean bound = channel.isBound();
        boolean iothread = isIoThread();
        
        try {
            channel.getJdkChannel().close();
            cancelledKeys ++;

            if (channel.setClosed()) {
                future.setSuccess();
                if (connected) {
                    if (iothread) {
                        fireChannelDisconnected(channel);
                    } else {
                        fireChannelDisconnectedLater(channel);
                    }
                }
                if (bound) {
                    if (iothread) {
                        fireChannelUnbound(channel);
                    } else {
                        fireChannelUnboundLater(channel);
                    }
                }

                cleanUpWriteBuffer(channel);
                if (iothread) {
                    fireChannelClosed(channel);
                } else {
                    fireChannelClosedLater(channel);
                }
            } else {
                future.setSuccess();
            }
        } catch (Throwable t) {
            future.setFailure(t);
            if (iothread) {
                fireExceptionCaught(channel, t);
            } else {
                logger.debug(thread + "==" + channel.getWorker().thread);
                fireExceptionCaughtLater(channel, t);
            }
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772198217/fstmerge_var2_7832134869865063304


	
    
    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772198268/fstmerge_var1_3850749529251368201
protected void cleanUpWriteBuffer(AbstractNioChannel channel) {
        Exception cause = null;
        boolean fireExceptionCaught = false;

        // Clean up the stale messages in the write buffer.
        synchronized (channel.writeLock) {
            MessageEvent evt = channel.currentWriteEvent;
            if (evt != null) {
                // Create the exception only once to avoid the excessive overhead
                // caused by fillStackTrace.
                if (channel.isOpen()) {
                    cause = new NotYetConnectedException();
                } else {
                    cause = new ClosedChannelException();
                }

                ChannelFuture future = evt.getFuture();
                channel.currentWriteBuffer.release();
                channel.currentWriteBuffer = null;
                channel.currentWriteEvent = null;
                evt = null;
                future.setFailure(cause);
                fireExceptionCaught = true;
            }

            Queue<MessageEvent> writeBuffer = channel.writeBufferQueue;
            for (;;) {
                evt = writeBuffer.poll();
                if (evt == null) {
                    break;
                }
                // Create the exception only once to avoid the excessive overhead
                // caused by fillStackTrace.
                if (cause == null) {
                    if (channel.isOpen()) {
                        cause = new NotYetConnectedException();
                    } else {
                        cause = new ClosedChannelException();
                    }
                    fireExceptionCaught = true;
                }
                evt.getFuture().setFailure(cause);

               
            }
        }

        if (fireExceptionCaught) {
            if (isIoThread()) {
                fireExceptionCaught(channel, cause);
            } else {
                fireExceptionCaughtLater(channel, cause);
            }
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772198268/fstmerge_var2_6122790965806767962


	

    

	
    
    /**
     * Read is called when a Selector has been notified that the underlying channel
     * was something to be read. The channel would previously have registered its interest
     * in read operations.
     *
     * @param k The selection key which contains the Selector registration information.
     */
    

	

    


}
