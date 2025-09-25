#include <jni.h>
#include <sys/mman.h>
#include "../cmemory.hpp"

extern "C"
JNIEXPORT jobject JNICALL
Java_com_cws_kmemory_CMemory_malloc(JNIEnv* env, jobject thiz, jint size) {
    void* ptr = cmemory::malloc(size);
    if (!ptr) return nullptr;
    return env->NewDirectByteBuffer(ptr, size);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_cws_kmemory_CMemory_free(JNIEnv* env, jobject thiz, jobject buffer) {
    void* ptr = env->GetDirectBufferAddress(buffer);
    cmemory::free(ptr);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_cws_kmemory_CMemory_realloc(JNIEnv* env, jobject thiz, jobject buffer, jint size) {
    void* oldPtr = env->GetDirectBufferAddress(buffer);
    void* ptr = nullptr;
    jobject newBuffer = nullptr;

    if (oldPtr) {
        ptr = cmemory::realloc(oldPtr, size);
    } else {
        ptr = cmemory::malloc(size);
    }

    if (ptr) {
        newBuffer = env->NewDirectByteBuffer(ptr, size);
    }

    return newBuffer;
}