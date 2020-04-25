//
// Created by Administrator on 2020/4/25.
//

#ifndef HELLOANDROID_LEXER_H
#define HELLOANDROID_LEXER_H

#include <sstream>

#include "../include/common.h"

namespace bridge {

    class bridge_reader {
    public:
        bridge_reader() {
            _line = 0;
            _idx = 0;
            _istream = nullptr;
        }

        ~bridge_reader() {

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

    typedef shared_ptr<bridge_reader> bridge_reader_ptr;
}
#endif //HELLOANDROID_LEXER_H
