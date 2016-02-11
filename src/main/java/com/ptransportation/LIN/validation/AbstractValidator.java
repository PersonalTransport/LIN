package com.ptransportation.LIN.validation;

import com.ptransportation.LIN.model.Reference;
import com.ptransportation.LIN.util.IPropertyListener;
import com.ptransportation.LIN.util.PropertyWalker;

import java.lang.reflect.Method;

public abstract class AbstractValidator {
    private PropertyWalker walker;

    public AbstractValidator() {
        this.walker = new PropertyWalker();
        this.walker.addPropertyListener(new IPropertyListener() {
            @Override
            public void property(Object self, String propertyName, Object propertyValue) {
                runChecks(propertyValue);
            }
        });
    }

    public void validate(Object object) {
        this.walker.walk(object);
    }

    private void runChecks(Object object) {
        for (Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(Check.class)) {
                Check check = method.getAnnotation(Check.class);
                if (!(object instanceof Reference) || check.checkReference()) {
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
}
