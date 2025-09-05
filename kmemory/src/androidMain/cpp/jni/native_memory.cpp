#include <jni.h>
#include <cstdlib>
#include <sys/mman.h>

extern "C"
JNIEXPORT jobject JNICALL
Java_com_cws_kmemory_Memory_nativeAlloc(JNIEnv* env, jobject thiz, jint size) {
    void* ptr = malloc(size);
    if (!ptr) return nullptr;
    return env->NewDirectByteBuffer(ptr, size);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_cws_kmemory_Memory_nativeFree(JNIEnv* env, jobject thiz, jobject buffer) {
    void* ptr = env->GetDirectBufferAddress(buffer);
    free(ptr);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_cws_kmemory_Memory_nativeRealloc(JNIEnv* env, jobject thiz, jobject buffer, jint size) {
    void* oldPtr = env->GetDirectBufferAddress(buffer);
    void* ptr = nullptr;
    jobject newBuffer = nullptr;

    if (oldPtr) {
        ptr = realloc(oldPtr, size);
    } else {
        ptr = malloc(size);
    }

    if (ptr) {
        newBuffer = env->NewDirectByteBuffer(ptr, size);
    }

    return newBuffer;
}