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
package io.netty.handler.execution; 
import java.util.concurrent.ConcurrentMap; 
import java.util.concurrent.Executor; 
import java.util.concurrent.Executors; 
import java.util.concurrent.RejectedExecutionException; 
import java.util.concurrent.RejectedExecutionHandler; 
import java.util.concurrent.ThreadFactory; 
import java.util.concurrent.ThreadPoolExecutor; 
import java.util.concurrent.TimeUnit; 
import java.util.concurrent.atomic.AtomicLong; 

import io.netty.buffer.ChannelBuffer; 
import io.netty.channel.Channel; 
import io.netty.channel.ChannelEvent; 
import io.netty.channel.ChannelHandlerContext; 
import io.netty.channel.ChannelState; 
import io.netty.channel.ChannelStateEvent; 
import io.netty.channel.MessageEvent; 
import io.netty.channel.WriteCompletionEvent; 
import io.netty.util.internal.ConcurrentIdentityHashMap; 
import io.netty.util.internal.QueueFactory; 
import io.netty.util.internal.SharedResourceMisuseDetector; 

import java.io.IOException; 
import java.util.HashSet; 
import java.util.List; 
import java.util.Set; 
import io.netty.channel.ChannelFuture; 
import io.netty.channel.Channels; 

/**
 * A {@link ThreadPoolExecutor} which blocks the task submission when there's
 * too many tasks in the queue.  Both per-{@link Channel} and per-{@link Executor}
 * limitation can be applied.
 * <p>
 * When a task (i.e. {@link Runnable}) is submitted,
 * {@link MemoryAwareThreadPoolExecutor} calls {@link ObjectSizeEstimator#estimateSize(Object)}
 * to get the estimated size of the task in bytes to calculate the amount of
 * memory occupied by the unprocessed tasks.
 * <p>
 * If the total size of the unprocessed tasks exceeds either per-{@link Channel}
 * or per-{@link Executor} threshold, any further {@link #execute(Runnable)}
 * call will block until the tasks in the queue are processed so that the total
 * size goes under the threshold.
 *
 * <h3>Using an alternative task size estimation strategy</h3>
 *
 * Although the default implementation does its best to guess the size of an
 * object of unknown type, it is always good idea to to use an alternative
 * {@link ObjectSizeEstimator} implementation instead of the
 * {@link DefaultObjectSizeEstimator} to avoid incorrect task size calculation,
 * especially when:
 * <ul>
 *   <li>you are using {@link MemoryAwareThreadPoolExecutor} independently from
 *       {@link ExecutionHandler},</li>
 *   <li>you are submitting a task whose type is not {@link ChannelEventRunnable}, or</li>
 *   <li>the message type of the {@link MessageEvent} in the {@link ChannelEventRunnable}
 *       is not {@link ChannelBuffer}.</li>
 * </ul>
 * Here is an example that demonstrates how to implement an {@link ObjectSizeEstimator}
 * which understands a user-defined object:
 * <pre>
 * public class MyRunnable implements {@link Runnable} {
 *
 *     <b>private final byte[] data;</b>
 *
 *     public MyRunnable(byte[] data) {
 *         this.data = data;
 *     }
 *
 *     public void run() {
 *         // Process 'data' ..
 *     }
 * }
 *
 * public class MyObjectSizeEstimator extends {@link DefaultObjectSizeEstimator} {
 *
 *     {@literal @Override}
 *     public int estimateSize(Object o) {
 *         if (<b>o instanceof MyRunnable</b>) {
 *             <b>return ((MyRunnable) o).data.length + 8;</b>
 *         }
 *         return super.estimateSize(o);
 *     }
 * }
 *
 * {@link ThreadPoolExecutor} pool = new {@link MemoryAwareThreadPoolExecutor}(
 *         16, 65536, 1048576, 30, {@link TimeUnit}.SECONDS,
 *         <b>new MyObjectSizeEstimator()</b>,
 *         {@link Executors}.defaultThreadFactory());
 *
 * <b>pool.execute(new MyRunnable(data));</b>
 * </pre>
 *
 * <h3>Event execution order</h3>
 *
 * Please note that this executor does not maintain the order of the
 * {@link ChannelEvent}s for the same {@link Channel}.  For example,
 * you can even receive a {@code "channelClosed"} event before a
 * {@code "messageReceived"} event, as depicted by the following diagram.
 *
 * For example, the events can be processed as depicted below:
 *
 * <pre>
 *           --------------------------------&gt; Timeline --------------------------------&gt;
 *
 * Thread X: --- Channel A (Event 2) --- Channel A (Event 1) ---------------------------&gt;
 *
 * Thread Y: --- Channel A (Event 3) --- Channel B (Event 2) --- Channel B (Event 3) ---&gt;
 *
 * Thread Z: --- Channel B (Event 1) --- Channel B (Event 4) --- Channel A (Event 4) ---&gt;
 * </pre>
 *
 * To maintain the event order, you must use {@link OrderedMemoryAwareThreadPoolExecutor}.
 * @apiviz.has io.netty.util.ObjectSizeEstimator oneway - -
 * @apiviz.has io.netty.handler.execution.ChannelEventRunnable oneway - - executes
 */
  class  MemoryAwareThreadPoolExecutor  extends ThreadPoolExecutor {
	

    

	

    

	

    

	
    

	

    /**
     * Creates a new instance.
     *
     * @param corePoolSize          the maximum number of active threads
     * @param maxChannelMemorySize  the maximum total size of the queued events per channel.
     *                              Specify {@code 0} to disable.
     * @param maxTotalMemorySize    the maximum total size of the queued events for this pool
     *                              Specify {@code 0} to disable.
     */
    

	

    /**
     * Creates a new instance.
     *
     * @param corePoolSize          the maximum number of active threads
     * @param maxChannelMemorySize  the maximum total size of the queued events per channel.
     *                              Specify {@code 0} to disable.
     * @param maxTotalMemorySize    the maximum total size of the queued events for this pool
     *                              Specify {@code 0} to disable.
     * @param keepAliveTime         the amount of time for an inactive thread to shut itself down
     * @param unit                  the {@link TimeUnit} of {@code keepAliveTime}
     */
    

	

    /**
     * Creates a new instance.
     *
     * @param corePoolSize          the maximum number of active threads
     * @param maxChannelMemorySize  the maximum total size of the queued events per channel.
     *                              Specify {@code 0} to disable.
     * @param maxTotalMemorySize    the maximum total size of the queued events for this pool
     *                              Specify {@code 0} to disable.
     * @param keepAliveTime         the amount of time for an inactive thread to shut itself down
     * @param unit                  the {@link TimeUnit} of {@code keepAliveTime}
     * @param threadFactory         the {@link ThreadFactory} of this pool
     */
    

	

    /**
     * Creates a new instance.
     *
     * @param corePoolSize          the maximum number of active threads
     * @param maxChannelMemorySize  the maximum total size of the queued events per channel.
     *                              Specify {@code 0} to disable.
     * @param maxTotalMemorySize    the maximum total size of the queued events for this pool
     *                              Specify {@code 0} to disable.
     * @param keepAliveTime         the amount of time for an inactive thread to shut itself down
     * @param unit                  the {@link TimeUnit} of {@code keepAliveTime}
     * @param threadFactory         the {@link ThreadFactory} of this pool
     * @param objectSizeEstimator   the {@link ObjectSizeEstimator} of this pool
     */
    

	
    
    

	

    /**
     * Returns the {@link ObjectSizeEstimator} of this pool.
     */
    

	

    /**
     * Sets the {@link ObjectSizeEstimator} of this pool.
     */
    

	

    /**
     * Returns the maximum total size of the queued events per channel.
     */
    

	

    /**
     * Sets the maximum total size of the queued events per channel.
     * Specify {@code 0} to disable.
     */
    

	

    /**
     * Returns the maximum total size of the queued events for this pool.
     */
    

	

    
    /**
     * @deprecated <tt>maxTotalMemorySize</tt> is not modifiable anymore.
     */
    

	
    
    
    
    

	

    /**
     * Put the actual execution logic here.  The default implementation simply
     * calls {@link #doUnorderedExecute(Runnable)}.
     */
    

	

    /**
     * Executes the specified task without maintaining the event order.
     */
    

	

    

	

    

	

    

	

    

	

    

	

    /**
     * Returns {@code true} if and only if the specified {@code task} should
     * be counted to limit the global and per-channel memory consumption.
     * To override this method, you must call {@code super.shouldCount()} to
     * make sure important tasks are not counted.
     */
    

	

      class  Settings {
		
        

		
        

		

        


	}

	

      class  NewThreadRunsPolicy   {
		
        


	}

	

      class  MemoryAwareRunnable   {
		
        

		
        

		

        

		

        


	}

	


      class  Limiter {
		

        

		
        

		
        

		

        

		

        

		

        


	}

	

    private volatile boolean notifyOnShutdown;

	

    /**
     * This will call {@link #shutdownNow(boolean)} with the value of {@link #getNotifyChannelFuturesOnShutdown()}.
     */
    @Override
    public List<Runnable> shutdownNow() {
        return shutdownNow(notifyOnShutdown);
    }

	
    
    /**
     * See {@link ThreadPoolExecutor#shutdownNow()} for how it handles the shutdown. If <code>true</code> is given to this method it also notifies all {@link ChannelFuture}'s
     * of the not executed {@link ChannelEventRunnable}'s.
     * 
     * <p>
     * Be aware that if you call this with <code>false</code> you will need to handle the notification of the {@link ChannelFuture}'s by your self. So only use this if you
     * really have a use-case for it.
     * </p>
     * 
     */
    public List<Runnable> shutdownNow(boolean notify) {
        if (!notify) {
            return super.shutdownNow();
        }
        Throwable cause = null;
        Set<Channel> channels = null;

        List<Runnable> tasks = super.shutdownNow();

        // loop over all tasks and cancel the ChannelFuture of the ChannelEventRunable's
        for (Runnable task: tasks) {
            if (task instanceof ChannelEventRunnable) {
                if (cause == null) {
                    cause = new IOException("Unable to process queued event");
                }
                ChannelEvent event = ((ChannelEventRunnable) task).getEvent();
                event.getFuture().setFailure(cause);
                
                if (channels == null) {
                    channels = new HashSet<Channel>();
                }
                
                
                // store the Channel of the event for later notification of the exceptionCaught event
                channels.add(event.getChannel());
            }
        }
        
        // loop over all channels and fire an exceptionCaught event
        if (channels != null) {
            for (Channel channel: channels) {
                Channels.fireExceptionCaughtLater(channel, cause);
            }
        }
        return tasks;
    }

	

    /**
     * If set to <code>false</code> no queued {@link ChannelEventRunnable}'s {@link ChannelFuture} will get notified once {@link #shutdownNow()} is called.
     * If set to <code>true</code> every queued {@link ChannelEventRunnable} will get marked as failed via {@link ChannelFuture#setFailure(Throwable)}.
     * 
     * <p>
     * Please only set this to <code>false</code> if you want to handle the notification by yourself and know what you are doing. Default is <code>true</code>.
     * </p>
     */
    public void setNotifyChannelFuturesOnShutdown(boolean notifyOnShutdown) {
        this.notifyOnShutdown = notifyOnShutdown;
    }

	
    
    /**
     * Returns if the {@link ChannelFuture}'s of the {@link ChannelEventRunnable}'s should be notified about the shutdown of this {@link MemoryAwareThreadPoolExecutor}.
     * 
     */
    public boolean getNotifyChannelFuturesOnShutdown() {
        return notifyOnShutdown;
    }


}
