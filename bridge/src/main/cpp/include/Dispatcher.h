//
// Created by Administrator on 2020/4/16.
//

#ifndef HELLOANDROID_DISPATCHER_H
#define HELLOANDROID_DISPATCHER_H

namespace bridge {

    class Dispatcher {
    public:
        static void register_function(const char* func_name, int param_num);
    };

}

#endif //HELLOANDROID_DISPATCHER_H
