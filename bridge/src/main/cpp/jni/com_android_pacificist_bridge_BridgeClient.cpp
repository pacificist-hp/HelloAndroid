//
// Created by Administrator on 2020/4/16.
//
#include <jni.h>
#include <string>

#include "../include/common.h"
#include "../include/Manager.h"

extern "C" {

jclass BRIDGE_CLIENT_CLASS;
jclass BRIDGE_VALUE_CLASS;

jmethodID BRIDGE_VALUE_INIT_STRING_METHOD;
jmethodID BRIDGE_VALUE_INIT_VOID_METHOD;

jmethodID BRIDGE_CLIENT_CALLBACK_METHOD;

jfieldID BRIDGE_VALUE_TYPE_FIELD;
jfieldID BRIDGE_VALUE_STRING_FIELD;

/* JavaVM is valid globally */
static JavaVM *g_vm = NULL;

/* JNIEnv is thread related */
static JNIEnv *get_env() {
    JNIEnv *env = NULL;
    if (NULL != g_vm && g_vm->GetEnv((void **) &env, JNI_VERSION_1_6) == JNI_OK) {
        return env;
    }

    return NULL;
}

static jobject convert_to_jbridge_value(JNIEnv *env, bridge::bridge_value value) {
    jobject jvalue = NULL;

    switch (value._type) {
        case bridge::STRING: {
            jstring str = env->NewStringUTF(value._string.c_str());
            jvalue = env->NewObject(BRIDGE_VALUE_CLASS, BRIDGE_VALUE_INIT_STRING_METHOD, str);
            env->DeleteLocalRef(str);
            break;
        }
        default:
            jvalue = env->NewObject(BRIDGE_VALUE_CLASS, BRIDGE_VALUE_INIT_VOID_METHOD);
            break;
    }

    return jvalue;
}

static bridge::bridge_value convert_to_bridge_value(JNIEnv *env, jobject jvalue) {
    bridge::bridge_value value;

    if (NULL != jvalue) {
        jint jtype = env->GetIntField(jvalue, BRIDGE_VALUE_TYPE_FIELD);
        switch (jtype) {
            case 2: {
                value._type = bridge::STRING;
                jstring jstr = (jstring) env->GetObjectField(jvalue, BRIDGE_VALUE_STRING_FIELD);
                const char *str = env->GetStringUTFChars(jstr, 0);
                value._string = str;
                env->ReleaseStringUTFChars(jstr, str);
                break;
            }
            default:
                break;
        }
    }

    return value;
}

static bridge::bridge_value
outer_func_def(int bridge_id, int eval_id, const char *name, bridge::bridge_value *params,
               int param_num) {
    JNIEnv *env = get_env();
    if (NULL == env) {
        return bridge::bridge_value();
    }

    jstring jname = env->NewStringUTF(name);

    jobjectArray jparams = NULL;
    if (param_num > 0) {
        jparams = (jobjectArray) env->NewObjectArray(param_num, BRIDGE_VALUE_CLASS, NULL);

        for (int i = 0; i < param_num; i++) {
            env->SetObjectArrayElement(jparams, i, convert_to_jbridge_value(env, params[i]));
        }
    }

    jobject jvalue = env->CallStaticObjectMethod(BRIDGE_CLIENT_CLASS,
                                                 BRIDGE_CLIENT_CALLBACK_METHOD,
                                                 jname, jparams);
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();

        return bridge::bridge_value();
    }

    bridge::bridge_value value = convert_to_bridge_value(env, jvalue);

    env->DeleteLocalRef(jname);
    env->DeleteLocalRef(jparams);
    env->DeleteLocalRef(jvalue);

    return value;
}

