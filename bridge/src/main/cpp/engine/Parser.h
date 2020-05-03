//
// Created by Administrator on 2020/4/26.
//

#ifndef HELLOANDROID_PARSER_H
#define HELLOANDROID_PARSER_H

#include <unordered_map>
#include <unordered_set>

#include "../include/common.h"
#include "AstTree.h"
#include "Expression.h"
#include "Func.h"
#include "Lexer.h"
#include "Statement.h"

namespace bridge {

    struct Precedence {
        Precedence(int val, bool left_assoc) {
            _value = val;
            _left_assoc = left_assoc;
        }

        int _value;
        bool _left_assoc;
    };

    typedef shared_ptr<Precedence> PrecedencePtr;

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

        AstTreePtr expression() throw(bridge_exception) {
            AstTreePtr left = factor();
            if (left == nullptr) {
                if (_lexer->read() != nullptr) {
                    throw bridge_exception("parse error");
                }
                return nullptr;
            }

            PrecedencePtr next_prec = nullptr;

            while ((next_prec = next_op()) != nullptr) {
                left = shift_factor(left, next_prec->_value);
            }

            // try to parse ternary expression

            if (peek_next_token(";")) {
                discard_token(";");
            }

            LOGD("Parser::expression: %s", left == nullptr ? "null" : left->description().c_str());
            return left;
        }

    private:
        AstTreePtr factor() throw(bridge_exception) {
            AstTreePtr ptr = nullptr;

            string_ptr token = peek_token_text();

            if (token != nullptr) {
                if (*token == "(") {
                    ptr = parse_bracket();
                } else if (*token == "var") {

                } else if (*token == "import") {

                } else if (*token == "export") {

                } else if (*token == "!" || *token == "-" || *token == "++" || *token == "--") {

                } else if (*token == "if") {

                } else if (*token == "for") {

                } else if (*token == "while") {

                } else if (*token == "do") {

                } else if (*token == "switch") {

                } else if (*token == "function") {
                    ptr = parse_function();
                } else if (*token == "return") {

                } else if (*token == "break") {

                } else if (*token == "continue") {

                } else if (*token == "{" || *token == "[") {

                } else if (*token == ";") {
                    discard_token(";");
                    ptr = make_shared<EmptyStatement>();
                } else {
                    ptr = parse_literal();
                }
            }

            LOGD("Parser::shift_factor: %s", ptr == nullptr ? "null" : ptr->description().c_str());
            return ptr;
        }

        AstTreePtr shift_factor(AstTreePtr left, int prec) throw(bridge_exception) {
            AstLeafPtr op = make_shared<AstLeaf>(_lexer->read());
            AstTreePtr right = nullptr;

            right = factor();

            if (right == nullptr) {
                string err = "syntax: ";
                TokenPtr t = _lexer->last();
                if (t != nullptr) {
                    err += to_string(t->get_line());
                }

                throw bridge_exception(err);
            }

            // ternary expression
            string_ptr str_op = op->try_get_identifier();
            if (op != nullptr) {

            }

            LOGD("Parser::shift_factor: left=%s, op=%s, right=%s",
                 left == nullptr ? "null" : left->description().c_str(),
                 op == nullptr ? "null" : op->description().c_str(),
                 right == nullptr ? "null" : right->description().c_str());

            return make_shared<BinaryExpression>(left, op, right);
        }

        AstTreePtr parse_bracket() throw(bridge_exception) {
            discard_token("(");

            AstTreePtr expr = nullptr;

            AstLeafVecPtr vec_leaf = make_shared<vector<AstLeafPtr>>();

            if (!peek_next_token(")")) {
                expr = expression();

                VarLiteralPtr var_literal = dynamic_pointer_cast<VarLiteral>(expr);
                while (var_literal != nullptr) {
                    vec_leaf->push_back(var_literal);

                    if (peek_next_token(",")) {

                    } else {
                        break;
                    }
                }
            }

            discard_token(")");

            if (peek_next_token("=")) {

            }

            LOGD("Parser::parse_bracket: %s",
                 expr == nullptr ? "null" : expr->description().c_str());
            return expr;
        }

