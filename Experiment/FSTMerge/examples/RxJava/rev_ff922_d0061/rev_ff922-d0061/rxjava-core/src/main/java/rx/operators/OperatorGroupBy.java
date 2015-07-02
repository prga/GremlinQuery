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
package rx.operators; 

import static org.junit.Assert.*; 

import java.util.ArrayList; 
import java.util.Arrays; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
import java.util.concurrent.ConcurrentHashMap; 

import org.junit.Test; 

import rx.Observable; 
import rx.Observer; 
import rx.Subscription; 
import rx.observables.GroupedObservable; 
import rx.util.functions.Func1; 
import rx.util.functions.Functions; 

  class  OperatorGroupBy {
	

    

	

    

	

      class  GroupBy <K, V>   {
		
        

		

        

		

        <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1391034197865/fstmerge_var1_2995997177981715503
=======
@Override
        public Subscription call(final Observer<GroupedObservable<K, V>> observer) {
            return source.subscribe(new GroupByObserver(observer));
        }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1391034197865/fstmerge_var2_3432965698962396586


		

        private  class  GroupByObserver  implements Observer<KeyValue<K, V>> {
			
            private final Observer<GroupedObservable<K, V>> underlying;

			

            private final ConcurrentHashMap<K, Boolean> keys = new ConcurrentHashMap<K, Boolean>();

			

            private GroupByObserver(Observer<GroupedObservable<K, V>> underlying) {
                this.underlying = underlying;
            }

			

            @Override
            public void onCompleted() {
                underlying.onCompleted();
            }

			

            @Override
            public void onError(Exception e) {
                underlying.onError(e);
            }

			

            @Override
            public void onNext(final KeyValue<K, V> args) {
                K key = args.key;
                boolean newGroup = keys.putIfAbsent(key, true) == null;
                if (newGroup) {
                    underlying.onNext(buildObservableFor(source, key));
                }
            }


		}

		
        private final ConcurrentHashMap<K, Boolean> keys = new ConcurrentHashMap<K, Boolean>();


	}

	

    

	

      class  KeyValue <K, V> {
		
        

		
        

		

        


	}

	

      class  UnitTest {
		
        

		

        

		

        

		

        


	}


}
