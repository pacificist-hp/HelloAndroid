#ifndef HELLOANDROID_BPNATIVESERVICE_H
#define HELLOANDROID_BPNATIVESERVICE_H

#include <binder/Parcel.h>
#include "INativeService.h"

namespace android {
class BpNativeService: public BpInterface<INativeService> {
public:
    BpNativeService(const sp<IBinder> &impl);
    virtual status_t print(const char *message);
};
};

#endif //HELLOANDROID_BPNATIVESERVICE_H
