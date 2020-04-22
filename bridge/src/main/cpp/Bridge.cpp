//
// Created by Administrator on 2020/4/16.
//

#include "include/common.h"
#include "include/Bridge.h"

namespace bridge {
    void bridge::Bridge::register_function(const char *func_name, int param_num,
                                           bridge::BRIDGE_FUNC_BODY outer_func_body) {
        LOGD("Bridge::register_function: %s, %d", func_name, param_num);
    }
}
