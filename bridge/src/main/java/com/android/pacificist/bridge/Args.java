package com.android.pacificist.bridge;

/**
 * Created by pacificist on 2020/4/25.
 */
public class Args {

    public static final int TYPE_NONE = -1;
    public static final int TYPE_STRING = 2;

    public int[] types;
    public String[] strings;

    public Args(Object... args) {
        types = new int[args.length];
        strings = new String[args.length];

        for(int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof String) {
                types[i] = TYPE_STRING;
                strings[i] = arg.toString();
            } else {
                types[i] = TYPE_NONE;
            }
        }
    }
}
