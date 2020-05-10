//
// Created by Administrator on 2020/4/16.
//

#ifndef HELLOANDROID_MANAGER_H
#define HELLOANDROID_MANAGER_H

#include <unordered_map>

#include "Bridge.h"

#define VERSION_CODE 1

namespace bridge {

    class Manager {
    public:
        static void
        register_function(const char *func_name, int param_num, OUTER_FUNC_CALLBACK outer_func);

        static int load_code(const char *code) throw(BridgeException);

        static BridgeValue
        invoke(int bridge_id, string func_name, BridgeValue args[],
               int args_num) throw(BridgeException);

        static void release(int bridge_id);

    private:
        static unordered_map<int, BridgePtr> s_map_bridges;
    };
}

#endif //HELLOANDROID_MANAGER_H
