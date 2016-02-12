package com.ptransportation.LIN;


public class ErrorModel {
    public void error(String message, Object object, String field) {
        System.err.println(message);
    }

    public void error(String message, Object object, String field, int index) {
        System.err.println(message);
    }
}
