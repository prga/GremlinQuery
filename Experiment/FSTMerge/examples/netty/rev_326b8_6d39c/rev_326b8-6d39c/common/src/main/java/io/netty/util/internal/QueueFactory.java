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
package io.netty.util.internal; 

import java.util.Collection; 
import java.util.concurrent.BlockingQueue; 

import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 

/**
 * This factory should be used to create the "optimal" {@link BlockingQueue}
 * instance for the running JVM.
 */
public final  class  QueueFactory {
	
    
    private static final boolean useUnsafe = DetectionUtil.hasUnsafe();

	
    
    private QueueFactory() {
        // only use static methods!
    }


	
    
    
    /**
     * Create a new unbound {@link BlockingQueue} 
     * 
     * @param itemClass  the {@link Class} type which will be used as {@link BlockingQueue} items
     * @return queue     the {@link BlockingQueue} implementation
     */
    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772188151/fstmerge_var1_1416437961308749117
public static <T> BlockingQueue<T> createQueue(Class<T> itemClass) {
        try {
            if (useUnsafe) {
                return new LinkedTransferQueue<T>();
            }
        } catch (Throwable t) {
            // For whatever reason an exception was thrown while loading the LinkedTransferQueue
            //
            // This mostly happens because of a custom classloader or security policy that did not allow us to access the
            // com.sun.Unmisc class. So just log it and fallback to the old LegacyLinkedTransferQueue that works in all cases
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Unable to instance LinkedTransferQueue, fallback to LegacyLinkedTransferQueue", t);
            }
        }
        
        return new LegacyLinkedTransferQueue<T>();
       
=======
public static <T> BlockingQueue<T> createQueue(Class<T> itemClass) {
        if (useUnsafe) {
            return new LinkedTransferQueue<T>();
        } else {
            return new LegacyLinkedTransferQueue<T>();
        }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772188151/fstmerge_var2_1625645184595685588
    }


	
    
    /**
     * Create a new unbound {@link BlockingQueue} 
     * 
     * @param collection  the collection which should get copied to the newly created {@link BlockingQueue}
     * @param itemClass   the {@link Class} type which will be used as {@link BlockingQueue} items
     * @return queue      the {@link BlockingQueue} implementation
     */
    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772188203/fstmerge_var1_1073176475920007430
public static <T> BlockingQueue<T> createQueue(Collection<? extends T> collection, Class<T> itemClass) {
        try {
            if (useUnsafe) {
                return new LinkedTransferQueue<T>(collection);
            }
        } catch (Throwable t) {
            // For whatever reason an exception was thrown while loading the LinkedTransferQueue
            //
            // This mostly happens because of a custom classloader or security policy that did not allow us to access the
            // com.sun.Unmisc class. So just log it and fallback to the old LegacyLinkedTransferQueue that works in all cases
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Unable to instance LinkedTransferQueue, fallback to LegacyLinkedTransferQueue", t);
            }
        }
         
        return new LegacyLinkedTransferQueue<T>(collection);
        
=======
public static <T> BlockingQueue<T> createQueue(Collection<? extends T> collection, Class<T> itemClass) {
        if (useUnsafe) {
            return new LinkedTransferQueue<T>(collection);
        } else {
            return new LegacyLinkedTransferQueue<T>(collection);
        }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772188203/fstmerge_var2_7507864690527747415
    }


	
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(QueueFactory.class);


}
