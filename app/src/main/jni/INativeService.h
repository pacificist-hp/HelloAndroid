#ifndef HELLOANDROID_INATIVESERVICE_H
#define HELLOANDROID_INATIVESERVICE_H

#include <binder/IInterface.h>

namespace android {
    enum {
        HW_PRINT = IBinder::FIRST_CALL_TRANSACTION
    };

    class INativeService: public IInterface {
        public:
        	DECLARE_META_INTERFACE(NativeService);

        	virtual status_t print(const char *message) = 0;
    };
};

#endif //HELLOANDROID_INATIVESERVICE_H