        AstTreePtr parse_function() throw(bridge_exception) {
            discard_token("function");

            AstLeafPtr name_ptr = make_shared<AstLeaf>(_lexer->read());
            if (name_ptr == nullptr) {
                throw bridge_exception("syntax: function name");
            }

            TokenPtr token_func_name = name_ptr->get_token();
            if (token_func_name == nullptr || token_func_name->get_type() != TYPE_IDENTIFIER) {
                throw bridge_exception("syntax: function name");
            }

            string_ptr str_ptr = token_func_name->get_identifier();
            if (str_ptr == nullptr) {
                throw bridge_exception("syntax: function name");
            }

            func_param_list_ptr params = parse_func_params();
            AstTreePtr block = parse_block();

            func_def_ptr func = make_shared<func_def>(name_ptr, params, block);
            _map_func_def[*str_ptr] = func;

            auto it = _map_vec_undef_func_call.find(*str_ptr);
            if (it != _map_vec_undef_func_call.end()) {
                auto vec = it->second;
                for (auto vec_it = vec->begin(); vec_it != vec->end(); vec_it++) {
                    (*vec_it)->set_func_def(func);
                }

                _map_vec_undef_func_call.erase(it);
            }

            LOGD("Parser::parse_function: %s",
                 func == nullptr ? "null" : func->description().c_str());
            return func;
        }

        AstTreePtr parse_literal() throw(bridge_exception) {
            TokenPtr token = _lexer->read();
            AstTreePtr ptr = nullptr;
            if (token != nullptr) {
                switch (token->get_type()) {
                    case TYPE_TEXT:
                        ptr = make_shared<TextLiteral>(token);
                        break;
                    case TYPE_IDENTIFIER:
                        ptr = parse_identifier(token);
                        break;
                    default:
                        throw bridge_exception("syntax: wrong token type");
                }
            }

            LOGD("Parser::parse_literal: %s", ptr == nullptr ? "null" : ptr->description().c_str());
            return ptr;
        }

        AstTreePtr parse_identifier(TokenPtr t) throw(bridge_exception) {
            AstTreePtr ret = nullptr;

            string_ptr id_ptr = t->get_identifier();

            if (id_ptr != nullptr && _reserved.find(*id_ptr) == _reserved.end()) {
                AstLeafPtr ptr = make_shared<VarLiteral>(t);

                string_ptr next_token = peek_token_text(0);
                if (next_token != nullptr) {
                    if (*next_token == "(") {
                        ret = parse_call(ptr);
                    } else if (*next_token == "." || *next_token == "[") {

                    } else if (*next_token == "++" || *next_token == "--") {

                    }
                }
            }

            LOGD("Parser::parse_identifier: %s",
                 ret == nullptr ? "null" : ret->description().c_str());
            return ret;
        }

        AstTreePtr parse_call(AstLeafPtr func_name) {
            CallStatementPtr ptr = nullptr;

            string_ptr name = func_name->try_get_identifier();

            if (name != nullptr) {
                ArgsListPtr args = parse_func_args();
                func_def_ptr func_def = nullptr;

                auto it = _map_func_def.find(*name);
                if (it != _map_func_def.end()) {
                    func_def = it->second;
                }

                ptr = make_shared<CallStatement>(func_name, args, func_def);
                if (func_def != nullptr) {
                    shared_ptr<vector<CallStatementPtr>> vec_ptr = nullptr;
                    auto it = _map_vec_undef_func_call.find(*name);
                    if (it == _map_vec_undef_func_call.end()) {
                        vec_ptr = make_shared<vector<CallStatementPtr>>();
                        _map_vec_undef_func_call[*name] = vec_ptr;
                    } else {
                        vec_ptr = it->second;
                    }

                    vec_ptr->push_back(ptr);
                }
            }

            LOGD("Parser::parse_call: %s", ptr == nullptr ? "null" : ptr->description().c_str());
            return ptr;
        }

