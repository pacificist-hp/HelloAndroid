//
// Created by Administrator on 2020/5/3.
//

#ifndef HELLOANDROID_EXPRESSION_H
#define HELLOANDROID_EXPRESSION_H

#include "AstTree.h"

namespace bridge {
    class BinaryExpression : public AstTree {
    public:
        BinaryExpression(AstTreePtr left, AstLeafPtr op, AstTreePtr right) {
            _left = left;
            _op = op;
            _right = right;
        }

        virtual string description() {
            string desc = "(";
            desc += _left->description();
            desc += _op->description();
            desc += _right->description();
            return desc;
        }

    private:
        AstTreePtr _left;
        AstLeafPtr _op;
        AstTreePtr _right;
    };
}

#endif //HELLOANDROID_EXPRESSION_H
