package com.android.pacificist.helloandroid.rxbus;

/**
 * Created by pacificist on 2018/10/20.
 */
public class RxEvent {

    public static final String KEY_DISABLE_SCROLL = "disable_scroll";
    public static final String KEY_ENABLE_SCROLL = "enable_scroll";

    public RxEvent(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String key;
    public Object value;
}
