package com.android.pacificist.bridge;

/**
 * Created by pacificist on 2020/4/16.
 */
public class Value {

    public static final int TYPE_NONE = -1;
    public static final int TYPE_STRING = 2;

    public int type;
    public String stringVal;

    public Value() {
        type = TYPE_NONE;
    }

    public Value(String val) {
        type = TYPE_STRING;
        stringVal = val;
    }

    @Override
    public String toString() {
        switch (type) {
            case TYPE_STRING:
                return stringVal;
            default:
                return "null";
        }
    }
}
