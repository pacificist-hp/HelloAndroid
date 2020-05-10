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
        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            throw BridgeException("AstTree cannot evaluate");
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

        StringPtr try_get_identifier() {
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

    class AstList : public AstTree {
    public:
        AstList(AstTreeVecPtr children) {
            if (children == nullptr) {
                throw BridgeException("AstList::AstList error");
            }

            _children = children;
        }

        int children_count() {
            return int(_children->size());
        }

        AstTreePtr child(int i) throw(BridgeException) {
            if (i >= 0 && i < _children->size()) {
                return _children->at(i);
            }

            throw BridgeException("AstList::child error");
        }

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            BridgeValue v;
            for (auto it = _children->begin(); it != _children->end(); it++) {
                v = (*it)->evaluate(env);
            }

            return v;
        }

        virtual string description() {
            string dest;
            for (auto it = _children->begin(); it != _children->end(); it++) {
                AstTreePtr ptr = *it;
                if (it != _children->begin()) {
                    dest += ",";
                }
                dest += ptr->description();
            }
            return dest;
        }

    protected:
        AstTreeVecPtr _children;
    };

    class TextLiteral : public AstLeaf {
    public:
        TextLiteral(TokenPtr token) : AstLeaf(token) {}

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            BridgeValue v;
            v._type = STRING;
            v._string = *get_value();

            return v;
        }

    private:
        StringPtr get_value() throw(BridgeException) {
            TokenPtr t = get_token();
            if (t != nullptr && t->get_type() == TYPE_TEXT) {
                return t->get_text();
            }

            throw BridgeException("TextLiteral::get_value error");
        }
    };

    class VarLiteral : public AstLeaf {
    public:
        VarLiteral(TokenPtr token) : AstLeaf(token) {}
    };

    typedef shared_ptr<VarLiteral> VarLiteralPtr;
}

#endif //HELLOANDROID_ASTTREE_H
