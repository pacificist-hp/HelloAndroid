package com.android.pacificist.bridge;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pacificist on 2020/4/16.
 */
public class Bridge {
    private static Bridge sInstance;

    private Bridge() { }

    public static Bridge instance() {
        if (sInstance == null) {
            sInstance = new Bridge();
        }
        return sInstance;
    }

    public interface Callable {
        Value call(Evaluator evaluator, Value[] params);
    }

    private Map<String, Callable> mFunctionCallableMap = new HashMap<>();

    public void registerFunction(String name, int paramNum, Callable callable) {
        mFunctionCallableMap.put(name, callable);
        nativeRegisterFunction(name, paramNum);
    }

    private native void nativeRegisterFunction(String name, int paramNum);

    static {
        System.loadLibrary("bridge-lib");
    }
}
