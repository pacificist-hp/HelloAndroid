package com.android.pacificist.ioc;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public class InjectHandler implements InvocationHandler {

    private WeakReference<Object> handlerRef;
    private final HashMap<String, Method> methodMap = new HashMap<>(1);

    InjectHandler(Object handler) {
        this.handlerRef = new WeakReference<>(handler);
    }

    void addMethod(String name, Method method) {
        methodMap.put(name, method);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        Object handler = handlerRef.get();
        if (handler == null) {
            return null;
        }

        String methodName = method.getName();
        method = methodMap.get(methodName);
        if (method == null) {
            return null;
        }

        return method.invoke(handler, args);
    }
}
