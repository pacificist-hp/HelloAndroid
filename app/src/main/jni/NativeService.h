#ifndef HELLOANDROID_NATIVESERVICE_H
#define HELLOANDROID_NATIVESERVICE_H

#include "BnHelloNativeService.h"

namespace android {
class NativeService: public BnNativeService {
public:
    static void instantiate();
    virtual status_t print(const char *message);
    virtual status_t onTransact(uint32_t code, const Parcel &data, Parcel *reply, uint32_t flags);
private:
    NativeService();
    virtual ~NativeService();
};
};

#endif //HELLOANDROID_NATIVESERVICE_H
