//
// Created by Administrator on 2020/4/23.
//

#ifndef HELLOANDROID_ASTTREE_H
#define HELLOANDROID_ASTTREE_H

#include "Token.h"

namespace bridge {

    class AstTree {

    };

    typedef shared_ptr<AstTree> AstTreePtr;
    typedef shared_ptr<vector<AstTreePtr>> AstTreeVecPtr;

    class AstLeaf: public AstTree {
    public:
        AstLeaf(TokenPtr token) {
            _token = token;
        }

    private:
        TokenPtr _token;
    };

    typedef shared_ptr<AstLeaf> AstLeafPtr;
    typedef shared_ptr<vector<AstLeafPtr>> AstLeafVecPtr;
}

#endif //HELLOANDROID_ASTTREE_H
