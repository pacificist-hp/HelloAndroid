//
// Created by Administrator on 2020/4/16.
//

#ifndef HELLOANDROID_BRIDGE_H
#define HELLOANDROID_BRIDGE_H

#include "../engine/Environment.h"
#include "../engine/Func.h"
#include "../engine/Lexer.h"
#include "../engine/Parser.h"

namespace bridge {

    class Bridge {
    public:
        Bridge();

        ~Bridge();

    public:
        static void
        register_function(const char *func_name, int param_num, BRIDGE_FUNC_BODY outer_func);

    public:
        int get_id();

        int load_code(const char *code) throw(bridge_exception);

        bridge_value
        invoke(string func_name, bridge_value args[], int args_num) throw(bridge_exception);

    private:
        static func_def_ptr
        create_bridge_func(string func_name, vector<string> param_name, AstTreePtr body);

    private:
        int load(ReaderPtr reader);

        void parse() throw(bridge_exception);

        void build_to_string_func();

        void build_internal_func(string name, vector<string> vec_param, AstTreePtr body);

        void reg_func(string func_name, func_def_ptr func_def);

    private:
        static func_def_map_ptr s_map_reg_func_def;
        static int s_bridge_id_index;

    private:
        int _id;
        LexerPtr _lexer;
        ParserPtr _parser;
        AstTreeVecPtr _vec_statement;
        func_def_map_ptr _map_func_def;

        EnvironmentPtr _env;
    };

    typedef shared_ptr<Bridge> BridgePtr;
}

#endif //HELLOANDROID_BRIDGE_H
