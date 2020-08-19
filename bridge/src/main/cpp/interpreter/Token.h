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

        virtual StringPtr get_identifier() {
            return nullptr;
        }

        virtual int get_integer() {
            return 0;
        }

        virtual float get_float() {
            return 0.0f;
        }

        virtual bool get_bool() {
            return false;
        }

        virtual StringPtr get_text() = 0;

        virtual string description() {
            return "";
        }

    private:
        int _line;
    };

    typedef shared_ptr<Token> TokenPtr;

    class IdentifierToken : public Token {
    public:
        IdentifierToken(StringPtr identifier, int line) : Token(line) {
            _identifier = identifier;
        }

        virtual TokenType get_type() {
            return TYPE_IDENTIFIER;
        }

        virtual StringPtr get_identifier() {
            return _identifier;
        }

        virtual StringPtr get_text() {
            return _identifier;
        }

        virtual string description() {
            return *_identifier;
        }

    private:
        StringPtr _identifier;
    };

    class IntToken : public Token {
    public:
        IntToken(int value, int line) : Token(line) {
            _value = value;
        }

        virtual TokenType get_type() {
            return TYPE_INTEGER;
        }

        virtual int get_integer() {
            return _value;
        }

        virtual StringPtr get_text() {
            return make_shared<string>(to_string(_value));
        }

        virtual string description() {
            return to_string(_value);
        }

    private:
        int _value;
    };

    class FloatToken : public Token {
    public:
        FloatToken(float value, int line) : Token(line) {
            _value = value;
        }

        virtual TokenType get_type() {
            return TYPE_FLOAT;
        }

        virtual float get_float() {
            return _value;
        }

        virtual StringPtr get_text() {
            return make_shared<string>(to_string(_value));
        }

        virtual string description() {
            return to_string(_value);
        }

    private:
        float _value;
    };

    class BoolToken : public Token {
    public:
        BoolToken(bool value, int line) : Token(line) {
            _value = value;
        }

        virtual TokenType get_type() {
            return TYPE_BOOL;
        }

        virtual bool get_bool() {
            return _value;
        }

        virtual StringPtr get_text() {
            return make_shared<string>(_value ? "true" : "false");
        }

        virtual string description() {
            return _value ? "true" : "false";
        }

    private:
        bool _value;
    };

    class TextToken : public Token {
    public:
        TextToken(StringPtr text, int line) : Token(line) {
            _text = text;
        }

        virtual TokenType get_type() {
            return TYPE_TEXT;
        }

        virtual StringPtr get_text() {
            return _text;
        }

        virtual string description() {
            string desc = "\"";
            desc += *_text;
            desc += "\"";
            return desc;
        }

    private:
        StringPtr _text;
    };

    class NullToken : public Token {
    public:
        NullToken(int line) : Token(line) {}

        virtual TokenType get_type() {
            return TYPE_NONE;
        }

        virtual StringPtr get_text() {
            return make_shared<string>("null");
        }

        virtual string description() {
            return "null";
        }
    };
}

#endif //HELLOANDROID_TOKEN_H
