//
// Created by Administrator on 2020/5/3.
//

#ifndef HELLOANDROID_EXPRESSION_H
#define HELLOANDROID_EXPRESSION_H

#include "AstTree.h"
#include "Statement.h"

namespace bridge {

    // 一元表达式
    class UnaryExpression : public AstTree {
    public:
        UnaryExpression(AstLeafPtr op, AstTreePtr variable) {
            _op = op;
            _variable = variable;
            _left = true;
        }

        UnaryExpression(AstLeafPtr op, AstTreePtr variable, bool left) {
            _op = op;
            _variable = variable;
            _left = left;
        }

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            StringPtr op = _op->get_token()->get_identifier();
            BridgeValue value;
            if (op != nullptr) {
                if (*op == "-") {
                    value = evaluate_minus_expression(env);
                } else if (*op == "!") {
                    value = evaluate_not_expression(env);
                } else if (*op == "++") {
                    value = evaluate_add_or_minus_self_expression(env, true);
                } else if (*op == "--") {
                    value = evaluate_add_or_minus_self_expression(env, false);
                }
            }

            return value;
        }

        virtual string description() {
            string desc = "(";
            if (_left) {
                desc += _op->description();
                desc += _variable->description();
            } else {
                desc += _variable->description();
                desc += _op->description();
            }
            desc += ")";
            return desc;
        }

    private:
        BridgeValue evaluate_minus_expression(EnvironmentPtr &env) throw(BridgeException) {
            BridgeValue value = _variable->evaluate(env);
            if (value._type == INT) {
                value._int *= -1;
            } else if (value._type == FLOAT) {
                value._float *= -1;
            }

            return value;
        }

        BridgeValue evaluate_not_expression(EnvironmentPtr &env) throw(BridgeException) {
            ConditionStatementPtr condition = make_shared<ConditionStatement>(_variable);
            return BridgeValue(!condition->is_condition_true(env));
        }

        BridgeValue evaluate_add_or_minus_self_expression(EnvironmentPtr &env,
                                                          bool add) throw(BridgeException) {
            BridgeValue v = _variable->evaluate(env);
            if (v._type != INT) {
                throw (BridgeException("only INT supports '++/--'"));
            }

            BridgeValue tmp(add ? (int) (v._int + 1) : (int) (v._int - 1));
            auto var_literal = dynamic_pointer_cast<VarLiteral>(_variable);
            if (var_literal != nullptr) {
                var_literal->assign(env, tmp);
            }

            if (_left) {
                v._int = tmp._int;
            }

            return v;
        }

    protected:
        AstLeafPtr _op;
        AstTreePtr _variable;
        bool _left; // 操作符在表达式的左侧或右侧
    };


    // 二元表达式
    class Expression : public AstTree {
    public:
        Expression(AstTreePtr left, AstLeafPtr op, AstTreePtr right) {
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

    protected:
        AstTreePtr _left;
        AstLeafPtr _op;
        AstTreePtr _right;
    };
}

#endif //HELLOANDROID_EXPRESSION_H
