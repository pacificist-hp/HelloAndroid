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
}
