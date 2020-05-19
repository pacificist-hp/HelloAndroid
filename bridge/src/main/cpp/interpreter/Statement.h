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

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            return BridgeValue();
        }

        virtual string description() {
            return "{empty}";
        }
    };

    class VarStatement : public AstTree {
    public:
        VarStatement(VarLiteralPtr var_literal, AstTreePtr value) {
            _var_literal = var_literal;
            _value = value;
        }

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            BridgeValue value = _value == nullptr ? BridgeValue() : _value->evaluate(env);
            _var_literal->define(env, value);
            return value;
        }

        virtual string description() {
            string desc = "var ";
            desc += _var_literal->description();
            if (_value != nullptr) {
                desc += " = ";
                desc += _value->description();
            }

            return desc;
        }

    private:
        VarLiteralPtr _var_literal;
        AstTreePtr _value;
    };

    class BlockStatement : public AstList {
    public:
        BlockStatement(AstTreeVecPtr children) : AstList(children) {}
    };


    class CallStatement : public AstTree {
    public:
        CallStatement(AstLeafPtr name, ArgsListPtr args, FuncPtr func_def) {
            _name = name;
            _args = args;
            _func_def = func_def;
        }

        void set_func_def(FuncPtr func_def) {
            _func_def = func_def;
        }

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            if (_func_def == nullptr) {
                string err = "function ";
                if (_name != nullptr) {
                    err += _name->description();
                }
                err += " is not defined";
                throw BridgeException(err);
            }

            evaluate_args(env, _func_def);
            return _func_def->evaluate(env);
        }

        virtual string description() {
            string desc;
            desc += _name->description();
            desc += "(";
            desc += _args->description();
            desc += ")";
            return desc;
        }

    private:
        void evaluate_args(EnvironmentPtr &env, FuncPtr func_def) throw(BridgeException) {
            shared_ptr<vector<string>> params_name_list = func_def->param_name_list();
            if (params_name_list == nullptr) {
                return;
            }

            int count = int(params_name_list->size());
            int arg_count = _args->children_count();
            for (int i = 0; i < count; i++) {
                BridgeValue v;
                if (arg_count > i) {
                    AstTreePtr arg = _args->child(i);
                    if (arg != nullptr) {
                        v = arg->evaluate(env);
                    }
                }

                LOGD("CallStatement::evaluate_args: %s->%s", params_name_list->at(i).c_str(),
                     v.to_string().c_str());
                env->set(params_name_list->at(i), v);
            }
        }

    private:
        AstLeafPtr _name;
        ArgsListPtr _args;
        FuncPtr _func_def;
    };

    typedef shared_ptr<CallStatement> CallStatementPtr;

    // for "if" and "while"
    class ConditionStatement : public AstTree {
    public:
        ConditionStatement(AstTreePtr condition) {
            _condition = condition;
        }

        bool is_condition_true(EnvironmentPtr &env) {
            bool ret = false;
            BridgeValue value = _condition->evaluate(env);
            if (value._type == STRING) {
                ret = !value._string.empty();
            }

            return ret;
        }

        virtual string description() {
            return _condition == nullptr ? "" : _condition->description();
        }

    protected:
        AstTreePtr _condition;
    };

    class IfStatement : public ConditionStatement {
    public:
        IfStatement(AstTreePtr condition, AstTreePtr then_block, AstTreePtr else_block)
                : ConditionStatement(condition) {
            _then_block = then_block;
            _else_block = else_block;
        }

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            BridgeValue value;
            if (is_condition_true(env)) {
                value = _then_block->evaluate(env);
            } else {
                value = _else_block->evaluate(env);
            }

            return value;
        }

        virtual string description() {
            string desc = "if(";
            desc += _condition->description();
            desc += ") ";
            desc += _then_block->description();

            if (_else_block != nullptr) {
                desc += " else ";
                desc += _else_block->description();
            }

            return desc;
        }

    private:
        AstTreePtr _then_block;
        AstTreePtr _else_block;
    };
}

#endif //HELLOANDROID_STATEMENT_H
