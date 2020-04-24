package com.android.pacificist.bridge;

import android.text.TextUtils;

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
        Value call(Value[] params);
    }

    private Map<String, Callable> mCallableMap = new HashMap<>();

    public int loadCode(String code) throws Exception {
        if (!TextUtils.isEmpty(code)) {
            return nativeLoadCode(code);
        }

        return -1;
    }

    public Value invoke(int id, String name, Args args) throws Exception {
        if (id < 0) {
            throw new IllegalArgumentException("id is illegal");
        }
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("function name is empty");
        }

        if (args == null) {
            args = new Args();
        }

        return (Value) BridgeClient.instance().nativeInvoke(id, name, args);
    }

    public void release(int id) {
        nativeRelease(id);
    }

    public void registerFunctionCallable(String name, int paramNum, Callable callable) {
        mCallableMap.put(name, callable);
        nativeRegisterFunction(name, paramNum);
    }

    public static Value callback(String name, Value[] params) {
        Callable callable = instance().mCallableMap.get(name);
        if (callable != null) {
            try {
                return callable.call(params);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return new Value("java callback exception");
    }

    private static native int nativeInit();

    private native void nativeRegisterFunction(String name, int paramNum);

    native int nativeLoadCode(String code) throws Exception;

    native Object nativeInvoke(int id, String name, Object args) throws Exception;

    native void nativeRelease(int id);

    static {
        System.loadLibrary("bridge-lib");
        VERSION_CODE = nativeInit();
    }
}
