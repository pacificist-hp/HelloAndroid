//
// Created by Administrator on 2020/5/20.
//

#ifndef HELLOANDROID_LITERAL_H
#define HELLOANDROID_LITERAL_H

#include "AstTree.h"

namespace bridge {

    class Literal : public AstLeaf {
    public:
        Literal(TokenPtr token) : AstLeaf(token) {}

    protected:
        virtual void check_token(TokenPtr token) throw(BridgeException) = 0;
    };

    class IntLiteral : public Literal {
    public:
        IntLiteral(TokenPtr token) throw(BridgeException) : Literal(token) {
            check_token(token);
        }

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            BridgeValue v;
            v._type = INT;
            v._int = (get_token()->get_integer());

            return v;
        }

    protected:
        virtual void check_token(TokenPtr token) throw(BridgeException) {
            if (token == nullptr || token->get_type() != TYPE_INTEGER) {
                throw BridgeException("IntLiteral token error");
            }
        }
    };

    class FloatLiteral : public Literal {
    public:
        FloatLiteral(TokenPtr token) throw(BridgeException) : Literal(token) {
            check_token(token);
        }

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            BridgeValue v;
            v._type = FLOAT;
            v._float = (get_token()->get_float());

            return v;
        }

    protected:
        virtual void check_token(TokenPtr token) throw(BridgeException) {
            if (token == nullptr || token->get_type() != TYPE_FLOAT) {
                throw BridgeException("FloatLiteral token error");
            }
        }
    };

    class BoolLiteral : public Literal {
    public:
        BoolLiteral(TokenPtr token) throw(BridgeException) : Literal(token) {
            check_token(token);
        }

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            BridgeValue v;
            v._type = BOOL;
            v._bool = (get_token()->get_bool());

            return v;
        }

    protected:
        virtual void check_token(TokenPtr token) throw(BridgeException) {
            if (token == nullptr || token->get_type() != TYPE_BOOL) {
                throw BridgeException("BoolLiteral token error");
            }
        }
    };

    class TextLiteral : public Literal {
        public:
            TextLiteral(TokenPtr token) throw(BridgeException) : Literal(token) {
                check_token(token);
            }

            virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
                BridgeValue v;
                v._type = STRING;
                v._string = *(get_token()->get_text());

                return v;
            }

        protected:
            virtual void check_token(TokenPtr token) throw(BridgeException) {
            if (token == nullptr || token->get_type() != TYPE_TEXT) {
                throw BridgeException("TextLiteral token error");
            }
        }
    };

    class VarLiteral : public Literal {
    public:
        VarLiteral(TokenPtr var_name) : Literal(var_name) {
            check_token(var_name);
        }

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            StringPtr name = (get_token()->get_identifier());
            if (name != nullptr) {
                return env->get(*name);
            }

            throw BridgeException("VarLiteral: variable is null");
        }

        void assign(EnvironmentPtr &env, BridgeValue value) throw(BridgeException) {
            StringPtr name = (get_token()->get_identifier());
            if (name != nullptr) {
                env->put(*name, value);
            }
        }

        void define(EnvironmentPtr &env, BridgeValue value) {
            StringPtr name = (get_token()->get_identifier());
            if (name != nullptr) {
                env->set(*name, value);
            }
        }

    protected:
        virtual void check_token(TokenPtr token) throw(BridgeException) {
            if (token == nullptr || token->get_type() != TYPE_IDENTIFIER) {
                throw BridgeException("VarLiteral: variable's name error");
            }
        }
    };

    typedef shared_ptr<VarLiteral> VarLiteralPtr;

    class NullLiteral : public Literal {
    public:
        NullLiteral(TokenPtr token) : Literal(token) {
            check_token(token);
        }

        virtual BridgeValue evaluate(EnvironmentPtr &env) throw(BridgeException) {
            return BridgeValue();
        }

    protected:
        virtual void check_token(TokenPtr token) throw(BridgeException) {
            if (token == nullptr || token->get_type() != TYPE_NONE) {
                throw BridgeException("NullLiteral token error");
            }
        }
    };
}

#endif //HELLOANDROID_LITERAL_H
