package com.ptransportation.LIN.validation;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public abstract class AbstractValidator {
    public void validate(Object object) {
        if(object != null) {
            runChecks(object.getClass(),object);
            try {
                BeanInfo info = Introspector.getBeanInfo(object.getClass());
                for (PropertyDescriptor property : info.getPropertyDescriptors()) {
                    if(!property.getName().equals("class")) {
                        Method read = property.getReadMethod();
                        Object sub = read.invoke(object);
                        validate(sub);
                    }
                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }

    private void runChecks(Class<?> klass,Object object) {
        for(Method method:this.getClass().getMethods()) {
            if(method.isAnnotationPresent(Check.class)) {
                Class<?> [] parameters = method.getParameterTypes();
                if(parameters.length == 1 && parameters[0].isAssignableFrom(klass)) {
                    try {
                        method.invoke(this, object);
                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
