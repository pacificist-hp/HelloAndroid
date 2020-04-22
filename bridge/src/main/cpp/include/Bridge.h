//
// Created by Administrator on 2020/4/16.
//

#ifndef HELLOANDROID_BRIDGE_H
#define HELLOANDROID_BRIDGE_H

namespace bridge {

    class Bridge {
    public:
        Bridge();

        ~Bridge();

        int get_id();

        int load_code(const char *code) throw(bridge_exception);

        int load_file(const char *file) throw(bridge_exception);

        static void
        register_function(const char *func_name, int param_num, BRIDGE_FUNC_BODY outer_func_body);
    };

}

#endif //HELLOANDROID_BRIDGE_H
