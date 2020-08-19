//
// Created by Administrator on 2020/5/3.
//

#ifndef HELLOANDROID_ENVIRONMENT_H
#define HELLOANDROID_ENVIRONMENT_H

#include <unordered_map>

#include "../include/common.h"

namespace bridge {
    class Environment;
    typedef shared_ptr<Environment> EnvironmentPtr;

    class Environment {
    public:
        Environment(int bridge_id) {
            _bridge_id = bridge_id;
            s_count++;
        }

        Environment(Environment &env) {
            _bridge_id = env._bridge_id;
            env.copy_to_map(&_map_val);
            s_count++;
        }

        Environment(EnvironmentPtr &env) {
            _bridge_id = env->_bridge_id;
            _outer_env = env;
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

            EnvironmentPtr env = _outer_env.lock();
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
                EnvironmentPtr env = _outer_env.lock();
                if (env == nullptr) {
                    throw BridgeException(name + " is not in environment");
                } else {
                    return env->get(name);
                }
            } else {
                return it->second;
            }
        }

        void copy_to_map(unordered_map<string, BridgeValue> *desc) {
            if (desc == NULL) {
                return;
            }

            for(auto it = _map_val.begin(); it != _map_val.end(); it++) {
                if (desc->find(it->first) == desc->end()) {
                    (*desc)[it->first] = it->second;
                }
            }

            shared_ptr<Environment> outer = _outer_env.lock();
            // 不需要copy全局变量
            if (outer != nullptr && (outer->_outer_env).lock() != nullptr) {
                outer->copy_to_map(desc);
            }
        }

    private:
        bool exist(string &name) {
            return _map_val.find(name) != _map_val.end();
        }

    private:
        unordered_map<string, BridgeValue> _map_val;
        weak_ptr<Environment> _outer_env;

        int _bridge_id;

        static int s_count;
    };

}

#endif //HELLOANDROID_ENVIRONMENT_H
