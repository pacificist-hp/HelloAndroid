#include <jni.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <android/log.h>

#define TAG "NativeService"
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__);

#ifdef USE_ONLOAD
static jint com_android_pacificist_helloandroid_NativeService_start(
        JNIEnv* env, jobject thiz, jstring params) {
    ALOGD("onload:com_android_pacificist_helloandroid_NativeService_start");
#else
extern "C" {
JNIEXPORT jint JNICALL Java_com_android_pacificist_helloandroid_NativeService__1start(
        JNIEnv* env, jobject thiz, jstring params) {
    ALOGD("export:JNICALL Java_com_android_pacificist_helloandroid_NativeService__1start");
#endif

    const char *str = env->GetStringUTFChars(params, NULL);
    ALOGD("native: %s", str);
    env->ReleaseStringUTFChars(params, str);

    return 0;
}


#ifndef USE_ONLOAD
}
#else
static JNINativeMethod nativeMethods[] = {
    {"_start", "(Ljava/lang/String;)I", (void *) com_android_pacificist_helloandroid_NativeService_start}
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
