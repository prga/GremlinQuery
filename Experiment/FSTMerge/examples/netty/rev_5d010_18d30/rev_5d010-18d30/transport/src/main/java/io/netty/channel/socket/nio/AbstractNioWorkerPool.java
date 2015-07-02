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

import io.netty.channel.Channel; 
import io.netty.channel.socket.Worker; 
import io.netty.util.ExternalResourceReleasable; 
import io.netty.util.internal.ExecutorUtil; 

import java.util.concurrent.Executor; 
import java.util.concurrent.atomic.AtomicInteger; 

/**
 * Abstract base class for {@link WorkerPool} implementations that create the {@link Worker}'s up-front and return them in a "fair" fashion when calling
 * {@link #nextWorker()}
 *
 */
  class  AbstractNioWorkerPool <E extends AbstractNioWorker>   {
	

    

	
    

	
    

	
    
    /**
     * Create a new instance
     * 
     * @param workerExecutor the {@link Executor} to use for the {@link Worker}'s
     * @param allowShutdownOnIdle allow the {@link Worker}'s to shutdown when there is not {@link Channel} is registered with it
     * @param workerCount the count of {@link Worker}'s to create
     */
    

	

    /**
     * Create a new {@link Worker} which uses the given {@link Executor} to service IO
     * 
     * 
     * @param executor the {@link Executor} to use
     * @param allowShutdownOnIdle allow the {@link Worker} to shutdown when there is not {@link Channel} is registered with it
     * @return worker the new {@link Worker} 
     */
    

	

    

	

    


}