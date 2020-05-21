package com.android.pacificist.bridge;

/**
 * Created by pacificist on 2020/4/16.
 */
public class Value {

    public static final int TYPE_NONE = 0;
    public static final int TYPE_INT = 1;
    public static final int TYPE_FLOAT = 2;
    public static final int TYPE_BOOL = 3;
    public static final int TYPE_STRING = 4;

    public int type;
    public long intVal;
    public float floatVal;
    public boolean boolVal;
    public String stringVal;

    public Value() {
        type = TYPE_NONE;
    }

    public Value(long val) {
        type = TYPE_INT;
        intVal = val;
    }

    public Value(float val) {
        type = TYPE_FLOAT;
        floatVal = val;
    }

    public Value(boolean val) {
        type = TYPE_BOOL;
        boolVal = val;
    }

    public Value(String val) {
        type = TYPE_STRING;
        stringVal = val;
    }

    @Override
    public String toString() {
        switch (type) {
            case TYPE_INT:
                return String.valueOf(intVal);
            case TYPE_FLOAT:
                return String.valueOf(floatVal);
            case TYPE_BOOL:
                return String.valueOf(boolVal);
            case TYPE_STRING:
                return stringVal;
            default:
                return "null";
        }
    }
}
