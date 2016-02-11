package com.ptransportation.LIN.validation;

import com.ptransportation.LIN.model.Reference;
import com.ptransportation.LIN.util.IPropertyListener;
import com.ptransportation.LIN.util.PropertyWalker;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractValidator implements IPropertyListener {
    private PropertyWalker walker;
    public AbstractValidator() {
        this.walker = new PropertyWalker();
        this.walker.addPropertyListener(this);
    }

    public void validate(Object object) {
        this.walker.walk(object);
    }

    @Override
    public void property(Object self,Class<?> propertyClass,String propertyName,Object propertyValue) {
        if(propertyValue instanceof Iterable<?>) {
            for(Object o:(Iterable<?>)propertyValue)
                runChecks(o.getClass(), o);
        }
        else {
            runChecks(propertyClass, propertyValue);
        }
    }

    /*public void validate(Object object) {
        ArrayList<Object> validatedObjects = new ArrayList<Object>();
        validate(object,validatedObjects);
    }

    private void validate(Object object, List<Object> alreadyValidated) {
        if(object != null) {
            if(object instanceof Iterable<?>) {
                for(Object o:(Iterable<?>)object)
                    validate(o,alreadyValidated);
            }
            else {
                runChecks(object.getClass(), object);
                alreadyValidated.add(object);
                try {
                    BeanInfo info = Introspector.getBeanInfo(object.getClass());
                    for (PropertyDescriptor property : info.getPropertyDescriptors()) {
                        Method read = property.getReadMethod();
                        if(read != null) {
                            Object sub = read.invoke(object);
                            if (!alreadyValidated.contains(sub))
                                validate(sub, alreadyValidated);
                        }
                    }
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    private void runChecks(Class<?> klass,Object object) {
        for(Method method:this.getClass().getMethods()) {
            if(method.isAnnotationPresent(Check.class)) {
                Check check = method.getAnnotation(Check.class);
                if(!Reference.class.isAssignableFrom(klass) || check.checkReference()) {
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
}
