//
// Created by Administrator on 2020/4/16.
//

#include "include/common.h"
#include "include/Manager.h"

namespace bridge {

    void Manager::register_function(const char* func_name, int param_num) {
        LOGD("Manager::register_function: %s, %d", func_name, param_num);
    }
}