        AstTreePtr parse_block() throw(bridge_exception) {
            if (peek_next_token("{")) {
                discard_token("{");

                AstTreeVecPtr vec = make_shared<vector<AstTreePtr>>();

                string_ptr token = peek_token_text();
                while (token != nullptr && *token != "}") {
                    vec->push_back(expression());
                    token = peek_token_text();
                }

                discard_token("}");

                return make_shared<BlockStatement>(vec);
            }

            return nullptr;
        }

        func_param_list_ptr parse_func_params() throw(bridge_exception) {
            AstLeafVecPtr vec = make_shared<vector<AstLeafPtr>>();
            if (peek_next_token("(")) {
                discard_token("(");

                if (peek_next_token(")")) {
                    discard_token(")");
                } else {
                    AstLeafPtr param = make_shared<AstLeaf>(_lexer->read());

                    while (param != nullptr) {
                        TokenPtr t = param->get_token();
                        if (t != nullptr && t->get_type() == TYPE_IDENTIFIER) {
                            vec->push_back(param);

                            if (peek_next_token(",")) {
                                discard_token(",");
                                param = make_shared<AstLeaf>(_lexer->read());
                            } else if (peek_next_token(")")) {
                                discard_token(")");
                                break;
                            } else {
                                throw bridge_exception("syntax: function params");
                            }
                        } else {
                            throw bridge_exception("syntax: function params");
                        }
                    }
                }
            } else {
                throw bridge_exception("syntax: function params");
            }

            return make_shared<func_param_list>(vec);
        }

        ArgsListPtr parse_func_args() throw(bridge_exception) {
            AstTreeVecPtr vec = make_shared<vector<AstTreePtr>>();
            if (peek_next_token("(")) {
                discard_token("(");

                if (peek_next_token(")")) {
                    discard_token(")");
                } else {
                    AstTreePtr arg = expression();
                    while (arg != nullptr) {
                        if (peek_next_token(",")) {
                            discard_token(",");
                            arg = expression();
                        } else if (peek_next_token(")")) {
                            discard_token(")");
                            break;
                        } else {
                            throw bridge_exception("syntax: func args )");
                        }
                    }
                }
            } else {
                throw bridge_exception("syntax: func args have no ()");
            }

            return make_shared<ArgsList>(vec);
        }

        PrecedencePtr next_op() throw(bridge_exception) {
            TokenPtr t = _lexer->peek(0);
            if (t != nullptr && t->get_type() == TYPE_IDENTIFIER) {
                auto it = _operators.find(*(t->get_identifier()));
                if (it != _operators.end()) {
                    LOGD("Parser::next_op: %s", t->description().c_str());
                    return it->second;
                }
            }

            LOGD("Parser::next_op: null");
            return nullptr;
        }

        void discard_token(string name) {
            TokenPtr token = _lexer->read();
            if (token == nullptr || token->get_text() == nullptr || *(token->get_text()) != name) {
                throw bridge_exception("discard token error");
            }
        }

        bool peek_next_token(string name) throw(bridge_exception) {
            TokenPtr token = _lexer->peek(0);
            return token != nullptr && token->get_text() != nullptr && *(token->get_text()) == name;
        }

        string_ptr peek_token_text(int step = 0) throw(bridge_exception) {
            TokenPtr token = _lexer->peek(step);
            return token == nullptr ? nullptr : token->get_text();
        }

    private:
        LexerPtr _lexer;
        unordered_map<string, PrecedencePtr> _operators;
        unordered_set <string> _reserved;

        unordered_map<string, func_def_ptr> _map_func_def;
        unordered_map<string, shared_ptr<vector<CallStatementPtr>>> _map_vec_undef_func_call;
    };

    typedef shared_ptr<Parser> ParserPtr;
}

#endif //HELLOANDROID_PARSER_H
