package com.ptransportation.LIN.util;

public interface IPropertyListener {
    void property(Object self, Class<?> propertyClass, String propertyName, Object propertyValue);
}
