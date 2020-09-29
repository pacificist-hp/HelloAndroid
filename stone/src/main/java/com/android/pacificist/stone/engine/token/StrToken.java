package com.android.pacificist.stone.engine.token;

public class StrToken extends Token {
    private String literal;

    public StrToken(int line, String literal) {
        super(line);
        this.literal = literal;
    }

    public boolean isString() {
        return true;
    }

    public String getText() {
        return literal;
    }
}
