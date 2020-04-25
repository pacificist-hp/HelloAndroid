//
// Created by Administrator on 2020/4/16.
//

#ifndef HELLOANDROID_BRIDGE_H
#define HELLOANDROID_BRIDGE_H

#include "../engine/func.h"
#include "../engine/lexer.h"

namespace bridge {

    class Bridge {
    public:
        Bridge();

        ~Bridge();

    public:
        static void
        register_function(const char *func_name, int param_num, BRIDGE_FUNC_BODY outer_func);

    public:
        int get_id();

        int load_code(const char *code) throw(bridge_exception);

    private:
        static func_def_ptr
        create_bridge_func(string func_name, vector<string> param_name, ast_tree_ptr body);

    private:
        int load(bridge_reader_ptr reader);

        void parse() throw(bridge_exception);

        void build_to_string_func();

        void build_internal_func(string name, vector<string> vec_param, ast_tree_ptr body);

        void reg_func(string func_name, func_def_ptr func_def);

    private:
        static func_def_map_ptr s_map_reg_func_def;
        static int s_bridge_id_index;

    private:
        int _id;
    };

    typedef shared_ptr<Bridge> BridgePtr;
}

#endif //HELLOANDROID_BRIDGE_H
