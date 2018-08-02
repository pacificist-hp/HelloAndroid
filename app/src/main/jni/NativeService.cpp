#include <stdio.h>
#include <binder/IserviceManager.h>
#include <binder/IPCThreadState.h>

#include "BnNativeService.h"
#include "NativeService.h"

namespace android {
void NativeService::instantiate() {
    defaultServiceManager()->addService(String16("android.pacificist.INativeService"), new NativeService());
}

status_t NativeService::print(const char *message) {
    printf("print in NativeService: message\n");
    return NO_ERROR;
}

NativeService::NativeService() {
    printf("NativeService crated\n");
}

NativeService::~NativeService() {
    printf("NativeService destroyed\n");
}

status_t NativeService::onTransate(uint32_t code, const Parcel &data, Parcel *reply, uint32_t flags) {
    return BnNativeService::onTransate(code, data, reply, flags);
}
};
