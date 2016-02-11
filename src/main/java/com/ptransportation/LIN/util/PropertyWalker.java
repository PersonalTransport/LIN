package com.ptransportation.LIN.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PropertyWalker {
    private List<IPropertyListener> listeners;

    public PropertyWalker() {
        this.listeners = new ArrayList<IPropertyListener>();
    }

    public void addPropertyListener(IPropertyListener listener) {
        this.listeners.add(listener);
    }

    public void removePropertyListener(IPropertyListener listener) {
        this.listeners.remove(listener);
    }

    public void walk(Object object, String name) {
        ArrayList<Object> validatedObjects = new ArrayList<Object>();
        walk(null, object.getClass(), name, object, validatedObjects);
    }

    public void walk(Object object) {
        walk(object, null);
    }

    private void walk(Object self, Class<?> klass, String name, Object object, List<Object> alreadyWalked) {
        for (IPropertyListener listener : listeners)
            listener.property(self, klass, name, object);
        alreadyWalked.add(object);
        try {
            if (object != null) {
                BeanInfo info = Introspector.getBeanInfo(object.getClass());
                for (PropertyDescriptor property : info.getPropertyDescriptors()) {
                    Method read = property.getReadMethod();
                    if (!property.getName().equals("class") && read != null) {
                        Object sub = read.invoke(object);
                        if (!alreadyWalked.contains(sub))
                            walk(object, property.getPropertyType(), property.getName(), sub, alreadyWalked);
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
