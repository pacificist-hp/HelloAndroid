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
}

#endif //HELLOANDROID_STATEMENT_H
