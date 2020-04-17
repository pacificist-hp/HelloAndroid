//
// Created by Administrator on 2020/4/16.
//

#ifndef HELLOANDROID_MANAGER_H
#define HELLOANDROID_MANAGER_H

namespace bridge {

    class Manager {
    public:
        static void register_function(const char* func_name, int param_num);
    };

}

#endif //HELLOANDROID_MANAGER_H
