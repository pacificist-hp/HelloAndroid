//
// Created by Administrator on 2020/4/16.
//

#include "include/common.h"
#include "include/Manager.h"

namespace bridge {

    unordered_map<int, BridgePtr> Manager::s_map_bridges;

    void Manager::register_function(const char *func_name, int param_num,
                                    OUTER_FUNC_CALLBACK outer_func) {
        Bridge::register_function(func_name, param_num, outer_func);
    }

    int Manager::load_code(const char *code) throw(BridgeException) {
        BridgePtr bridge = make_shared<Bridge>();
        bridge->load_code(code);
        s_map_bridges.insert({bridge->get_id(), bridge});

        return bridge->get_id();
    }

    BridgeValue
    Manager::invoke(int bridge_id, string func_name, BridgeValue *args,
                    int args_num) throw(BridgeException) {
        auto it = s_map_bridges.find(bridge_id);
        if (it == s_map_bridges.end()) {
            return BridgeValue();
        }

        return it->second->invoke(func_name, args, args_num);
    }

    void Manager::release(int bridge_id) {
        s_map_bridges.erase(bridge_id);
    }
}
