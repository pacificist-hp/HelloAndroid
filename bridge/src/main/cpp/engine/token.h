//
// Created by Administrator on 2020/4/23.
//

#ifndef HELLOANDROID_TOKEN_H
#define HELLOANDROID_TOKEN_H

#include "../include/common.h"

namespace bridge {

    enum toke_type {
        TYPE_IDENTIFIER = 0,
        TYPE_INTEGER,
        TYPE_FLOAT,
        TYPE_TEXT,
        TYPE_BOOL,
        TYPE_JSON,
        TYPE_NONE
    };

    class token {
    public:
        token(int line) {
            _line = line;
        }

    private:
        int _line;
    };

    class identifier_token : public token {
    public:
        identifier_token(string_ptr identifier, int line) : token(line) {
            _identifier = identifier;
        }

    private:
        string_ptr _identifier;
    };

    typedef shared_ptr<token> token_ptr;
}
#endif //HELLOANDROID_TOKEN_H
