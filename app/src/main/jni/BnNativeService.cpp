#include <binder/Parcel.h>
#include "BnNativeService.h"

namespace android {
    status_t onTransact(uint32_t code, const Parcel &data, Parcel *reply, uint32_t flags = 0) {
        switch (code) {
            case HW_PRINT:
                CHECK_INTERFACE(INativeService, data, reply);
                const char *message;
                message = data.readCString();
                reply->writeInt32(print(message));
                return NO_ERROR;
                break;
            default:
                return BBinder::onTransact(code, data, reply, flags);
        }
    }
};