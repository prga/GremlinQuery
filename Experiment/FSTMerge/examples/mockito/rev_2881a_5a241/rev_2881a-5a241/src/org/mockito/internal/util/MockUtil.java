/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util; 
import org.mockito.exceptions.misusing.NotAMockException; 
import org.mockito.internal.InternalMockHandler; 
import org.mockito.internal.InvocationNotifierHandler; 
import org.mockito.internal.MockHandlerImpl; 
import org.mockito.internal.configuration.ClassPathLoader; 
import org.mockito.internal.creation.settings.CreationSettings; 
import org.mockito.internal.util.reflection.LenientCopyTool; 
import org.mockito.mock.MockCreationSettings; 
import org.mockito.mock.MockName; 
import org.mockito.plugins.MockMaker; 

import org.mockito.cglib.proxy.Callback; 
import org.mockito.cglib.proxy.Factory; 
import org.mockito.internal.MockHandler; 
import org.mockito.internal.MockHandlerInterface; 
import org.mockito.internal.creation.MethodInterceptorFilter; 
import org.mockito.internal.creation.MockSettingsImpl; 
import org.mockito.internal.creation.jmock.ClassImposterizer; 

import java.io.Serializable; 

@SuppressWarnings("unchecked")
public  class  MockUtil {
	

    private static final MockMaker mockMaker = ClassPathLoader.getMockMaker();

	

    public <T> T createMock(MockCreationSettings<T> settings) {
        InvocationNotifierHandler<T> mockHandler = new InvocationNotifierHandler<T>(
                new MockHandlerImpl<T>(settings), settings);
        T mock = mockMaker.createMock(settings, mockHandler);

        Object spiedInstance = settings.getSpiedInstance();
        if (spiedInstance != null) {
            new LenientCopyTool().copyToMock(spiedInstance, mock);
        }

        return mock;
    }

	

    public <T> void resetMock(T mock) {
<<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390148773302/fstmerge_var1_1172461132789161359
        MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
        MethodInterceptorFilter newFilter = newMethodInterceptorFilter(oldMockHandler.getMockSettings());
        ((Factory) mock).setCallback(0, newFilter);
=======
        InvocationNotifierHandler oldHandler = (InvocationNotifierHandler) getMockHandler(mock);
        MockCreationSettings settings = oldHandler.getMockSettings();
        InvocationNotifierHandler<T> newHandler = new InvocationNotifierHandler<T>(
                new MockHandlerImpl<T>(settings), settings);
        mockMaker.resetMock(mock, newHandler, settings);
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390148773302/fstmerge_var2_947458539150710245
    }


	

    public <T> InternalMockHandler<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (isMockitoMock(mock)) {
            return (InternalMockHandler) mockMaker.getHandler(mock);
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }


	

    public boolean isMock(Object mock) {
        return mock != null && isMockitoMock(mock);
    }


	

    public boolean isSpy(Object mock) {
        return mock instanceof MockitoSpy && isMock(mock);
    }

	

    private <T> boolean isMockitoMock(T mock) {
        return mockMaker.getHandler(mock) != null;
    }


	

    public MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockSettings().getMockName();
    }


	

    public void maybeRedefineMockName(Object mock, String newName) {
        MockName mockName = getMockName(mock);
        //TODO SF hacky...
        if (mockName.isDefault() && getMockHandler(mock).getMockSettings() instanceof CreationSettings) {
            ((CreationSettings) getMockHandler(mock).getMockSettings()).setMockName(new MockNameImpl(newName));
        }
    }

	
    
    

	

    

	
    
    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390148773713/fstmerge_var1_4897544034956239105
public <T> T createMock(Class<T> classToMock, MockSettingsImpl settings) {
        creationValidator.validateType(classToMock);
        creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        creationValidator.validateMockedType(classToMock, settings.getSpiedInstance());

        settings.initiateMockName(classToMock);

        MethodInterceptorFilter filter = newMethodInterceptorFilter(settings);
        Class<?>[] interfaces = settings.getExtraInterfaces();

        Class<?>[] ancillaryTypes;
        if (settings.isSerializable()) {
            ancillaryTypes = interfaces == null ? new Class<?>[] {Serializable.class} : new ArrayUtils().concat(interfaces, Serializable.class);
        } else {
            ancillaryTypes = interfaces == null ? new Class<?>[0] : interfaces;
        }

        Object spiedInstance = settings.getSpiedInstance();
        
        T mock = ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes);
        
        if (spiedInstance != null) {
            new LenientCopyTool().copyToMock(spiedInstance, mock);
        }
        
        return mock;
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390148773713/fstmerge_var2_758478464185185821


	

    

	

    private <T> MethodInterceptorFilter newMethodInterceptorFilter(MockSettingsImpl settings) {
        MockHandler<T> mockHandler = new MockHandler<T>(settings);
        InvocationNotifierHandler<T> invocationNotifierHandler = new InvocationNotifierHandler<T>(mockHandler, settings);
        return new MethodInterceptorFilter(invocationNotifierHandler, settings);
    }


}
