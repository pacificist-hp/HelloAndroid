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
#include "Function.h"
#include "Lexer.h"
#include "Literal.h"
#include "Statement.h"

namespace bridge {

    // 运算符优先级
    struct Precedence {
        Precedence(int level, bool left) {
            _level = level;
            _left_associative = left;
        }

        // 值越大，优先级越高
        int _level;
        // 运算符是否左结合
        bool _left_associative;
    };

    typedef shared_ptr<Precedence> PrecedencePtr;

    class Parser {
    public:
        Parser(LexerPtr lexer) {
            _lexer = lexer;

            _operators["&&"] = make_shared<Precedence>(0, true);
            _operators["||"] = make_shared<Precedence>(0, true);
            _operators["="] = make_shared<Precedence>(0, true);
            _operators["<"] = make_shared<Precedence>(1, true);
            _operators[">"] = make_shared<Precedence>(1, true);
            _operators["<="] = make_shared<Precedence>(1, true);
            _operators[">="] = make_shared<Precedence>(1, true);
            _operators["=="] = make_shared<Precedence>(1, true);
            _operators["!="] = make_shared<Precedence>(1, true);
            _operators["+="] = make_shared<Precedence>(1, true);
            _operators["-="] = make_shared<Precedence>(1, true);
            _operators["*="] = make_shared<Precedence>(1, true);
            _operators["/="] = make_shared<Precedence>(1, true);
            _operators["+"] = make_shared<Precedence>(2, true);
            _operators["-"] = make_shared<Precedence>(2, true);
            _operators["*"] = make_shared<Precedence>(3, true);
            _operators["/"] = make_shared<Precedence>(3, true);
            _operators["%"] = make_shared<Precedence>(3, true);
            _operators["^"] = make_shared<Precedence>(4, true);
            _operators["!"] = make_shared<Precedence>(4, true);

            _reserved.insert("{");
            _reserved.insert("}");
            _reserved.insert("[");
            _reserved.insert("]");
            _reserved.insert("(");
            _reserved.insert(")");
            _reserved.insert(";");
            _reserved.insert("function");
            _reserved.insert("var");

            _op_bool.insert("&&");
            _op_bool.insert("||");
            _op_bool.insert(">");
            _op_bool.insert("<");
            _op_bool.insert("==");
            _op_bool.insert(">=");
            _op_bool.insert("<=");
            _op_bool.insert("!=");
        }

    public:
        void register_func(string name, FuncPtr func_def) {
            _map_func_def[name] = func_def;
        }

        AstTreePtr expression() throw(BridgeException) {
            AstTreePtr left = factor();
            if (left == nullptr) {
                if (_lexer->read() != nullptr) {
                    throw BridgeException("parse error");
                }
                return nullptr;
            }

            PrecedencePtr precedence = nullptr;

            while ((precedence = next_operator_precedence()) != nullptr) {
                left = shift_factor(left, precedence->_level);
            }

            // try to parse ternary expression

            if (peek_next_token(";")) {
                discard_token(";");
            }

            LOGD("Parser::expression: %s", left == nullptr ? "null" : left->description().c_str());
            return left;
        }

