/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration; 

import org.mockito.exceptions.Reporter; 
import org.mockito.exceptions.base.MockitoException; 
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter; 
import org.mockito.internal.configuration.injection.MockCandidateFilter; 
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter; 
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter; 
import org.mockito.internal.util.reflection.FieldInitializer; 

import java.lang.reflect.Field; 
import java.util.Arrays; 
import java.util.Comparator; 
import java.util.HashSet; 
import java.util.Set; 
import org.mockito.internal.util.reflection.AccessibilityChanger; 
import org.mockito.internal.util.reflection.ConstructorInitializer; 

/**
 * Initializes mock/spies dependencies for fields annotated with
 * &#064;InjectMocks
 * <p/>
 * See {@link org.mockito.MockitoAnnotations}
 */
public  class  DefaultInjectionEngine {
	

	private final MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(
			new NameBasedCandidateFilter(new FinalMockCandidateFilter()));

	
    private Comparator<Field> supertypesLast = new Comparator<Field>() {
        public int compare(Field field1, Field field2) {
            Class<?> field1Type = field1.getType();
            Class<?> field2Type = field2.getType();

            if(field1Type.isAssignableFrom(field2Type)) {
                return 1;
            }
            if(field2Type.isAssignableFrom(field1Type)) {
                return -1;
            }
            return 0;
        }
    };

	

	// for each tested
	// - for each field of tested
	// - find mock candidate by type
	// - if more than *one* find mock candidate on name
	// - if one mock candidate then set mock
	// - else don't fail, user will then provide dependencies
	<<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390148580384/fstmerge_var1_7797864230939258834
public void injectMocksOnFields(Set<Field> testClassFields,
			Set<Object> mocks, Object testClass) {
		for (Field field : testClassFields) {
			if (hasDefaultConstructor(field, testClass) || alreadyInitialized(field, testClass)) {
				setterInject(mocks, testClass, field);
			} else {
				constructorInject(mocks, testClass, field);
			}
		}
	}
=======
public void injectMocksOnFields(Set<Field> injectMocksFields, Set<Object> mocks, Object testClassInstance) {
        for (Field field : injectMocksFields) {
            Set<Object> mocksToBeInjected = new HashSet<Object>(mocks);
            Object injectMocksFieldInstance = null;
            try {
                injectMocksFieldInstance = new FieldInitializer(testClassInstance, field).initialize();
            } catch (MockitoException e) {
                new Reporter().cannotInitializeForInjectMocksAnnotation(field.getName(), e);
            }

            // for each field in the class hierarchy
            Class<?> fieldClass = injectMocksFieldInstance.getClass();
            while (fieldClass != Object.class) {
                injectMockCandidate(fieldClass, mocksToBeInjected, injectMocksFieldInstance);
                fieldClass = fieldClass.getSuperclass();
            }
        }
    }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390148580384/fstmerge_var2_4745273858594022752


	

	<<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390148580481/fstmerge_var1_3898777732391376155
private void injectMockCandidate(Class<?> awaitingInjectionClazz,
			Set<Object> mocks, Object fieldInstance) {
		for (Field field : awaitingInjectionClazz.getDeclaredFields()) {
			mockCandidateFilter.filterCandidate(mocks, field, fieldInstance)
					.thenInject();
		}
	}
=======
private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
        for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
            Object injected = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
            mocks.remove(injected);
        }
    }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390148580481/fstmerge_var2_4209780710093533708


	

    private Field[] orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        Field[] declaredFields = awaitingInjectionClazz.getDeclaredFields();
        Arrays.sort(declaredFields, supertypesLast);
        return declaredFields;
    }

	

	private void constructorInject(Set<Object> mocks, Object testClass,
			Field field) {
		new ConstructorInitializer(field, testClass).initialize(mocks);
	}

	

	private void setterInject(Set<Object> mocks, Object testClass, Field field) {
		try {
			Object fieldInstance = new FieldInitializer(testClass, field)
					.initialize();
			Class<?> fieldClass = fieldInstance.getClass();
			// for each field in the class hierarchy
			while (fieldClass != Object.class) {
				injectMockCandidate(fieldClass, mocks, fieldInstance);
				fieldClass = fieldClass.getSuperclass();
			}
		} catch (Exception e) {
			new Reporter().cannotInitializeForInjectMocksAnnotation(
					field.getName(), e);
		}

	}

	

	private boolean hasDefaultConstructor(Field field, Object testClass) {
		try {
			field.getType().getDeclaredConstructor();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	

	private boolean alreadyInitialized(Field field, Object testClass) {
		final AccessibilityChanger changer = new AccessibilityChanger();
		changer.enableAccess(field);
		try {
			return field.get(testClass) != null;
		} catch (Exception e) {
			return false;
		} finally {
			changer.safelyDisableAccess(field);
		}
	}


}
