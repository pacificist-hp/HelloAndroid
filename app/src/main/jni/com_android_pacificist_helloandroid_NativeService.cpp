#include <jni.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <android/log.h>

#include <binder/IPCThreadState.h>
#include <binder/ProcessState.h>
#include <binder/IServiceManager.h>

#include "NativeService.h"

#define TAG "NativeService"
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__);

#ifndef USE_ONLOAD
#include "com_android_pacificist_helloandroid_NativeService.h"
#endif

using namesapce android;

#ifdef USE_ONLOAD
static jint com_android_pacificist_helloandroid_NativeService_start(
        JNIEnv* env, jobject thiz, jstring params) {
    ALOGD("com_android_pacificist_helloandroid_NativeService_start");
#else
extern "C" {
JNIEXPORT jint JNICALL Java_com_android_pacificist_helloandroid_NativeService__1start(
        JNIEnv* env, jclass clazz, jstring params) {
    ALOGD("Java_com_android_pacificist_helloandroid_NativeService__1start");
#endif
    const char *str = env->GetStringUTFChars(params, NULL);
    ALOGD("native start: %s", str);

    NativeService::instantiate();
    ProcessState::self()->startThreadPool();
    printf("NativeService is starting now");
    IPCThreadState::self()->joinThreadPool();

    env->ReleaseStringUTFChars(params, str);
    return 0;
}
#ifndef USE_ONLOAD
}
#endif

#ifdef USE_ONLOAD
static jint com_android_pacificist_helloandroid_NativeService_print(
        JNIEnv* env, jobject thiz, jstring message) {
    ALOGD("com_android_pacificist_helloandroid_NativeService_print");
#else
extern "C" {
JNIEXPORT jint JNICALL Java_com_android_pacificist_helloandroid_NativeService__1print(
        JNIEnv* env, jclass clazz, jstring message) {
    ALOGD("Java_com_android_pacificist_helloandroid_NativeService__1print");
#endif
    const char *str = env->GetStringUTFChars(message, NULL);
    ALOGD("native print: %s", str);

    sp<IServiceManager> sm = defaultServiceManager();
    sp<IBinder> b;
    sp<INativeService> sNativeService;

    do {
        b = sm->getService(String16("android.pacificist.INativeService"));
        if (0 != b)
        	break;
        printf("NativeService is not working, waiting...\n");
        usleep(500000);
    } while (true)

    sNativeService = interface_cast<INativeService>(b);
    sNativeService->print(str);

    env->ReleaseStringUTFChars(message, str);
    return 0;
}

#ifndef USE_ONLOAD
}
#else
static JNINativeMethod nativeMethods[] = {
    {"_start", "(Ljava/lang/String;)I", (void *) com_android_pacificist_helloandroid_NativeService_start},
    {"_print", "(Ljava/lang/String;)I", (void *) com_android_pacificist_helloandroid_NativeService_print}
};

static int registerNativeMethods(JNIEnv *env) {
    int result = -1;

    /* look up the class */
    jclass clazz = env->FindClass("com/android/pacificist/helloandroid/NativeService");

    if (NULL != clazz) {
        if (env->RegisterNatives(clazz, nativeMethods,
                        sizeof(nativeMethods) / sizeof(nativeMethods[0])) == JNI_OK) {
            result = 0;
        }
    }
    return result;
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env = NULL;
    jint result = -1;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) == JNI_OK) {
        if (NULL != env && registerNativeMethods(env) == 0) {
            result = JNI_VERSION_1_4;
        }
    }
    return result;
}
#endif
