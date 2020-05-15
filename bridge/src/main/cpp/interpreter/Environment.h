//
// Created by Administrator on 2020/5/3.
//

#ifndef HELLOANDROID_ENVIRONMENT_H
#define HELLOANDROID_ENVIRONMENT_H

#include <unordered_map>

#include "../include/common.h"

namespace bridge {
    class Environment {
    public:
        Environment(int bridge_id) {
            _bridge_id = bridge_id;
            s_count++;
        }

        ~Environment() {
            s_count--;
        }

        void set(string &name, BridgeValue &v) {
            _map_val[name] = v;
        }

        void put(string &name, BridgeValue &v) throw(BridgeException) {
            if (exist(name)) {
                _map_val[name] = v;
                return;
            }

            shared_ptr<Environment> env = _outer_env.lock();
            while (env != nullptr) {
                if (env->exist(name)) {
                    env->put(name, v);
                    return;
                }

                env = env->_outer_env.lock();
            }

            string err = "\"";
            err += name;
            err += "\"";
            err += "is not defined";

            throw BridgeException(err);
        }

        BridgeValue get(string &name) throw(BridgeException) {
            auto it = _map_val.find(name);
            if (it == _map_val.end()) {
                throw BridgeException(name + " is not in environment");
            }
            return it->second;
        }

        int get_bridge_id() {
            return _bridge_id;
        }

    private:
        bool exist(string &name) {
            return _map_val.find(name) != _map_val.end();
        }

    private:
        unordered_map <string, BridgeValue> _map_val;
        weak_ptr<Environment> _outer_env;

        int _bridge_id;

        static int s_count;
    };

    typedef shared_ptr<Environment> EnvironmentPtr;
}

#endif //HELLOANDROID_ENVIRONMENT_H
