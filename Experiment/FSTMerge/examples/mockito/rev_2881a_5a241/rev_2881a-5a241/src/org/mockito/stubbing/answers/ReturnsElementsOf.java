/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing.answers; 

import java.util.Collection; 
import java.util.LinkedList; 

import org.mockito.exceptions.base.MockitoException; 
import org.mockito.invocation.InvocationOnMock; 
import org.mockito.stubbing.Answer; 

/**
 * Returns elements of the collection. Keeps returning the last element forever.
 * Might be useful on occasion when you have a collection of elements to return.
 * <p>
 * <pre class="code"><code class="java">
 *   //this:
 *   when(mock.foo()).thenReturn(1, 2, 3);
 *   //is equivalent to:
 *   when(mock.foo()).thenReturn(new ReturnsElementsOf(Arrays.asList(1, 2, 3)));
 * <pre class="code"><code class="java">
 */
@Deprecated
public  class  ReturnsElementsOf  extends org.mockito.internal.stubbing.answers.ReturnsElementsOf   {
	

    @Deprecated
    public ReturnsElementsOf(Collection<?> elements) {
        super(elements);
    }

	

    

	

    

	

    


}
