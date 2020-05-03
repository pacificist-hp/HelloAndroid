//
// Created by Administrator on 2020/4/16.
//

#include "include/common.h"
#include "include/Bridge.h"

#include "engine/Token.h"

namespace bridge {
    func_def_map_ptr Bridge::s_map_reg_func_def = make_shared<unordered_map<string, bridge::func_def_ptr>>();
    int Bridge::s_bridge_id_index = 0;

    Bridge::Bridge() {
        _lexer = make_shared<Lexer>();
        _parser = make_shared<Parser>(_lexer);
        _vec_statement = make_shared<vector<AstTreePtr>>();
        _map_func_def = make_shared<unordered_map<string, func_def_ptr>>();
    }

    Bridge::~Bridge() {

    }

    void Bridge::register_function(const char *func_name, int param_num,
                                   bridge::BRIDGE_FUNC_BODY outer_func) {
        LOGD("Bridge::register_function: %s, %d", func_name, param_num);

        vector<string> vec;
        string base = "a";
        for (int i = 0; i < param_num; i++) {
            vec.push_back(base + to_string(i));
        }

        AstTreePtr body = make_shared<outer_func_body>(func_name, vec, outer_func);
        func_def_ptr func = create_bridge_func(func_name, vec, body);

        if (nullptr != func) {
            s_map_reg_func_def->insert({func_name, func});
        }
    }

    func_def_ptr
    Bridge::create_bridge_func(string func_name, vector<string> param_name, AstTreePtr body) {
        LOGD("Bridge::create_bridge_func: %s", func_name.c_str());

        AstLeafVecPtr params_vec = make_shared<vector<AstLeafPtr>>();

        TokenPtr name_token = make_shared<IdentifierToken>(make_shared<string>(func_name), 0);
        AstLeafPtr name_leaf = make_shared<AstLeaf>(name_token);

        for (int i = 0; i < param_name.size(); i++) {
            TokenPtr param_token = make_shared<IdentifierToken>(make_shared<string>(param_name[i]),
                                                                0);
            AstLeafPtr param_leaf = make_shared<AstLeaf>(param_token);
            params_vec->push_back(param_leaf);
        }

        func_param_list_ptr params_list = make_shared<func_param_list>(params_vec);

        return make_shared<func_def>(name_leaf, params_list, body);
    }

    int Bridge::get_id() {
        return _id;
    }

    int Bridge::load_code(const char *code) throw(bridge_exception) {
        LOGD("Bridge::load_code");
        ReaderPtr reader = make_shared<Reader>();
        reader->set_code(code);

        return load(reader);
    }

    int Bridge::load(ReaderPtr reader) {
        build_to_string_func();

        _lexer->set_reader(reader);

        for (auto it = s_map_reg_func_def->begin(); it != s_map_reg_func_def->end(); it++) {
            _parser->register_func(it->first, it->second);
        }

        parse();

        _id = s_bridge_id_index++;
        return _id;
    }

    void Bridge::parse() throw(bridge_exception) {
        LOGD("Bridge::parse");

        if (_lexer == nullptr || _parser == nullptr) {
            return;
        }

        _vec_statement->clear();
        _map_func_def->clear();

        TokenPtr ret = _lexer->peek(0);
        if (ret != nullptr) {
            AstTreePtr tree = _parser->expression();
            while (tree != nullptr) {
                func_def_ptr func_expr = dynamic_pointer_cast<func_def>(tree);
                if (func_expr != nullptr) {
                    string name = func_expr->func_name();
                    _map_func_def->insert({name, func_expr});
                } else {
                    _vec_statement->push_back(tree);
                }

                tree = _parser->expression();
            }
        }


    }

    void Bridge::build_to_string_func() {
        vector<string> vec;
        vec.push_back("a");

        AstTreePtr body = make_shared<to_string_func_body>("a");
        build_internal_func("toString", vec, body);
    }

    void Bridge::build_internal_func(string name, vector<string> vec_param, AstTreePtr body) {
        func_def_ptr func = create_bridge_func(name, vec_param, body);
        if (func != nullptr) {
            reg_func(name, func);
        }
    }

    void Bridge::reg_func(string func_name, func_def_ptr func_def) {
        if (_parser != nullptr) {
            _parser->register_func(func_name, func_def);
        }
    }
}
