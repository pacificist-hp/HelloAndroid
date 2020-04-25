//
// Created by Administrator on 2020/4/16.
//

#include "include/common.h"
#include "include/Manager.h"
#include "include/Bridge.h"

namespace bridge {

    void Manager::register_function(const char *func_name, int param_num,
                                    BRIDGE_FUNC_BODY outer_func) {
        Bridge::register_function(func_name, param_num, outer_func);
    }

    int Manager::load_code(const char *code) throw(bridge_exception) {
        BridgePtr bridge = make_shared<Bridge>();
        bridge->load_code(code);
        return bridge->get_id();
    }

    bridge_value
    Manager::invoke(string func_name, bridge_value *args, int args_num) throw(bridge_exception) {
        return bridge_value();
    }

    void Manager::release(int bridge_id) {

    }
}
