LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := native_service
LOCAL_SRC_FILES := com_android_pacificist_helloandroid_NativeService.cpp
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog
LOCAL_CFLAGS += -DUSE_ONLOAD

include $(BUILD_SHARED_LIBRARY)