//
// Created by Administrator on 2020/4/16.
//

#ifndef HELLOANDROID_BRIDGE_H
#define HELLOANDROID_BRIDGE_H

#include "../interpreter/Environment.h"
#include "../interpreter/Function.h"
#include "../interpreter/Lexer.h"
#include "../interpreter/Parser.h"

namespace bridge {

    class Bridge {
    public:
        Bridge();

        ~Bridge();

    public:
        static void
        register_function(const char *func_name, int param_num, OUTER_FUNC_CALLBACK outer_func);

    public:
        int get_id();

        int load_code(const char *code) throw(BridgeException);

        BridgeValue
        invoke(string func_name, BridgeValue args[], int args_num) throw(BridgeException);

    private:
        static FuncPtr
        create_function(string func_name, vector<string> param_name, AstTreePtr body);

    private:
        int load(ReaderPtr reader) throw(BridgeException);

        void parse() throw(BridgeException);

    private:
        static FuncMapPtr s_map_reg_func_def;
        static int s_bridge_id_index;

    private:
        int _id;
        LexerPtr _lexer;
        ParserPtr _parser;
        AstTreeVecPtr _vec_statement;
        FuncMapPtr _map_func_def;

        EnvironmentPtr _env;
    };

    typedef shared_ptr<Bridge> BridgePtr;
}

#endif //HELLOANDROID_BRIDGE_H
