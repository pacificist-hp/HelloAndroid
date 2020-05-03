//
// Created by Administrator on 2020/4/23.
//

#ifndef HELLOANDROID_ASTTREE_H
#define HELLOANDROID_ASTTREE_H

#include "Environment.h"
#include "Token.h"

namespace bridge {

    class AstTree {
    public:
        virtual bridge_value evaluate(EnvironmentPtr &env) throw(bridge_exception) {
            throw bridge_exception("AstTree cannot evaluate");
        }

        virtual string description() = 0;
    };

    typedef shared_ptr<AstTree> AstTreePtr;
    typedef shared_ptr<vector<AstTreePtr>> AstTreeVecPtr;

    class AstLeaf : public AstTree {
    public:
        AstLeaf(TokenPtr token) {
            _token = token;
        }

        TokenPtr get_token() {
            return _token;
        }

        string_ptr try_get_identifier() {
            return _token == nullptr ? nullptr : _token->get_identifier();
        }

        virtual string description() {
            return _token->description();
        }

    private:
        TokenPtr _token;
    };

    typedef shared_ptr<AstLeaf> AstLeafPtr;
    typedef shared_ptr<vector<AstLeafPtr>> AstLeafVecPtr;

    class TextLiteral : public AstLeaf {
    public:
        TextLiteral(TokenPtr token) : AstLeaf(token) {}

        virtual bridge_value evaluate(EnvironmentPtr &env) throw(bridge_exception) {
            bridge_value v;
            v._type = STRING;
            v._string = *get_value();

            return v;
        }

    private:
        string_ptr get_value() throw(bridge_exception) {
            TokenPtr t = get_token();
            if (t != nullptr && t->get_type() == TYPE_TEXT) {
                return t->get_text();
            }

            throw bridge_exception("TextLiteral::get_value error");
        }
    };

    class VarLiteral : public AstLeaf {
    public:
        VarLiteral(TokenPtr token) : AstLeaf(token) {}

    };

    typedef shared_ptr<VarLiteral> VarLiteralPtr;
}

#endif //HELLOANDROID_ASTTREE_H
