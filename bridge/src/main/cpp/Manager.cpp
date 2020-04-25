//
// Created by Administrator on 2020/4/16.
//

#include "include/common.h"
#include "include/Manager.h"
#include "include/Bridge.h"

namespace bridge {

    void Manager::register_function(const char *func_name, int param_num,
                                    BRIDGE_FUNC_BODY out_func) {
        LOGD("Manager::register_function: %s, %d", func_name, param_num);
        bridge::Bridge::register_function(func_name, param_num, out_func);
    }

    int Manager::load_code(const char *code) throw(bridge_exception) {
        LOGD("Manager::load_code");
        return 0;
    }

    bridge_value
    Manager::invoke(string func_name, bridge_value *args, int args_num) throw(bridge_exception) {
        LOGD("Manager::invoke: %s, %d", func_name.c_str(), args_num);
        return bridge_value();
    }

    void Manager::release(int bridge_id) {
        LOGD("Manager::release: %d", bridge_id);
    }
}
