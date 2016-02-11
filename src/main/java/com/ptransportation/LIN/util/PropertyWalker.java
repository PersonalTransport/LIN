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
        walk(null, name, object, validatedObjects);
    }

    public void walk(Object object) {
        walk(object, null);
    }

    private void walk(Object self, String propertyName, Object propertyValue, List<Object> alreadyWalked) {
        if (propertyValue == null)
            return;
        for (IPropertyListener listener : listeners)
            listener.property(self, propertyName, propertyValue);
        alreadyWalked.add(propertyValue);
        try {
            BeanInfo info = Introspector.getBeanInfo(propertyValue.getClass());
            for (PropertyDescriptor property : info.getPropertyDescriptors()) {
                Method read = property.getReadMethod();
                if (!property.getName().equals("class") && read != null) {
                    Object sub = read.invoke(propertyValue);
                    if (sub instanceof Iterable<?>) {
                        int i = 0;
                        for (Object o : (Iterable<?>) sub) {
                            if (o != null && !alreadyWalked.contains(o))
                                walk(propertyValue, property.getName() + "[" + i + "]", o, alreadyWalked);
                            i++;
                        }
                    } else if (!alreadyWalked.contains(sub)) {
                        walk(propertyValue, property.getName(), sub, alreadyWalked);
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
