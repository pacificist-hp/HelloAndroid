//
// Created by Administrator on 2020/4/16.
//

#ifndef HELLOANDROID_BRIDGE_H
#define HELLOANDROID_BRIDGE_H

#include "../parser/func.h"

namespace bridge {

    class Bridge {
    public:
        Bridge();

        ~Bridge();

        int get_id();

        int load_code(const char *code) throw(bridge_exception);

        int load_file(const char *file) throw(bridge_exception);

        static void
        register_function(const char *func_name, int param_num, BRIDGE_FUNC_BODY outer_func);

    private:
        static func_def_ptr
        create_bridge_func(string func_name, vector<string> param_name, ast_tree_ptr body);

    private:
        static func_def_map_ptr s_map_reg_func_def;
    };

}

#endif //HELLOANDROID_BRIDGE_H
