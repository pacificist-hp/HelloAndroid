package com.android.pacificist.stone.engine.token;

public class NumToken extends Token {
    private int value;

    public NumToken(int line, int value) {
        super(line);
        this.value = value;
    }

    public boolean isNumber() {
        return true;
    }

    public String getText() {
        return Integer.toString(value);
    }

    public int getNumber() {
        return value;
    }
}
