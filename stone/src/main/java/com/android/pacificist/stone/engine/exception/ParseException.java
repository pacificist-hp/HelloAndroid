package com.android.pacificist.stone.engine.exception;

import com.android.pacificist.stone.engine.token.Token;

public class ParseException extends Exception {

    public ParseException(Token t) {
        this("", t);
    }

    public ParseException(String msg, Token t) {
        super("syntax error around " + location(t) + ". " + msg);
    }

    private static String location(Token t) {
        if (t == Token.EOF) {
            return "the last line";
        }

        return "\"" + t.getText() + "\" at line " + t.getLineNumber();
    }

    public ParseException(Exception e) {
        super(e);
    }

    public ParseException(String msg) {
        super(msg);
    }
}
