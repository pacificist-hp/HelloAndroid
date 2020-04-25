//
// Created by Administrator on 2020/4/23.
//

#ifndef HELLOANDROID_AST_H
#define HELLOANDROID_AST_H

#include "token.h"

namespace bridge {

    class ast_tree {

    };

    typedef shared_ptr<ast_tree> ast_tree_ptr;

    class ast_leaf: public ast_tree {
    public:
        ast_leaf(token_ptr token) {
            _token = token;
        }

    private:
        token_ptr _token;
    };

    typedef shared_ptr<ast_leaf> ast_leaf_ptr;
    typedef shared_ptr<vector<ast_leaf_ptr>> ast_leaf_vec_ptr;
}

#endif //HELLOANDROID_AST_H
