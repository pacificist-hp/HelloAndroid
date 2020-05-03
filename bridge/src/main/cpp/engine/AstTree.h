//
// Created by Administrator on 2020/4/23.
//

#ifndef HELLOANDROID_ASTTREE_H
#define HELLOANDROID_ASTTREE_H

#include "Token.h"

namespace bridge {

    class AstTree {
    public:
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
    };

    class VarLiteral : public AstLeaf {
    public:
        VarLiteral(TokenPtr token) : AstLeaf(token) {}

    };

    typedef shared_ptr<VarLiteral> VarLiteralPtr;
}

#endif //HELLOANDROID_ASTTREE_H
