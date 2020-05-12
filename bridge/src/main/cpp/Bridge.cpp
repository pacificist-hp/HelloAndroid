//
// Created by Administrator on 2020/4/16.
//

#include "include/common.h"
#include "include/Bridge.h"

#include "interpreter/Token.h"

namespace bridge {
    FuncMapPtr Bridge::s_map_reg_func_def = make_shared<unordered_map<string, FuncPtr>>();
    int Bridge::s_bridge_id_index = 0;

    int Environment::s_count = 0;

    Bridge::Bridge() {
        _lexer = make_shared<Lexer>();
        _parser = make_shared<Parser>(_lexer);
        _vec_statement = make_shared<vector<AstTreePtr>>();
        _map_func_def = make_shared<unordered_map<string, FuncPtr>>();
    }

    Bridge::~Bridge() {

    }

    void Bridge::register_function(const char *func_name, int param_num,
                                   bridge::OUTER_FUNC_CALLBACK outer_func) {
        vector<string> vec;
        string base = "a";
        for (int i = 0; i < param_num; i++) {
            vec.push_back(base + to_string(i));
        }

        AstTreePtr body = make_shared<OuterFuncBody>(func_name, vec, outer_func);
        FuncPtr func = create_function(func_name, vec, body);

        if (nullptr != func) {
            s_map_reg_func_def->insert({func_name, func});
        }
    }

    FuncPtr
    Bridge::create_function(string func_name, vector<string> param_name, AstTreePtr body) {
        TokenPtr name_token = make_shared<IdentifierToken>(make_shared<string>(func_name), 0);
        AstLeafPtr name_leaf = make_shared<AstLeaf>(name_token);

        AstLeafVecPtr params_vec = make_shared<vector<AstLeafPtr>>();
        for (int i = 0; i < param_name.size(); i++) {
            TokenPtr param_token = make_shared<IdentifierToken>(make_shared<string>(param_name[i]),
                                                                0);
            AstLeafPtr param_leaf = make_shared<AstLeaf>(param_token);
            params_vec->push_back(param_leaf);
        }

        FuncParamListPtr params_list = make_shared<FuncParamList>(params_vec);

        return make_shared<Function>(name_leaf, params_list, body);
    }

    int Bridge::get_id() {
        return _id;
    }

    int Bridge::load_code(const char *code) throw(BridgeException) {
        ReaderPtr reader = make_shared<Reader>();
        reader->set_code(code);

        return load(reader);
    }

    int Bridge::load(ReaderPtr reader) {
        _lexer->set_reader(reader);

        for (auto it = s_map_reg_func_def->begin(); it != s_map_reg_func_def->end(); it++) {
            _parser->register_func(it->first, it->second);
        }

        parse();

        _id = s_bridge_id_index++;

        _env = make_shared<Environment>(_id);
        for (auto it: *_vec_statement) {
            BridgeValue v = it->evaluate(_env);
            LOGD("Bridge::load: %s->%s", it->description().c_str(), v.to_string().c_str());
        }

        return _id;
    }

    void Bridge::parse() throw(BridgeException) {
        if (_lexer == nullptr || _parser == nullptr) {
            return;
        }

        _vec_statement->clear();
        _map_func_def->clear();

        TokenPtr ret = _lexer->peek(0);
        if (ret != nullptr) {
            AstTreePtr tree = _parser->expression();
            while (tree != nullptr) {
                FuncPtr func_expr = dynamic_pointer_cast<Function>(tree);
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

    BridgeValue
    Bridge::invoke(string func_name, BridgeValue *args, int args_num) throw(BridgeException) {
        if (func_name.empty() || args_num < 0) {
            throw BridgeException("cannot find function");
        }

        StringPtr name = make_shared<string>();
        *name = func_name;

        TokenPtr name_token = make_shared<IdentifierToken>(name, 0);
        AstLeafPtr name_leaf = make_shared<VarLiteral>(name_token);

        AstTreeVecPtr vec = make_shared<vector<AstTreePtr>>();

        for (int i = 0; i < args_num; i++) {
            AstTreePtr arg = nullptr;
            TokenPtr token = nullptr;

            switch (args[i]._type) {
                case STRING:
                    token = make_shared<TextToken>(make_shared<string>(args[i]._string), 0);
                    arg = make_shared<TextLiteral>(token);
                    break;
                default:
                    break;
            }

            if (arg != nullptr) {
                vec->push_back(arg);
            }
        }

        ArgsListPtr args_ptr = make_shared<ArgsList>(vec);

        FuncPtr func_def = nullptr;
        auto it = _map_func_def->find(func_name);
        if (it != _map_func_def->end()) {
            func_def = it->second;
        }

        if (func_def == nullptr) {
            string err = "cannot find function: <";
            err += func_name;
            err += ">";
            throw BridgeException(err);
        }

        CallStatementPtr call_statement = make_shared<CallStatement>(name_leaf, args_ptr, func_def);
        return call_statement->evaluate(_env);
    }
}
