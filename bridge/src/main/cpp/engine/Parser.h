//
// Created by Administrator on 2020/4/26.
//

#ifndef HELLOANDROID_PARSER_H
#define HELLOANDROID_PARSER_H

#include <unordered_map>
#include <unordered_set>

#include "../include/common.h"
#include "Lexer.h"
#include "Func.h"

namespace bridge {

    class Parser {
    public:
        Parser(LexerPtr lexer) {
            _lexer = lexer;

            _reserved.insert("{");
            _reserved.insert("}");
            _reserved.insert("[");
            _reserved.insert("]");
            _reserved.insert("(");
            _reserved.insert(")");
            _reserved.insert(";");
            _reserved.insert("function");
            _reserved.insert("var");
        }

    public:
        void register_func(string name, func_def_ptr func_def) {
            _map_func_def[name] = func_def;
        }
    private:
        LexerPtr _lexer;
        unordered_set<string> _reserved;

        unordered_map<string, func_def_ptr> _map_func_def;
    };

    typedef shared_ptr<Parser> ParserPtr;
}

#endif //HELLOANDROID_PARSER_H
