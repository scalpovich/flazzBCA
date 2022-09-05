#include <jni.h>
#include "BCAdllC.h"
#include <string>
#include <stdio.h>
#include <string.h>
#include <android/log.h>
#include <malloc.h>

#define TAG "Logc_native"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_ndp_flazzbca_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

unsigned char BCAsendContactlessAPDU(unsigned char* cmdBYTE_BCA, unsigned short lenC, unsigned char* RAPDU, unsigned short* lenR)
{
    LOGD("cmdBYTE_BCA:");
    for (int i=0; i< lenC;i++)
    {
        LOGD("%02x", cmdBYTE_BCA[i]);
    }

    return false;
}
unsigned char BCAsendContactAPDU(int SAMSlotNo, unsigned char* cmdBYTE_BCA, unsigned short lenC, unsigned char* RAPDU, unsigned short* lenR)
{
    return false;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ndp_flazzbca_library_FlazzLib_BCAVersionDLL(JNIEnv *env, jobject thiz) {
    unsigned char strLogResponse[255];
    memset(strLogResponse, 0x00, sizeof(strLogResponse));

    unsigned char result = BCAVersionDll(strLogResponse);
    return (env)->NewStringUTF(reinterpret_cast<const char *>(strLogResponse));
}

//extern "C"
//JNIEXPORT jobject JNICALL
//Java_com_ndp_flazzbca_library_FlazzLib_cekSaldo(JNIEnv *env, jobject thiz, jlong Balance, jstring cardNo, jstring log) {
//    long balance;
//    unsigned char Cardno[255];
//    unsigned char strLogResponse[255];
//    memset(strLogResponse, 0x00, sizeof(strLogResponse));
//    memset(Cardno, 0x00, sizeof(Cardno));
//
//    unsigned char result = BCACheckBalance(reinterpret_cast<long *>(balance), Cardno, strLogResponse);
//    jobjectArray retobjarr = (jobjectArray)env->NewObjectArray(2, env->FindClass("java/lang/Object"), NULL);
//    env->SetObjectArrayElement(retobjarr, 0, env->NewStringUTF(
//            reinterpret_cast<const char *>(balance)));
//    env->SetObjectArrayElement(retobjarr, 1, env->NewStringUTF(
//            reinterpret_cast<const char *>(Cardno)));
//    env->SetObjectArrayElement(retobjarr, 2, env->NewStringUTF(
//            reinterpret_cast<const char *>(strLogResponse)));
//
//    return retobjarr;
//
//}


extern "C" JNIEXPORT jstring JNICALL
Java_com_ndp_flazzbca_library_FlazzLib_cekSaldo(JNIEnv *env, jobject javaThis)
{
    long* balance = -1;
    char sbalance[255];
    unsigned char Cardno[255];
    unsigned char strLogResponse[255];
    memset(strLogResponse, 0x00, sizeof(strLogResponse));
    memset(Cardno, 0x00, sizeof(Cardno));

    unsigned char result = BCACheckBalance(balance, Cardno, strLogResponse);
    if (result)
    LOGD("cmdBYTE_BCsssA:");

    sprintf(sbalance, "%lu", balance);

    return (env)->NewStringUTF(reinterpret_cast<const char *>(balance));
}