    private:
        AstTreePtr factor() throw(BridgeException) {
            AstTreePtr ptr = nullptr;

            StringPtr token = peek_token_text();

            LOGD("Parser::factor start: %s", token == nullptr ? "null" : token->c_str());

            if (token != nullptr) {
                if (*token == "(") {
                    ptr = parse_bracket();
                } else if (*token == "var") {
                    ptr = parse_var();
                } else if (*token == "import") {

                } else if (*token == "export") {

                } else if (*token == "!" || *token == "-" || *token == "++" || *token == "--") {
                    ptr = parse_unary_expression(token);
                } else if (*token == "if") {
                    ptr = parse_if();
                } else if (*token == "for") {

                } else if (*token == "while") {
                    ptr = parse_while();
                } else if (*token == "do") {
                    ptr = parse_do_while();
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

            LOGD("Parser::factor: %s", ptr == nullptr ? "null" : ptr->description().c_str());
            return ptr;
        }

        AstTreePtr shift_factor(AstTreePtr left, int level) throw(BridgeException) {
            AstLeafPtr op = make_shared<AstLeaf>(_lexer->read());
            AstTreePtr right = nullptr;

            right = factor();

            if (right == nullptr) {
                string err = "syntax: ";
                TokenPtr t = _lexer->last();
                if (t != nullptr) {
                    err += to_string(t->get_line());
                }

                throw BridgeException(err);
            }

            PrecedencePtr next_precedence = nullptr;
            while ((next_precedence = next_operator_precedence()) != nullptr
                    && check_expression(level, next_precedence)) {
                right = shift_factor(right, next_precedence->_level);
            }

            // ternary expression
            StringPtr str_op = op->try_get_identifier();
            if (op != nullptr) {

            }

            LOGD("Parser::shift_factor: left=%s, op=%s, right=%s",
                 left == nullptr ? "null" : left->description().c_str(),
                 op == nullptr ? "null" : op->description().c_str(),
                 right == nullptr ? "null" : right->description().c_str());

            return make_shared<BinaryExpression>(left, op, right);
        }

        bool check_expression(int level, PrecedencePtr next_precedence) {
            return next_precedence->_left_associative ?
                level < next_precedence->_level : level <= next_precedence->_level;
        }

        AstTreePtr parse_bracket() throw(BridgeException) {
            discard_token("(");

            AstTreePtr ptr = nullptr;
            AstLeafVecPtr vec_leaf = make_shared<vector<AstLeafPtr>>();
            if (!peek_next_token(")")) {
                ptr = expression();


            }

            discard_token(")");

            if (peek_next_token("=")) {

            }

            LOGD("Parser::parse_bracket: %s", ptr == nullptr ? "null" : ptr->description().c_str());
            return ptr;
        }

        AstTreePtr parse_var() throw(BridgeException) {
            discard_token("var");

            TokenPtr name_token = _lexer->read();
            if (name_token == nullptr) {
                throw BridgeException("syntax: var miss identifier");
            }

            StringPtr name = name_token->get_identifier();
            if (name == nullptr) {
                throw BridgeException("syntax: var miss identifier");
            }

            if (_reserved.find(*name) != _reserved.end()) {
                throw BridgeException("syntax: var miss identifier");
            }

            VarLiteralPtr var_literal = make_shared<VarLiteral>(name_token);
            AstTreePtr value = nullptr;
            if (peek_next_token("=")) {
                discard_token("=");
                value = expression();
            }

            AstTreePtr var = make_shared<VarStatement>(var_literal, value);
            LOGD("Parser::parse_var: %s", var == nullptr ? "null" : var->description().c_str());
            return var;
        }

        AstTreePtr parse_unary_expression(StringPtr token) throw(BridgeException) {
            AstLeafPtr op = make_shared<AstLeaf>(_lexer->read());
            AstTreePtr variable = factor();

            AstTreePtr unary = make_shared<UnaryExpression>(op, variable);
            LOGD("Parser::parse_unary_expression: %s",
                 unary == nullptr ? "null" : unary->description().c_str());
            return unary;
        }

        AstTreePtr parse_if() throw(BridgeException) {
            discard_token("if");
            AstTreePtr condition = factor();
            AstTreePtr then_block = parse_block();
            AstTreePtr else_block = nullptr;

            if (peek_next_token("else")) {
                discard_token("else");

                if (peek_next_token("if")) {
                    else_block = parse_if();
                } else {
                    else_block = parse_block();
                }
            }

            AstTreePtr ret = make_shared<IfStatement>(condition, then_block, else_block);
            LOGD("Parser::parse_if: %s", ret == nullptr ? "null" : ret->description().c_str());
            return ret;
        }

        AstTreePtr parse_while() throw(BridgeException) {
            discard_token("while");

            AstTreePtr condition = factor();
            AstTreePtr block = parse_block();

            AstTreePtr ret = make_shared<WhileStatement>(condition, block);
            LOGD("Parser::parse_while: %s", ret == nullptr ? "null" : ret->description().c_str());
            return ret;
        }

        AstTreePtr parse_do_while() throw(BridgeException) {
            discard_token("do");
            AstTreePtr block = parse_block();

            discard_token("while");
            AstTreePtr condition = factor();

            AstTreePtr ret = make_shared<DoWhileStatement>(condition, block);
            LOGD("Parser::parse_do_while: %s",
                 ret == nullptr ? "null" : ret->description().c_str());
            return ret;
        }

        AstTreePtr parse_function() throw(BridgeException) {
            discard_token("function");

            AstLeafPtr name_ptr = make_shared<AstLeaf>(_lexer->read());
            if (name_ptr == nullptr) {
                throw BridgeException("syntax: function name");
            }

            TokenPtr token_func_name = name_ptr->get_token();
            if (token_func_name == nullptr || token_func_name->get_type() != TYPE_IDENTIFIER) {
                throw BridgeException("syntax: function name");
            }

            StringPtr str_ptr = token_func_name->get_identifier();
            if (str_ptr == nullptr) {
                throw BridgeException("syntax: function name");
            }

            FuncParamListPtr params = parse_func_params();
            AstTreePtr block = parse_block();

            FuncPtr func = make_shared<Function>(name_ptr, params, block);
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

        AstTreePtr parse_literal() throw(BridgeException) {
            TokenPtr token = _lexer->read();
            AstTreePtr literal = nullptr;
            if (token != nullptr) {
                switch (token->get_type()) {
                    case TYPE_INTEGER:
                        literal = make_shared<IntLiteral>(token);
                        break;
                    case TYPE_FLOAT:
                        literal = make_shared<FloatLiteral>(token);
                        break;
                    case TYPE_BOOL:
                        literal = make_shared<BoolLiteral>(token);
                        break;
                    case TYPE_TEXT:
                        literal = make_shared<TextLiteral>(token);
                        break;
                    case TYPE_IDENTIFIER:
                        literal = parse_identifier(token);
                        break;
                    case TYPE_NONE:
                        literal = make_shared<NullLiteral>(token);
                        break;
                    default:
                        throw BridgeException("syntax: wrong token type");
                }
            }

            LOGD("Parser::parse_literal: %s",
                 literal == nullptr ? "null" : literal->description().c_str());
            return literal;
        }

        AstTreePtr parse_identifier(TokenPtr t) throw(BridgeException) {
            AstTreePtr ret = nullptr;

            StringPtr id_ptr = t->get_identifier();

            if (id_ptr != nullptr && _reserved.find(*id_ptr) == _reserved.end()) {
                AstLeafPtr ptr = make_shared<VarLiteral>(t);

                StringPtr next_token = peek_token_text(0);
                if (next_token != nullptr) {
                    if (*next_token == "(") {
                        ret = parse_call(ptr);
                    } else if (*next_token == "." || *next_token == "[") {

                    } else if (*next_token == "++" || *next_token == "--") {

                    }
                }

                if (ret == nullptr) {
                    ret = ptr;
                }
            }

            LOGD("Parser::parse_identifier: %s",
                 ret == nullptr ? "null" : ret->description().c_str());
            return ret;
        }

        AstTreePtr parse_call(AstLeafPtr func_name) {
            CallStatementPtr call = nullptr;

            StringPtr name = func_name->try_get_identifier();

            if (name != nullptr) {
                ArgsListPtr args = parse_func_args();
                FuncPtr func_def = nullptr;

                auto it = _map_func_def.find(*name);
                if (it != _map_func_def.end()) {
                    func_def = it->second;
                }

                call = make_shared<CallStatement>(func_name, args, func_def);
                if (func_def == nullptr) {
                    shared_ptr<vector<CallStatementPtr>> vec_ptr = nullptr;
                    auto it = _map_vec_undef_func_call.find(*name);
                    if (it == _map_vec_undef_func_call.end()) {
                        vec_ptr = make_shared<vector<CallStatementPtr>>();
                        _map_vec_undef_func_call[*name] = vec_ptr;
                    } else {
                        vec_ptr = it->second;
                    }

                    vec_ptr->push_back(call);
                }
            }

            LOGD("Parser::parse_call: %s", call == nullptr ? "null" : call->description().c_str());
            return call;
        }

        AstTreePtr parse_block() throw(BridgeException) {
            AstTreePtr block = nullptr;

            if (peek_next_token("{")) {
                discard_token("{");

                AstTreeVecPtr vec = make_shared<vector<AstTreePtr>>();

                StringPtr token = peek_token_text();
                while (token != nullptr && *token != "}") {
                    vec->push_back(expression());
                    token = peek_token_text();
                }

                discard_token("}");

                block = make_shared<BlockStatement>(vec);
            }

            LOGD("Parser::parse_block: %s",
                 block == nullptr ? "null" : block->description().c_str());
            return block;
        }

        FuncParamListPtr parse_func_params() throw(BridgeException) {
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
                                throw BridgeException("syntax: function params");
                            }
                        } else {
                            throw BridgeException("syntax: function params");
                        }
                    }
                }
            } else {
                throw BridgeException("syntax: function params");
            }

