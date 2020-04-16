//
// Created by Administrator on 2020/4/16.
//

#include "include/common.h"
#include "include/Dispatcher.h"

namespace bridge {

    void Dispatcher::register_function(const char* func_name, int param_num) {
        LOGD("Dispatcher::register_function: %s, %d", func_name, param_num);
    }
}
