//
// Created by Administrator on 2020/4/23.
//

#ifndef HELLOANDROID_FUNC_H
#define HELLOANDROID_FUNC_H

#include <unordered_map>
#include <vector>

#include "ast.h"

using namespace std;

namespace bridge {

    class outer_func_body : public ast_tree {
    public:
        outer_func_body(string name, vector<string> &vec_param, BRIDGE_FUNC_BODY outer_func) {
            _name = name;
            _vec_param = vec_param;
            _outer_func = outer_func;
        }

    private:
        string _name;
        vector<string> _vec_param;
        BRIDGE_FUNC_BODY _outer_func;
    };

    class func_param_list : public ast_tree {
    public:
        func_param_list(const ast_leaf_vec_ptr &params_list) {
            _params_list = params_list;
        }

    private:
        ast_leaf_vec_ptr _params_list;
    };


    typedef shared_ptr<func_param_list> func_param_list_ptr;

    class func_def : public ast_tree {
    public:
        func_def(ast_leaf_ptr name, func_param_list_ptr params, ast_tree_ptr block) {

        }
    };

    typedef shared_ptr<func_def> func_def_ptr;

    typedef shared_ptr<unordered_map<string, func_def_ptr>> func_def_map_ptr;
}
#endif //HELLOANDROID_FUNC_H