            FuncParamListPtr ret = make_shared<FuncParamList>(vec);
            LOGD("Parser::parse_func_params: %s",
                 ret == nullptr ? "null" : ret->description().c_str());
            return make_shared<FuncParamList>(vec);
        }

        ArgsListPtr parse_func_args() throw(BridgeException) {
            AstTreeVecPtr vec = make_shared<vector<AstTreePtr>>();
            if (peek_next_token("(")) {
                discard_token("(");

                if (peek_next_token(")")) {
                    discard_token(")");
                } else {
                    AstTreePtr arg = expression();
                    while (arg != nullptr) {
                        vec->push_back(arg);

                        if (peek_next_token(",")) {
                            discard_token(",");
                            arg = expression();
                        } else if (peek_next_token(")")) {
                            discard_token(")");
                            break;
                        } else {
                            throw BridgeException("syntax: func args )");
                        }
                    }
                }
            } else {
                throw BridgeException("syntax: func args have no ()");
            }

            ArgsListPtr ret = make_shared<ArgsList>(vec);
            LOGD("Parser::parse_func_args: %s",
                 ret == nullptr ? "null" : ret->description().c_str());
            return ret;
        }

        PrecedencePtr next_operator_precedence() throw(BridgeException) {
            TokenPtr t = _lexer->peek(0);
            if (t != nullptr && t->get_type() == TYPE_IDENTIFIER) {
                auto it = _operators.find(*(t->get_identifier()));
                if (it != _operators.end()) {
                    LOGD("Parser::next_operator_precedence: %s", t->description().c_str());
                    return it->second;
                }
            }

            LOGD("Parser::next_operator_precedence: null");
            return nullptr;
        }

        void discard_token(string name) {
            TokenPtr token = _lexer->read();
            if (token == nullptr || token->get_text() == nullptr || *(token->get_text()) != name) {
                string msg = "discard token \"";
                msg += name;
                msg += "\" error";
                throw BridgeException(msg);
            }
        }

        bool peek_next_token(string name) throw(BridgeException) {
            TokenPtr token = _lexer->peek(0);
            return token != nullptr && token->get_text() != nullptr && *(token->get_text()) == name;
        }

        StringPtr peek_token_text(int step = 0) throw(BridgeException) {
            TokenPtr token = _lexer->peek(step);
            return token == nullptr ? nullptr : token->get_text();
        }

    private:
        LexerPtr _lexer;
        unordered_map<string, PrecedencePtr> _operators;
        unordered_set<string> _reserved;
        unordered_set<string> _op_bool;

        // 保存已注册或定义好的函数
        unordered_map<string, FuncPtr> _map_func_def;
        // 语法解析时，函数调用在函数定义之前，保存暂未定义的函数
        unordered_map<string, shared_ptr<vector<CallStatementPtr>>> _map_vec_undef_func_call;
    };

    typedef shared_ptr<Parser> ParserPtr;
}

#endif //HELLOANDROID_PARSER_H
