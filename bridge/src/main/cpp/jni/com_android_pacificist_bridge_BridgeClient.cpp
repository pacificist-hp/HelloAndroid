//
// Created by Administrator on 2020/4/16.
//
#include <jni.h>
#include <string>

#include "../include/common.h"
#include "../include/Manager.h"

extern "C" {

static JavaVM *g_vm;

static void native_register_function(JNIEnv* env, jobject thiz, jstring jname, jint jparam_num) {
    const char* name = env->GetStringUTFChars(jname, 0);
    LOGD("native_register_function: %s, %d", name, jparam_num);
    bridge::Manager::register_function(name, jparam_num);
    env->ReleaseStringUTFChars(jname, name);
}

static const JNINativeMethod nativeMethods[] = {
        {"nativeRegisterFunction", "(Ljava/lang/String;I)V", (void *) native_register_function}
};

static int registerNativeMethods(JNIEnv* env) {
    int result = -1;

    /* look up the class */
    jclass clazz = env->FindClass("com/android/pacificist/bridge/BridgeClient");

    if (NULL != clazz) {
        if (env->RegisterNatives(clazz, nativeMethods,
                sizeof(nativeMethods) / sizeof(nativeMethods[0])) == JNI_OK) {
            result = 0;
        }
    }
    return result;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void * /* reserved */) {
    g_vm = vm;

    JNIEnv *env = NULL;
    jint result = -1;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) == JNI_OK) {
        if (NULL != env && registerNativeMethods(env) == 0) {
            result = JNI_VERSION_1_6;
        }
    }

    return result;
}

}
