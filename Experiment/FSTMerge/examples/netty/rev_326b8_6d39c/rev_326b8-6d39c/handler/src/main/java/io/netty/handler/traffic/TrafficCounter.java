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
package io.netty.handler.traffic; 

import java.util.concurrent.Executor; 
import java.util.concurrent.atomic.AtomicBoolean; 
import java.util.concurrent.atomic.AtomicLong; 

import io.netty.channel.ChannelHandlerContext; 

import java.util.concurrent.TimeUnit; 
import io.netty.util.Timeout; 
import io.netty.util.Timer; 
import io.netty.util.TimerTask; 

/**
 * TrafficCounter is associated with {@link AbstractTrafficShapingHandler}.<br>
 * <br>
 * A TrafficCounter has for goal to count the traffic in order to enable to limit the traffic or not,
 * globally or per channel. It compute statistics on read and written bytes at the specified
 * interval and call back the {@link AbstractTrafficShapingHandler} doAccounting method at every
 * specified interval. If this interval is set to 0, therefore no accounting will be done and only
 * statistics will be computed at each receive or write operations.
 */
  class  TrafficCounter {
	
    /**
     * Current written bytes
     */
    

	

    /**
     * Current read bytes
     */
    

	

    /**
     * Long life written bytes
     */
    

	

    /**
     * Long life read bytes
     */
    

	

    /**
     * Last Time where cumulative bytes where reset to zero
     */
    

	

    /**
     * Last writing bandwidth
     */
    

	

    /**
     * Last reading bandwidth
     */
    

	

    /**
     * Last Time Check taken
     */
    

	

    /**
     * Last written bytes number during last check interval
     */
    

	

    /**
     * Last read bytes number during last check interval
     */
    

	

    /**
     * Delay between two captures
     */
    

	

    // default 1 s

    /**
     * Name of this Monitor
     */
    

	

    /**
     * The associated TrafficShapingHandler
     */
    ~~FSTMerge~~ private final AbstractTrafficShapingHandler trafficShapingHandler; ##FSTMerge## private AbstractTrafficShapingHandler trafficShapingHandler; ##FSTMerge##

	

    /**
     * Default Executor
     */
    private Executor executor;

	

    /**
     * Is Monitor active
     */
    

	

    /**
     * Monitor
     */
    private TrafficMonitoring trafficMonitoring;

	

    /**
     * Class to implement monitoring at fix delay
 */
    private  class  TrafficMonitoring  implements Runnable {
		
        /**
         * The associated TrafficShapingHandler
         */
        private final AbstractTrafficShapingHandler trafficShapingHandler1;

		

        /**
         * The associated TrafficCounter
         */
        private final TrafficCounter counter;

		

        /**
         * @param trafficShapingHandler
         * @param counter
         */
        protected TrafficMonitoring(
                AbstractTrafficShapingHandler trafficShapingHandler,
                TrafficCounter counter) {
            trafficShapingHandler1 = trafficShapingHandler;
            this.counter = counter;
        }

		

        /**
         * Default run
         */
        @Override
        public void run() {
            try {
                Thread.currentThread().setName(name);
                for (; monitorActive.get();) {
                    long check = counter.checkInterval.get();
                    if (check > 0) {
                        Thread.sleep(check);
                    } else {
                        // Delay goes to 0, so exit
                        return;
                    }
                    long endTime = System.currentTimeMillis();
                    counter.resetAccounting(endTime);
                    if (trafficShapingHandler1 != null) {
                        trafficShapingHandler1.doAccounting(counter);
                    }
                }
            } catch (InterruptedException e) {
                // End of computations
            }
        }


	}

	

    /**
     * Start the monitoring process
     */
    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772195513/fstmerge_var1_6910933124740155267
public void start() {
        synchronized (lastTime) {
            if (monitorActive.get()) {
                return;
            }
            lastTime.set(System.currentTimeMillis());
            if (checkInterval.get() > 0) {
                monitorActive.set(true);
                timerTask = new TrafficMonitoringTask(trafficShapingHandler, this);
                timeout = 
                    timer.newTimeout(timerTask, checkInterval.get(), TimeUnit.MILLISECONDS);
            }
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772195513/fstmerge_var2_3523092350473757749


	

    /**
     * Stop the monitoring process
     */
    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772195564/fstmerge_var1_7650791482397644632
public void stop() {
        synchronized (lastTime) {
            if (!monitorActive.get()) {
                return;
            }
            monitorActive.set(false);
            resetAccounting(System.currentTimeMillis());
            if (trafficShapingHandler != null) {
                trafficShapingHandler.doAccounting(this);
            }
            if (timeout != null) {
                timeout.cancel();
            }
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772195564/fstmerge_var2_8175967947945462189


	

    /**
     * Reset the accounting on Read and Write
     *
     * @param newLastTime
     */
    

	

    /**
     * Constructor with the {@link AbstractTrafficShapingHandler} that hosts it, the executorService to use, its
     * name, the checkInterval between two computations in millisecond
     * @param trafficShapingHandler the associated AbstractTrafficShapingHandler
     * @param executor
     *            Should be a CachedThreadPool for efficiency
     * @param name
     *            the name given to this monitor
     * @param checkInterval
     *            the checkInterval in millisecond between two computations
     */
    public TrafficCounter(AbstractTrafficShapingHandler trafficShapingHandler,
            Executor executor, String name, long checkInterval) {
        this.trafficShapingHandler = trafficShapingHandler;
        this.executor = executor;
        this.name = name;
        lastCumulativeTime = System.currentTimeMillis();
        configure(checkInterval);
    }

	

    /**
     * Change checkInterval between
     * two computations in millisecond
     *
     * @param newcheckInterval
     */
    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772195665/fstmerge_var1_2220957700718232917
public void configure(long newcheckInterval) {
        long newInterval = (newcheckInterval / 10) * 10;
        if (checkInterval.get() != newInterval) {
            checkInterval.set(newInterval);
            if (newInterval <= 0) {
                stop();
                // No more active monitoring
                lastTime.set(System.currentTimeMillis());
            } else {
                // Start if necessary
                start();
            }
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772195665/fstmerge_var2_8480514612806586534


	

    /**
     * Computes counters for Read.
     *
     * @param ctx
     *            the associated channelHandlerContext
     * @param recv
     *            the size in bytes to read
     */
    

	

    /**
     * Computes counters for Write.
     *
     * @param write
     *            the size in bytes to write
     */
    

	

    /**
     *
     * @return the current checkInterval between two computations of traffic counter
     *         in millisecond
     */
    

	

    /**
     *
     * @return the Read Throughput in bytes/s computes in the last check interval
     */
    

	

    /**
     *
     * @return the Write Throughput in bytes/s computes in the last check interval
     */
    

	

    /**
     *
     * @return the number of bytes read during the last check Interval
     */
    

	

    /**
     *
     * @return the number of bytes written during the last check Interval
     */
    

	

    /**
    *
    * @return the current number of bytes read since the last checkInterval
    */
    

	

    /**
     *
     * @return the current number of bytes written since the last check Interval
     */
    

	

    /**
     * @return the Time in millisecond of the last check as of System.currentTimeMillis()
     */
    

	

    /**
     * @return the cumulativeWrittenBytes
     */
    

	

    /**
     * @return the cumulativeReadBytes
     */
    

	

    /**
     * @return the lastCumulativeTime in millisecond as of System.currentTimeMillis()
     * when the cumulative counters were reset to 0.
     */
    

	

    /**
     * Reset both read and written cumulative bytes counters and the associated time.
     */
    

	

    /**
     * @return the name
     */
    

	

    /**
     * String information
     */
    

	

    /**
     * One Timer for all Counter
     */
    private final Timer timer;

	  // replace executor
    /**
     * Monitor created once in start()
     */
    private TimerTask timerTask;

	
    /**
     * used in stop() to cancel the timer
     */
    private volatile Timeout timeout;

	

    /**
     * Class to implement monitoring at fix delay
     *
     */
    private static  class  TrafficMonitoringTask  implements TimerTask {
		
        /**
         * The associated TrafficShapingHandler
         */
        private final AbstractTrafficShapingHandler trafficShapingHandler1;

		

        /**
         * The associated TrafficCounter
         */
        private final TrafficCounter counter;

		

        /**
         * @param trafficShapingHandler
         * @param counter
         */
        protected TrafficMonitoringTask(
                AbstractTrafficShapingHandler trafficShapingHandler,
                TrafficCounter counter) {
            trafficShapingHandler1 = trafficShapingHandler;
            this.counter = counter;
        }

		

        public void run(Timeout timeout) throws Exception {
            if (!counter.monitorActive.get()) {
                return;
            }
            long endTime = System.currentTimeMillis();
            counter.resetAccounting(endTime);
            if (trafficShapingHandler1 != null) {
                trafficShapingHandler1.doAccounting(counter);
            }
            timeout = 
                counter.timer.newTimeout(this, counter.checkInterval.get(), TimeUnit.MILLISECONDS);                        
        }


	}

	

    /**
     * Constructor with the {@link AbstractTrafficShapingHandler} that hosts it, the Timer to use, its
     * name, the checkInterval between two computations in millisecond
     * @param trafficShapingHandler the associated AbstractTrafficShapingHandler
     * @param timer
     *            Could be a HashedWheelTimer
     * @param name
     *            the name given to this monitor
     * @param checkInterval
     *            the checkInterval in millisecond between two computations
     */
    public TrafficCounter(AbstractTrafficShapingHandler trafficShapingHandler,
            Timer timer, String name, long checkInterval) {
        this.trafficShapingHandler = trafficShapingHandler;
        this.timer = timer;
        this.name = name;
        lastCumulativeTime = System.currentTimeMillis();
        configure(checkInterval);
    }


}
