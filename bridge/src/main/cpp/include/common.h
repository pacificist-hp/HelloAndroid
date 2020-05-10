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

#define BRIDGE_VERSION_CODE 1

namespace bridge {

    enum val_type {
        STRING,
        NONE
    };

    struct BridgeValue {
    public:
        BridgeValue() {
            _type = NONE;
        }

        BridgeValue(const char *s) {
            _string = s;
            _type = STRING;
        }

        string _string;
        val_type _type;

        string to_string() {
            switch (_type) {
                case STRING:
                    return _string;
                default:
                    return "null";
            }
        }
    };

    struct BridgeException {
        int _line;
        string _msg;

        BridgeException(string msg, int line) {
            _msg = msg;
            _line = line;
        }

        BridgeException(string msg) {
            _msg = msg;
        }
    };

    typedef BridgeValue (*OUTER_FUNC_CALLBACK)(const char *name, BridgeValue *params,
                                               int param_num);

    typedef shared_ptr<string> StringPtr;
}

#endif //HELLOANDROID_COMMON_H
