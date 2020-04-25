//
// Created by Administrator on 2020/4/16.
//

#include "include/common.h"
#include "include/Manager.h"
#include "include/Bridge.h"

namespace bridge {

    void Manager::register_function(const char *func_name, int param_num,
                                    BRIDGE_FUNC_BODY outer_func) {
        LOGD("Manager::register_function: %s, %d", func_name, param_num);
        Bridge::register_function(func_name, param_num, outer_func);
    }

    int Manager::load_code(const char *code) throw(bridge_exception) {
        LOGD("Manager::load_code");
        BridgePtr ptr = make_shared<Bridge>();
        ptr->load_code(code);
        return ptr->get_id();
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
