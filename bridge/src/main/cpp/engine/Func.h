//
// Created by Administrator on 2020/4/23.
//

#ifndef HELLOANDROID_FUNC_H
#define HELLOANDROID_FUNC_H

#include <unordered_map>
#include <vector>

#include "AstTree.h"

using namespace std;

namespace bridge {

    class outer_func_body : public AstTree {
    public:
        outer_func_body(string name, vector<string> &vec_param, BRIDGE_FUNC_BODY outer_func) {
            _name = name;
            _vec_param = vec_param;
            _outer_func = outer_func;
        }

        virtual bridge_value evaluate(EnvironmentPtr &env) throw(bridge_exception) {
            bridge_value *args = nullptr;

            int size = int(_vec_param.size());
            if (size > 0) {
                args = new bridge_value[size];
                for (int i = 0; i < size; i++) {
                    bridge_value val = env->get(_vec_param[i]);
                    args[i] = val;
                }
            }

            bridge_value ret = _outer_func(env->get_bridge_id(), 0, _name.c_str(), args, size);

            if (args != nullptr) {
                delete[] args;
            }

            return ret;
        }

        virtual string description() {
            return "{customize_func_body}";
        }

    private:
        string _name;
        vector<string> _vec_param;
        BRIDGE_FUNC_BODY _outer_func;
    };

    class func_param_list : public AstTree {
    public:
        func_param_list(const AstLeafVecPtr &params) {
            _param_list = params;
        }

        shared_ptr<vector<string>> param_name_list() {
            if (_param_list == nullptr) {
                return nullptr;
            }

            shared_ptr<vector<string>> ptr = make_shared<vector<string>>();
            for (auto it : *_param_list) {
                string_ptr name = it->try_get_identifier();
                if (name != nullptr) {
                    ptr->push_back(*name);
                }
            }

            return ptr;
        }

        virtual string description() {
            string desc;
            for (auto it = _param_list->begin(); it != _param_list->end(); it++) {
                if (it != _param_list->begin()) {
                    desc += ",";
                }
                desc += (*it)->description();
            }
            return desc;
        }

    private:
        AstLeafVecPtr _param_list;
    };

    typedef shared_ptr<func_param_list> func_param_list_ptr;

    class func_def : public AstTree {
    public:
        func_def(AstLeafPtr name, func_param_list_ptr params, AstTreePtr block) {
            _name = name;
            _param_list = params;
            _block = block;
        }

        string func_name() {
            string ret;
            if (_name != nullptr) {
                string_ptr ptr = _name->try_get_identifier();
                if (ptr != nullptr) {
                    ret = *ptr;
                }
            }

            return ret;
        }

        shared_ptr<vector<string>> param_name_list() {
            if (_param_list == nullptr) {
                return nullptr;
            }

            return _param_list->param_name_list();
        }

        virtual bridge_value evaluate(EnvironmentPtr &env) throw(bridge_exception) {
            LOGD("func_def::evaluate: this=%s,%s", description().c_str(),
                 _block->description().c_str());
            return _block->evaluate(env);
        }

        virtual string description() {
            string desc;
            desc += _name->description();
            desc += "(";
            desc += _param_list->description();
            desc += ")";
            return desc;
        }

    private:
        AstLeafPtr _name;
        func_param_list_ptr _param_list;
        AstTreePtr _block;

    };

    typedef shared_ptr<func_def> func_def_ptr;

    typedef shared_ptr<unordered_map<string, func_def_ptr>> func_def_map_ptr;

    class AstList : public AstTree {
    public:
        AstList(AstTreeVecPtr children) {
            _children = children;
        }

        int children_count() {
            return int(_children->size());
        }

        AstTreePtr child(int i) throw(bridge_exception) {
            if (i >= 0 && i < _children->size()) {
                return _children->at(i);
            }

            throw bridge_exception("AstList::child error");
        }

        virtual bridge_value evaluate(EnvironmentPtr &env) throw(bridge_exception) {
            bridge_value v;
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

    class ArgsList : public AstList {
    public:
        ArgsList(const AstTreeVecPtr &children) : AstList(children) {}
    };

    typedef shared_ptr<ArgsList> ArgsListPtr;

    class CallStatement : public AstTree {
    public:
        CallStatement(AstLeafPtr name, ArgsListPtr args, func_def_ptr func_def) {
            _name = name;
            _args = args;
            _func_def = func_def;
        }

        void set_func_def(func_def_ptr func_def) {
            _func_def = func_def;
        }

        virtual bridge_value evaluate(EnvironmentPtr &env) throw(bridge_exception) {
            if (_func_def == nullptr) {
                string err = "function ";
                if (_name != nullptr) {
                    err += _name->description();
                }
                err += " is not defined";
                throw bridge_exception(err);
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
        void evaluate_args(EnvironmentPtr &env, func_def_ptr func_def) throw(bridge_exception) {
            shared_ptr<vector<string>> params_name_list = func_def->param_name_list();
            if (params_name_list == nullptr) {
                return;
            }

            int count = int(params_name_list->size());
            int arg_count = _args->children_count();
            for (int i = 0; i < count; i++) {
                bridge_value v;
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
        func_def_ptr _func_def;
    };

    typedef shared_ptr<CallStatement> CallStatementPtr;

    class to_string_func_body : public AstTree {
    public:
        to_string_func_body(string param_name) {
            _param_name = param_name;
        }

        virtual string description() {
            return "{to_string_func_body}";
        }

    private:
        string _param_name;
    };
}
#endif //HELLOANDROID_FUNC_H