static jint native_init(JNIEnv *env, jclass clazz) {
    LOGD("native_init");

    BRIDGE_CLIENT_CLASS = (jclass) env->NewGlobalRef(
            env->FindClass("com/android/pacificist/bridge/BridgeClient"));
    BRIDGE_VALUE_CLASS = (jclass) env->NewGlobalRef(
            env->FindClass("com/android/pacificist/bridge/Value"));

    BRIDGE_VALUE_INIT_STRING_METHOD = env->GetMethodID(BRIDGE_VALUE_CLASS, "<init>",
                                                       "(Ljava/lang/String;)V");
    BRIDGE_VALUE_INIT_VOID_METHOD = env->GetMethodID(BRIDGE_VALUE_CLASS, "<init>", "()V");

    BRIDGE_CLIENT_CALLBACK_METHOD = env->GetStaticMethodID(BRIDGE_CLIENT_CLASS, "callback",
                                                           "(Ljava/lang/String;[Lcom/android/pacificist/bridge/Value;)Lcom/android/pacificist/bridge/Value;");

    BRIDGE_VALUE_TYPE_FIELD = env->GetFieldID(BRIDGE_VALUE_CLASS, "type", "I");
    BRIDGE_VALUE_STRING_FIELD = env->GetFieldID(BRIDGE_VALUE_CLASS, "stringVal",
                                                "Ljava/lang/String;");

    return BRIDGE_VERSION_CODE;
}

static void native_register_function(JNIEnv *env, jobject thiz, jstring jname, jint jparam_num) {
    const char *name = env->GetStringUTFChars(jname, 0);
    LOGD("native_register_function: %s, %d", name, jparam_num);
    bridge::Manager::register_function(name, jparam_num, outer_func_def);
    env->ReleaseStringUTFChars(jname, name);
}

static jint native_load_code(JNIEnv *env, jobject thiz, jstring jcode) {
    LOGD("native_load_code");
    const char *code = env->GetStringUTFChars(jcode, 0);

    int id = -1;
    try {
        id = bridge::Manager::load_code(code);
    } catch (bridge::bridge_exception &e) {
        LOGD("exception in native_load_code: %s", e._msg.c_str());
        jclass clazz = env->FindClass("java/lang/Exception");
        env->ThrowNew(clazz, e._msg.c_str());
    }

    env->ReleaseStringUTFChars(jcode, code);

    return id;
}

static jobject
native_invoke(JNIEnv *env, jobject thiz, jint jbridge_id, jstring jname, jobjectArray jargs) {
    const char *name = env->GetStringUTFChars(jname, 0);
    LOGD("native_invoke: %s", name);

    bridge::bridge_value v;

    bridge::bridge_value *args = NULL;
    int len = NULL == jargs ? 0 : env->GetArrayLength(jargs);
    if (len > 0) {
        args = new bridge::bridge_value[len];
        for (int i = 0; i < len; i++) {
            args[i] = convert_to_bridge_value(env, env->GetObjectArrayElement(jargs, i));
        }
        v = bridge::Manager::invoke(name, args, len);
        delete[] args;
    } else {
        v = bridge::Manager::invoke(name, NULL, 0);
    }

    env->ReleaseStringUTFChars(jname, name);

    jobject jvalue = convert_to_jbridge_value(env, v);

    return jvalue;
}

static void native_release(JNIEnv *env, jobject thiz, jint jbridge_id) {
    LOGD("native_release: %d", jbridge_id);
    bridge::Manager::release(jbridge_id);
}

static const JNINativeMethod nativeMethods[] = {
        {"nativeInit",             "()I",                                                        (jint *) native_init},
        {"nativeRegisterFunction", "(Ljava/lang/String;I)V",                                     (void *) native_register_function},
        {"nativeLoadCode",         "(Ljava/lang/String;)I",                                      (jint *) native_load_code},
        {"nativeInvoke",           "(ILjava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", (jobject *) native_invoke},
        {"nativeRelease",          "(I)V",                                                       (void *) native_release}

};

static int registerNativeMethods(JNIEnv *env) {
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
    jint result = -1;

    g_vm = vm;
    JNIEnv *env = get_env();
    if (NULL != env && registerNativeMethods(env) == 0) {
        result = JNI_VERSION_1_6;
    }

    return result;
}

}
