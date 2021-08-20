package org.wso2.siddhi.android.platform.util;

/**
 * This Exception wraps the errors thrown by Siddhi
 */
public class SiddhiAndroidException extends RuntimeException {

    public SiddhiAndroidException() {
        super();
    }

    public SiddhiAndroidException(String exception) {
        super(exception);
    }

    public SiddhiAndroidException(Throwable exception) {
        super(exception);
    }
}
