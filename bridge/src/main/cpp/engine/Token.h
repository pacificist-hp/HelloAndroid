//
// Created by Administrator on 2020/4/23.
//

#ifndef HELLOANDROID_TOKEN_H
#define HELLOANDROID_TOKEN_H

#include "../include/common.h"

namespace bridge {

    enum TokenType {
        TYPE_IDENTIFIER = 0,
        TYPE_INTEGER,
        TYPE_FLOAT,
        TYPE_TEXT,
        TYPE_BOOL,
        TYPE_JSON,
        TYPE_NONE
    };

    class Token {
    public:
        Token(int line) {
            _line = line;
        }

        int get_line() {
            return _line;
        }

        virtual TokenType get_type() = 0;

        virtual string_ptr get_identifier() {
            return nullptr;
        }

        virtual string_ptr get_text() = 0;

        virtual string description() {
            return "";
        }

    private:
        int _line;
    };

    typedef shared_ptr<Token> TokenPtr;

    class IdentifierToken : public Token {
    public:
        IdentifierToken(string_ptr identifier, int line) : Token(line) {
            _identifier = identifier;
        }

        virtual string_ptr get_identifier() {
            return _identifier;
        }

        virtual TokenType get_type() {
            return TYPE_IDENTIFIER;
        }

        virtual string_ptr get_text() {
            return _identifier;
        }

        virtual string description() {
            return *_identifier;
        }

    private:
        string_ptr _identifier;
    };

    class TextToken : public Token {
    public:
        TextToken(string_ptr text, int line) : Token(line) {
            _text = text;
        }

        virtual TokenType get_type() {
            return TYPE_TEXT;
        }

        virtual string_ptr get_text() {
            return _text;
        }

        virtual string description() {
            string desc = "\"";
            desc += *_text;
            desc += "\"";
            return desc;
        }

    private:
        string_ptr _text;
    };

}
#endif //HELLOANDROID_TOKEN_H
