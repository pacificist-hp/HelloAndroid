//
// Created by Administrator on 2020/4/25.
//

#ifndef HELLOANDROID_LEXER_H
#define HELLOANDROID_LEXER_H

#include <sstream>

#include "../include/common.h"

namespace bridge {

    struct Character {
        Character(char c, int line) {
            _c = c;
            _line = line;
        }

        char _c;
        int _line;
    };

    typedef shared_ptr<Character> CharPtr;

    class Reader {
    public:
        Reader() {
            _line = 0;
            _idx = 0;
            _istream = nullptr;
        }

        ~Reader() {

        }

        void set_code(const char *code) {
            _line = 0;
            _idx = 0;
            _istream = make_shared<istringstream>(code);
        }
    private:
        int _idx;
        int _line;

        string _str;

        shared_ptr<istream> _istream;
    };

    typedef shared_ptr<Reader> ReaderPtr;

    class Lexer {
    public:
        Lexer() {

        }

        ~Lexer() {

        }

    public:
        void set_reader(ReaderPtr reader) {
            _reader = reader;
            _current_token = nullptr;
        }

    private:
        ReaderPtr _reader;
        TokenPtr _current_token;
    };

    typedef shared_ptr<Lexer> LexerPtr;
}
#endif //HELLOANDROID_LEXER_H
