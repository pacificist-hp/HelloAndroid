package com.android.pacificist.stone;

import com.android.pacificist.stone.engine.Lexer;
import com.android.pacificist.stone.engine.exception.ParseException;
import com.android.pacificist.stone.engine.token.Token;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Stone {

    public static void main(String[] args) {

        try {
            Lexer lexer = new Lexer(new FileReader(System.getProperty("user.dir") + "/stone/stone.js"));
            for (Token t; (t = lexer.read()) != Token.EOF; ) {
                System.out.println("=> " + t.getText());
            }
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }

    }
}
