/**
 * Copyright 2013 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rx.concurrency; 

import java.util.concurrent.TimeUnit; 

import rx.Scheduler; 
import rx.Subscription; 
import rx.util.functions.Func2; 

/* package */ 

/* package */class  SleepingAction <T>  implements Func2<Scheduler, T, Subscription> {
	
    private final Func2<Scheduler, T, Subscription> underlying;

	
    private final Scheduler scheduler;

	
    private final long execTime;

	

    public SleepingAction(Func2<Scheduler, T, Subscription> underlying, Scheduler scheduler, long execTime) {
        this.underlying = underlying;
        this.scheduler = scheduler;
        this.execTime = execTime;
    }

	

    @Override
    public Subscription call(Scheduler s, T state) {
<<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390856533473/fstmerge_var1_9108890961029213988
        if (execTime > scheduler.now()) {
            long delay = execTime - scheduler.now();
            if (delay> 0) {
            try {
                    Thread.sleep(delay);
                }
             catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
=======
        if (execTime > scheduler.now()) {
            long delay = execTime - scheduler.now();
            if (delay> 0) {
                try {
                    Thread.sleep(delay);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390856533473/fstmerge_var2_5681511693728977464
            }
            }
        }

        return underlying.call(s, state);
    }


	

    


}
