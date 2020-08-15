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
    class BinaryExpression : public AstTree {
    public:
        BinaryExpression(AstTreePtr left, AstLeafPtr op, AstTreePtr right) {
            _left = left;
            _op = op;
            _right = right;
        }

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            if (_op == nullptr || _op->get_token() == nullptr) {
                throw BridgeException("Expression error: op is null");
            }

            StringPtr op = _op->get_token()->get_identifier();

            BridgeValue right_value = _right->evaluate(env);
            if (*op == "=") {
                auto left_var = dynamic_pointer_cast<VarLiteral>(_left);
                if (left_var != nullptr) {
                    left_var->assign(env, right_value);
                    return right_value;
                } else {
                    throw BridgeException("Expression error: only var can be assigned");
                }
            }

            BridgeValue left_value = _left->evaluate(env);
            if (*op == "+") {
                op_add(left_value, right_value);
            } else if (*op == "-") {
                op_sub(left_value, right_value);
            } else if (*op == "*") {
                op_mul(left_value, right_value);
            } else if (*op == "/") {
                op_div(left_value, right_value);
            } else if (*op == "+=") {
                op_add_assign(env, left_value, right_value);
            } else if (*op == "-=") {
                op_sub_assign(env, left_value, right_value);
            } else if (*op == "*=") {
                op_mul_assign(env, left_value, right_value);
            } else if (*op == "/=") {
                op_div_assign(env, left_value, right_value);
            } else if (*op == "%") {
                op_remainder(left_value, right_value);
            } else if (*op == "^") {
                op_xor(left_value, right_value);
            } else if (*op == "!=") {
                op_not_euqal(left_value, right_value);
            } else if (*op == "==") {
                op_euqal(left_value, right_value);
            } else if (*op == "&&") {
                op_and(left_value, right_value);
            } else if (*op == "||") {
                op_or(left_value, right_value);
            } else if (*op == ">") {
                op_bigger_than(left_value, right_value);
            } else if (*op == "<") {
                op_less_than(left_value, right_value);
            } else if (*op == ">=") {
                op_bigger_equal(left_value, right_value);
            } else if (*op == "<=") {
                op_less_equal(left_value, right_value);
            } else {
                string msg = "illegal operator: " + left_value.to_string() + " " + *op + " " + right_value.to_string();
                throw BridgeException(msg);
            }

            return left_value;
        }

        virtual string description() {
            string desc = "(";
            desc += _left->description();
            desc += _op->description();
            desc += _right->description();
            return desc;
        }

    private:
        void op_add(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            if (left._type == INT) {
                switch (right._type) {
                    case INT:
                        left._int += right._int;
                        break;
                    case FLOAT:
                        left._float = left._int + right._float;
                        left._type = FLOAT;
                        break;
                    default:
                        left._string = left.to_string() + right.to_string();
                        left._type = STRING;
                        break;
                }
            } else if (left._type == FLOAT) {
                switch (right._type) {
                    case INT:
                        left._float += right._int;
                        break;
                    case FLOAT:
                        left._float += right._float;
                        break;
                    default:
                        left._string = left.to_string() + right.to_string();
                        left._type = STRING;
                        break;
                }
            } else {
                left._string = left.to_string() + right.to_string();
                left._type = STRING;
            }
        }

        void op_sub(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            if (left._type == INT) {
                switch (right._type) {
                    case INT:
                        left._int -= right._int;
                        break;
                    case FLOAT:
                        left._float = left._int - right._float;
                        left._type = FLOAT;
                        break;
                    default:
                        throw BridgeException("Expression error: only INT/FLOAT support op '-'");
                }
            } else if (left._type == FLOAT) {
                switch (right._type) {
                    case INT:
                        left._float += right._int;
                        break;
                    case FLOAT:
                        left._float += right._float;
                        break;
                    default:
                        throw BridgeException("Expression error: only INT/FLOAT support op '-'");
                }
            } else {
                throw BridgeException("Expression error: only INT/FLOAT support op '-'");
            }
        }

        void op_mul(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            if (left._type == INT) {
                switch (right._type) {
                    case INT:
                        left._int *= right._int;
                        break;
                    case FLOAT:
                        left._float = left._int * right._float;
                        left._type = FLOAT;
                        break;
                    default:
                        throw BridgeException("Expression error: only INT/FLOAT support op '*'");
                }
            } else if (left._type == FLOAT) {
                switch (right._type) {
                    case INT:
                        left._float *= right._int;
                        break;
                    case FLOAT:
                        left._float *= right._float;
                        break;
                    default:
                        throw BridgeException("Expression error: only INT/FLOAT support op '*'");
                }
            } else {
                throw BridgeException("Expression error: only INT/FLOAT support op '-'");
            }
        }

        void op_div(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            if (left._type == INT) {
                switch (right._type) {
                    case INT:
                        if (right._int == 0) {
                            throw BridgeException("Expression error: divide by 0");
                        }
                        left._int /= right._int;
                        break;
                    case FLOAT:
                        if (fabs(right._float) <= 0.0000001f) {
                            throw BridgeException("Expression error: divide by 0");
                        }
                        left._float = left._int / right._float;
                        left._type = FLOAT;
                        break;
                    default:
                        throw BridgeException("Expression error: only INT/FLOAT support op '/'");
                }
            } else if (left._type == FLOAT) {
                switch (right._type) {
                    case INT:
                        if (right._int == 0) {
                            throw BridgeException("Expression error: divide by 0");
                        }
                        left._float /= right._int;
                        break;
                    case FLOAT:
                        if (fabs(right._float) <= 0.0000001f) {
                            throw BridgeException("Expression error: divide by 0");
                        }
                        left._float /= right._float;
                        break;
                    default:
                        throw BridgeException("Expression error: only INT/FLOAT support op '*'");
                }
            } else {
                throw BridgeException("Expression error: only INT/FLOAT support op '-'");
            }
        }

        void op_add_assign(EnvironmentPtr &env, BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            op_add(left, right);
            auto left_var = dynamic_pointer_cast<VarLiteral>(_left);
            if (left_var != nullptr) {
                left_var->assign(env, left);
            } else {
                throw BridgeException("Expression error: only var support op '+='");
            }
        }

        void op_sub_assign(EnvironmentPtr &env, BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            op_sub(left, right);
            auto left_var = dynamic_pointer_cast<VarLiteral>(_left);
            if (left_var != nullptr) {
                left_var->assign(env, left);
            } else {
                throw BridgeException("Expression error: only var support op '-='");
            }
        }

        void op_mul_assign(EnvironmentPtr &env, BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            op_mul(left, right);
            auto left_var = dynamic_pointer_cast<VarLiteral>(_left);
            if (left_var != nullptr) {
                left_var->assign(env, left);
            } else {
                throw BridgeException("Expression error: only var support op '*='");
            }
        }

        void op_div_assign(EnvironmentPtr &env, BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            op_div(left, right);
            auto left_var = dynamic_pointer_cast<VarLiteral>(_left);
            if (left_var != nullptr) {
                left_var->assign(env, left);
            } else {
                throw BridgeException("Expression error: only var support op '/='");
            }
        }

        void op_remainder(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            if (left._type == INT && right._type == INT) {
                left._int %= right._int;
            } else {
                string msg = "Expression error: " + left.to_string() + " % " + right.to_string();
                throw BridgeException(msg);
            }
        }

        void op_xor(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            if (left._type == INT && right._type == INT) {
                left._int ^= right._int;
            } else {
                string msg = "Expression error: " + left.to_string() + " ^ " + right.to_string();
                throw BridgeException(msg);
            }
        }

        void op_not_euqal(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            left._bool = left.to_string() != right.to_string();
            left._bool = BOOL;
        }

        void op_euqal(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            left._bool = left.to_string() == right.to_string();
            left._bool = BOOL;
        }

        void op_and(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            if (left._type == BOOL && right._type == BOOL) {
                left._bool = left._bool && right._bool;
            } else {
                string msg = "Expression error: " + left.to_string() + " && " + right.to_string();
                throw BridgeException(msg);
            }
        }

        void op_or(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            if (left._type == BOOL && right._type == BOOL) {
                left._bool = left._bool || right._bool;
            } else {
                string msg = "Expression error: " + left.to_string() + " || " + right.to_string();
                throw BridgeException(msg);
            }
        }

        void op_bigger_than(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            bool b;
            switch (left._type) {
                case INT:
                    if (right._type == INT) {
                        b = left._int > right._int;
                    } else if (right._type == FLOAT) {
                        b = left._int > right._float;
                    } else if (right._type == NONE) {
                        b = left._int > 0;
                    } else {
                        string msg = "Expression error: " + left.to_string() + " > " + right.to_string();
                        throw BridgeException(msg);
                    }
                    break;
                case FLOAT:
                    if (right._type == INT) {
                        b = left._float > right._int;
                    } else if (right._type == FLOAT) {
                        b = left._float > right._float;
                    } else if (right._type == NONE) {
                        b = left._float > 0;
                    } else {
                        string msg = "Expression error: " + left.to_string() + " > " + right.to_string();
                        throw BridgeException(msg);
                    }
                    break;
                case NONE:
                    if (right._type == INT) {
                        b = 0 > right._int;
                    } else if (right._type == FLOAT) {
                        b = 0 > right._float;
                    } else if (right._type == NONE) {
                        b = false;
                    } else {
                        string msg = "Expression error: " + left.to_string() + " > " + right.to_string();
                        throw BridgeException(msg);
                    }
                    break;
                default:
                    string msg = "Expression error: " + left.to_string() + " > " + right.to_string();
                    throw BridgeException(msg);
            }

            left._type = BOOL;
            left._bool = b;
        }

        void op_less_than(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            bool b;
            switch (left._type) {
                case INT:
                    if (right._type == INT) {
                        b = left._int < right._int;
                    } else if (right._type == FLOAT) {
                        b = left._int < right._float;
                    } else if (right._type == NONE) {
                        b = left._int < 0;
                    } else {
                        string msg = "Expression error: " + left.to_string() + " < " + right.to_string();
                        throw BridgeException(msg);
                    }
                    break;
                case FLOAT:
                    if (right._type == INT) {
                        b = left._float < right._int;
                    } else if (right._type == FLOAT) {
                        b = left._float < right._float;
                    } else if (right._type == NONE) {
                        b = left._float < 0;
                    } else {
                        string msg = "Expression error: " + left.to_string() + " < " + right.to_string();
                        throw BridgeException(msg);
                    }
                    break;
                case NONE:
                    if (right._type == INT) {
                        b = 0 < right._int;
                    } else if (right._type == FLOAT) {
                        b = 0 < right._float;
                    } else if (right._type == NONE) {
                        b = false;
                    } else {
                        string msg = "Expression error: " + left.to_string() + " < " + right.to_string();
                        throw BridgeException(msg);
                    }
                    break;
                default:
                    string msg = "Expression error: " + left.to_string() + " < " + right.to_string();
                    throw BridgeException(msg);
            }

            left._type = BOOL;
            left._bool = b;
        }

        void op_bigger_equal(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            bool b;
            switch (left._type) {
                case INT:
                    if (right._type == INT) {
                        b = left._int >= right._int;
                    } else if (right._type == FLOAT) {
                        b = left._int >= right._float;
                    } else if (right._type == NONE) {
                        b = left._int >= 0;
                    } else {
                        string msg = "Expression error: " + left.to_string() + " >= " + right.to_string();
                        throw BridgeException(msg);
                    }
                    break;
                case FLOAT:
                    if (right._type == INT) {
                        b = left._float >= right._int;
                    } else if (right._type == FLOAT) {
                        b = left._float >= right._float;
                    } else if (right._type == NONE) {
                        b = left._float >= 0;
                    } else {
                        string msg = "Expression error: " + left.to_string() + " >= " + right.to_string();
                        throw BridgeException(msg);
                    }
                    break;
                case NONE:
                    if (right._type == INT) {
                        b = 0 >= right._int;
                    } else if (right._type == FLOAT) {
                        b = 0 >= right._float;
                    } else if (right._type == NONE) {
                        b = false;
                    } else {
                        string msg = "Expression error: " + left.to_string() + " >= " + right.to_string();
                        throw BridgeException(msg);
                    }
                    break;
                default:
                    string msg = "Expression error: " + left.to_string() + " >= " + right.to_string();
                    throw BridgeException(msg);
            }

            left._type = BOOL;
            left._bool = b;
        }

        void op_less_equal(BridgeValue &left, BridgeValue &right) throw(BridgeException) {
            bool b;
            switch (left._type) {
                case INT:
                    if (right._type == INT) {
                        b = left._int <= right._int;
                    } else if (right._type == FLOAT) {
                        b = left._int <= right._float;
                    } else if (right._type == NONE) {
                        b = left._int <= 0;
                    } else {
                        string msg = "Expression error: " + left.to_string() + " <= " + right.to_string();
                        throw BridgeException(msg);
                    }
                    break;
                case FLOAT:
                    if (right._type == INT) {
                        b = left._float <= right._int;
                    } else if (right._type == FLOAT) {
                        b = left._float <= right._float;
                    } else if (right._type == NONE) {
                        b = left._float <= 0;
                    } else {
                        string msg = "Expression error: " + left.to_string() + " <= " + right.to_string();
                        throw BridgeException(msg);
                    }
                    break;
                case NONE:
                    if (right._type == INT) {
                        b = 0 <= right._int;
                    } else if (right._type == FLOAT) {
                        b = 0 <= right._float;
                    } else if (right._type == NONE) {
                        b = false;
                    } else {
                        string msg = "Expression error: " + left.to_string() + " <= " + right.to_string();
                        throw BridgeException(msg);
                    }
                    break;
                default:
                    string msg = "Expression error: " + left.to_string() + " <= " + right.to_string();
                    throw BridgeException(msg);
            }

            left._type = BOOL;
            left._bool = b;
        }

    protected:
        AstTreePtr _left;
        AstLeafPtr _op;
        AstTreePtr _right;
    };
}

#endif //HELLOANDROID_EXPRESSION_H
