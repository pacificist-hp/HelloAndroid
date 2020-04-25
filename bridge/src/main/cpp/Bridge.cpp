//
// Created by Administrator on 2020/4/16.
//

#include "include/common.h"
#include "include/Bridge.h"

#include "parser/token.h"

namespace bridge {
    func_def_map_ptr Bridge::s_map_reg_func_def = make_shared<unordered_map<string, bridge::func_def_ptr>>();
    int Bridge::s_bridge_id_index = 0;

    Bridge::Bridge() {

    }

    Bridge::~Bridge() {

    }

    void Bridge::register_function(const char *func_name, int param_num,
                                           bridge::BRIDGE_FUNC_BODY outer_func) {
        LOGD("Bridge::register_function: %s, %d", func_name, param_num);

        vector<string> vec;
        string base = "a";
        for (int i = 0; i < param_num; i++) {
            vec.push_back(base + to_string(i));
        }

        ast_tree_ptr body = make_shared<outer_func_body>(func_name, vec, outer_func);
        func_def_ptr func = create_bridge_func(func_name, vec, body);

        if (nullptr != func) {
            s_map_reg_func_def->insert({func_name, func});
        }
    }

    func_def_ptr
    Bridge::create_bridge_func(string func_name, vector<string> param_name, ast_tree_ptr body) {
        LOGD("Bridge::create_bridge_func: %s", func_name.c_str());

        ast_leaf_vec_ptr params_vec = make_shared<vector<ast_leaf_ptr>>();

        token_ptr name_token = make_shared<identifier_token>(make_shared<string>(func_name), 0);
        ast_leaf_ptr name_leaf = make_shared<ast_leaf>(name_token);

        for (int i = 0; i < param_name.size(); i++) {
            token_ptr param_token = make_shared<identifier_token>(make_shared<string>(param_name[i]), 0);
            ast_leaf_ptr param_leaf = make_shared<ast_leaf>(param_token);
            params_vec->push_back(param_leaf);
        }

        func_param_list_ptr params_list = make_shared<func_param_list>(params_vec);

        return make_shared<func_def>(name_leaf, params_list, body);
    }

    int Bridge::get_id() {
        return _id;
    }

    int Bridge::load_code(const char *code) throw(bridge_exception) {
        LOGD("Bridge::load_code");
        bridge_reader_ptr reader = make_shared<bridge_reader>();
        reader->set_code(code);

        return load(reader);
    }

    int Bridge::load(bridge_reader_ptr reader) {
        _id = s_bridge_id_index++;
        return _id;
    }

    void Bridge::build_to_string_func() {
        vector<string> vec;
        vec.push_back("a");

        ast_tree_ptr body = make_shared<to_string_func_body>("a");
        build_internal_func("toString", vec, body);
    }

    void Bridge::build_internal_func(string name, vector<string> vec_param, ast_tree_ptr body) {
       func_def_ptr func = create_bridge_func(name, vec_param, body);
       if (func != nullptr) {
           reg_func(name, func);
       }
    }

    void Bridge::reg_func(string func_name, func_def_ptr func_def) {

    }



}
