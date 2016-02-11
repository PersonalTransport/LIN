package com.ptransportation.LIN.link;

import com.ptransportation.LIN.util.IPropertyListener;
import com.ptransportation.LIN.util.PropertyWalker;

import java.lang.reflect.Method;

public abstract class AbstractLinker {
    private PropertyWalker linkWalker;

    public AbstractLinker() {
        this.linkWalker = new PropertyWalker();
        this.linkWalker.addPropertyListener(new IPropertyListener() {
            @Override
            public void property(Object self, String propertyName, Object propertyValue) {
                runLinks(propertyValue);
            }
        });
    }

    public void link(Object object) {
        this.linkWalker.walk(object);
    }

    private void runLinks(Object object) {
        for (Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(Link.class)) {
                Link link = method.getAnnotation(Link.class);
                Class<?>[] parameters = method.getParameterTypes();
                if (parameters.length == 1 && parameters[0].isAssignableFrom(object.getClass())) {
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
