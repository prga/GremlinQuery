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
package rx.lang.groovy; 
import groovy.lang.Closure; 
import rx.util.functions.FunctionLanguageAdaptor; 

import static org.mockito.Matchers.*; 
import static org.mockito.Mockito.*; 
import groovy.lang.Binding; 
import groovy.lang.GroovyClassLoader; 

import java.util.Arrays; 

import org.codehaus.groovy.runtime.InvokerHelper; 
import org.junit.Before; 
import org.junit.Test; 
import org.mockito.Mock; 
import org.mockito.MockitoAnnotations; 

import rx.Notification; 
import rx.Observable; 
import rx.Observer; 
import rx.Subscription; 
import rx.util.functions.Func1; 

public  class  GroovyAdaptor  implements FunctionLanguageAdaptor {
	

    @Override
    public Object call(Object function, Object[] args) {
        return ((Closure<?>) function).call(args);
    }


	

    public Class<?>[] getFunctionClass() {
        return new Class<?>[] { Closure.class };
    }


	

      class  UnitTest {
		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

          interface  ScriptAssertion {
			
            

			

            


		}

		

          class  TestFactory {
			
            

			

            

			

            


		}

		

          class  TestObservable  extends Observable<String> {
			
            

			

            

			

            


		}

		

        @Test
        public void testTakeWhileViaGroovy() {
            runGroovyScript("o.takeWhile(o.toObservable(1, 2, 3), { x -> x < 3}).subscribe({ result -> a.received(result)});");
            verify(assertion, times(1)).received(1);
            verify(assertion, times(1)).received(2);
            verify(assertion, times(0)).received(3);
        }

		

        @Test
        public void testTakeWhileWithIndexViaGroovy() {
            runGroovyScript("o.takeWhileWithIndex(o.toObservable(1, 2, 3), { x, i -> i < 2}).subscribe({ result -> a.received(result)});");
            verify(assertion, times(1)).received(1);
            verify(assertion, times(1)).received(2);
            verify(assertion, times(0)).received(3);
        }


	}


}
