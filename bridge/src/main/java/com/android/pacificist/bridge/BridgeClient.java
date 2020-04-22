package com.android.pacificist.bridge;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pacificist on 2020/4/16.
 */
public class BridgeClient {

    public static int VERSION_CODE;

    private static BridgeClient sInstance;

    private BridgeClient() {
    }

    public static BridgeClient instance() {
        if (sInstance == null) {
            sInstance = new BridgeClient();
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

    public static Value callFunction(int bridgeId, int evaluatorId, String name, Value[] params) {

        return new Value("javaCallFunction: " + params[0].toString());
    }

    private static native int nativeInit();

    private native void nativeRegisterFunction(String name, int paramNum);

    static {
        System.loadLibrary("bridge-lib");
        VERSION_CODE = nativeInit();
    }
}
