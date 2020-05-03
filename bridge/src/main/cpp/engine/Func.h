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

    private:
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

        virtual string description() {
            string desc;
            desc += _name->description();
            desc += "(";
            desc += _args->description();
            desc += ")";
            return desc;
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
