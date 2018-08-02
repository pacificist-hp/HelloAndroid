#ifndef HELLOANDROID_BNNATIVESERVICE_H
#define HELLOANDROID_BNNATIVESERVICE_H

#include <binder/Parcel.h>
#include "INativeService.h"

namespace android {
    class BnNativeService: public BnInterface<INativeService> {
    public:
        virtual status_t onTransact(uint32_t code, const Parcel &data, Parcel *reply, uint32_t flags = 0);
    };
};

#endif //HELLOANDROID_BNNATIVESERVICE_H
