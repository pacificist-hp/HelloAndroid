//
// Created by Administrator on 2020/4/16.
//

#ifndef HELLOANDROID_MANAGER_H
#define HELLOANDROID_MANAGER_H

#define VERSION_CODE 1

namespace bridge {

    class Manager {
    public:
        static void
        register_function(const char *func_name, int param_num, BRIDGE_FUNC_BODY out_func);

        static int load_code(const char *code) throw(bridge_exception);

        static void release(int bridge_id);
    };

}

#endif //HELLOANDROID_MANAGER_H
