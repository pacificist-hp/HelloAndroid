//
// Created by Administrator on 2020/5/2.
//

#ifndef HELLOANDROID_STATEMENT_H
#define HELLOANDROID_STATEMENT_H

#include "AstTree.h"

namespace bridge {
    class EmptyStatement : public AstTree {
    public:
        EmptyStatement() {}

        virtual string description() {
            return "{empty}";
        }
    };

    class BlockStatement : public AstList {
    public:
        BlockStatement(AstTreeVecPtr children) : AstList(children) {}
    };
}

#endif //HELLOANDROID_STATEMENT_H
