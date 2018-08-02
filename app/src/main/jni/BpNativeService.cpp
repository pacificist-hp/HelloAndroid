#include <binder/Parcel.h>
#include "BpNativeService.h"

namespace android {
status_t BpNativeService::print(const char *message) {
    Parcel data, reply;
    data.writeInterfaceToken(IHelloNativeService::getInterfaceDes);
    data.writeCString(str);
    status_t status = remote()->transact(HW_PRINT, data, &reply);
    if (status != NO_ERROR) {
    	printf("call native print error: %s\n", strerror(-status));
    } else {
    	status = reply.readInt32();
    }
    return status;
}

BpNativeService:BpNativeService(const sp<IBinder> &impl): BpInterface<INativeService(impl)>{}
}
