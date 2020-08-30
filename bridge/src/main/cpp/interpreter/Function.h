//
// Created by Administrator on 2020/4/23.
//

#ifndef HELLOANDROID_FUNCTION_H
#define HELLOANDROID_FUNCTION_H

#include <unordered_map>
#include <vector>

#include "AstTree.h"

using namespace std;

namespace bridge {

    class OuterFuncBody : public AstTree {
    public:
        OuterFuncBody(string name, vector<string> &vec_param, OUTER_FUNC_CALLBACK outer_func) {
            _name = name;
            _vec_param = vec_param;
            _outer_func = outer_func;
        }

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            BridgeValue *args = nullptr;

            int size = int(_vec_param.size());
            if (size > 0) {
                args = new BridgeValue[size];
                for (int i = 0; i < size; i++) {
                    BridgeValue val = env->get(_vec_param[i]);
                    args[i] = val;
                }
            }

            BridgeValue ret = _outer_func(_name.c_str(), args, size);

            if (args != nullptr) {
                delete[] args;
            }

            return ret;
        }

        virtual string description() {
            return "OuterFuncBody: " + _name;
        }

    private:
        string _name;
        vector<string> _vec_param;
        OUTER_FUNC_CALLBACK _outer_func;
    };

    class FuncParamList : public AstTree {
    public:
        FuncParamList(const AstLeafVecPtr &params) {
            _param_list = params;
        }

        shared_ptr<vector<string>> param_name_list() {
            if (_param_list == nullptr) {
                return nullptr;
            }

            shared_ptr<vector<string>> ptr = make_shared<vector<string>>();
            for (auto it : *_param_list) {
                StringPtr name = it->try_get_identifier();
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

    typedef shared_ptr<FuncParamList> FuncParamListPtr;

    class Function : public AstTree {
    public:
        Function(AstLeafPtr name, FuncParamListPtr params, AstTreePtr block) {
            _name = name;
            _param_list = params;
            _block = block;
        }

        string func_name() {
            string ret;
            if (_name != nullptr) {
                StringPtr ptr = _name->try_get_identifier();
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

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException, FlowException) {
            LOGD("Function::evaluate: this=%s,%s", description().c_str(),
                 _block->description().c_str());
            try {
                return _block->evaluate(env);
            } catch (FlowException &e) {
                return BridgeValue(e._value);
            }
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
        FuncParamListPtr _param_list;
        AstTreePtr _block;

    };

    typedef shared_ptr<Function> FuncPtr;

    typedef shared_ptr<unordered_map<string, FuncPtr>> FuncMapPtr;

    class ArgsList : public AstList {
    public:
        ArgsList(const AstTreeVecPtr &children) : AstList(children) {}
    };

    typedef shared_ptr<ArgsList> ArgsListPtr;
}
#endif //HELLOANDROID_FUNCTION_H
