//
// Created by Administrator on 2020/4/16.
//

#ifndef HELLOANDROID_COMMON_H
#define HELLOANDROID_COMMON_H

#include <string>

using namespace std;

#define LOG_TAG "bridge_lib"

#define LOG_ENABLE 1

#if __ANDROID__
    #include <android/log.h>
#endif

#if LOG_ENABLE
    #if  __ANDROID__
        #define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
    #else
        #define LOGD(...)
    #endif
#else
    #define LOGD(...)
#endif

enum val_type {
    STRING,
    NONE
};

struct bridge_value {
public:
    bridge_value(const char* s) {
        _string = s;
        _type = STRING;
    }

    string _string;
    val_type _type;
};

#endif //HELLOANDROID_COMMON_H
