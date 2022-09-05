#include "BCAdllC.h"
#include <jni.h>
#include <string.h>
#include <malloc.h>
#include <android/log.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdio.h>

#define TAG "MY Wrapper LOG"
#define  LOGV(...)  __android_log_print(ANDROID_LOG_VERBOSE,    TAG, __VA_ARGS__)
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN,       TAG, __VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,      TAG, __VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,       TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,      TAG, __VA_ARGS__)

#define JNI_METHOD(RETURN, METHOD_NAME) JNIEXPORT RETURN JNICALL Java_com_ndp_flazzbca_library_FlazzLib_##METHOD_NAME

int LENGTH_BCALIBVER = 255;
int LENGTH_RESPONSE = 255;
int LENGTH_BCAISMYCARD_RESULT = 255;

static JavaVM *jvm;
JNIEnv *g_env;
jobject g_obj;

//Convert BIN string to readable HEX string, which have double length of BIN string. 0x12AB-->"12AB"
void PubBcd2Asc(unsigned char *psIn, unsigned int uiLength, unsigned char *psOut)
{
    static const unsigned char ucHexToChar[16] = {"0123456789ABCDEF"};
    unsigned int   uiCnt;

    if ((psIn == NULL) || (psOut == NULL))
    {
        return;
    }

    for(uiCnt = 0; uiCnt < uiLength; uiCnt++)
    {
        psOut[2*uiCnt]   = ucHexToChar[(psIn[uiCnt] >> 4)];
        psOut[2*uiCnt + 1] = ucHexToChar[(psIn[uiCnt] & 0x0F)];
    }
}

//Similar with function PubOne2Two(), and add '\0' at the end of object string
void PubBcd2Asc0(unsigned char *psIn, uint uiLength, unsigned char *pszOut)
{
    if ((psIn == NULL) || (pszOut == NULL))
    {
        return;
    }

    PubBcd2Asc(psIn, uiLength, pszOut);
    pszOut[2*uiLength] = 0;
}

//Convert readable HEX string to BIN string, which only half length of HEX string. "12AB"-->0x12AB
void PubAsc2Bcd(unsigned char *psIn, unsigned int uiLength, unsigned char *psOut)
{
    unsigned char   tmp;
    unsigned int    i;

    if ((psIn == NULL) || (psOut == NULL))
    {
        return;
    }

    for(i = 0; i < uiLength; i += 2)
    {
        tmp = psIn[i];
        if( tmp > '9' )
        {
            tmp = (unsigned char)toupper((int)tmp) - 'A' + 0x0A;
        }
        else
        {
            tmp &= 0x0F;
        }
        psOut[i / 2] = (tmp << 4);

        tmp = psIn[i+1];
        if( tmp > '9' )
        {
            tmp = toupper((char)tmp) - 'A' + 0x0A;
        }else
        {
            tmp &= 0x0F;
        }
        psOut[i/2] |= tmp;
    }
}

//convert long to char
void PubLong2Char(unsigned long ulSource, unsigned int uiTCnt, unsigned char *psTarget)
{
    unsigned int    i;

    for(i = 0; i < uiTCnt; i++)
    {
        psTarget[i] = (unsigned char)(ulSource >> (8 * (uiTCnt - i - 1)));
    }
}

JNI_METHOD(jint , init)( JNIEnv* env, jobject thisObj, jstring SAMS){
//    int status = (*env)->GetJavaVM(env, &jvm);
    g_env = env;
    g_obj = thisObj;
    unsigned char strLogResponse[LENGTH_RESPONSE];
    memset(strLogResponse, 0x00, sizeof(strLogResponse));

    unsigned char samSl[LENGTH_RESPONSE];
    memset(samSl, 0x00, sizeof(samSl));

    LOGD("SAMS old %s", SAMS);

    unsigned char *sams = (*env)->GetStringUTFChars(env, SAMS, NULL);
    LOGD("SAMS new %s", sams);

    PubAsc2Bcd(sams, 2, samSl);
    LOGD("sams %02x", sams);
    LOGD("Afsams %02x", samSl);

    unsigned char result= BCAInitialSAM((int)samSl, strLogResponse);

    if(result == FALSE){
        LOGD("result init SAMS False");
        return 1;
    }else{
        LOGD("result init SAMS True");
        return 0;

    }


}

JNI_METHOD(jstring ,BCAVersionDllw) ( JNIEnv* env, jobject thisObj){

    unsigned char strLogResponse[LENGTH_BCALIBVER];
    memset(strLogResponse, 0x00, sizeof(strLogResponse));

    unsigned char result = BCAVersionDll(strLogResponse);
    LOGD("response version dll : %s",strLogResponse);
    return (*env)->NewStringUTF(env,strLogResponse);
}

JNI_METHOD(jstring ,BCAVersionDll) ( JNIEnv* env, jobject thisObj){

    unsigned char strLogResponse[LENGTH_BCALIBVER];
    memset(strLogResponse, 0x00, sizeof(strLogResponse));

    unsigned char result = BCAVersionDll(strLogResponse);
    LOGD("response version dll : %s",strLogResponse);
    return (*env)->NewStringUTF(env,strLogResponse);
}

JNI_METHOD(jstring ,BCASetConfig) ( JNIEnv* env, jobject thisObj, jstring jstrConfig){
    const char *strConfig=(*env)->GetStringUTFChars(env, jstrConfig,0);
    unsigned char strLogResponse[LENGTH_RESPONSE];
    int iRet=0;
    memset(strLogResponse, 0x00, sizeof(strLogResponse));

    LOGD("Data %s", strConfig);
    unsigned char result = BCASetConfig(strConfig, strLogResponse);
    LOGD("%s",strLogResponse);

//    if(result==1) {
//        if (memcmp(strLogResponse, "0000", 4) == 0)
//            iRet = 1;
//    }

    if(result == FALSE) {
        if (strcmp(strLogResponse, "8211") == 0 ) {
            return (*env)->NewStringUTF(env, "Terminal ID or Merchant ID is NOT Alpha Numeric format");
        }else if (strcmp(strLogResponse, "8215") == 0 ) {
            return (*env)->NewStringUTF(env, "Terminal ID Invalid");
        }else if (strcmp(strLogResponse, "8216") == 0 ) {
            return (*env)->NewStringUTF(env, "Merchant ID Invalid");
        }
    }else{
        return (*env)->NewStringUTF(env, strLogResponse);
    }

//    (*env)->NewStringUTF(env, strConfig);
//    LOGD("%s","ReleaseStringUTFChars");
//    return iRet;
    return (*env)->NewStringUTF(env, strLogResponse);
}

JNI_METHOD(jstring,BCAGetConfig) (JNIEnv* env, jobject thisObj){
    unsigned char strConfig[LENGTH_STR_CONFIG];
    memset(strConfig, 0x00, sizeof(strConfig));
    unsigned char strLogResponse[LENGTH_RESPONSE];
    memset(strLogResponse, 0x00, sizeof(strLogResponse));

    unsigned char result= BCAGetconfig(strConfig, strLogResponse);

    LOGD("strConfig %s", strConfig);
    LOGD("strLogResponse %s",strLogResponse);
    return (*env)->NewStringUTF(env,strConfig);
}

JNI_METHOD(jstring,BCAIsMyCard) (JNIEnv* env, jobject thisObj){
    g_env = env;
    g_obj = thisObj;

    unsigned char strLogResponse[LENGTH_BCAISMYCARD_RESULT];
    memset(strLogResponse, 0x00, sizeof(strLogResponse));

    unsigned char result= BCAIsMyCard(strLogResponse);
    if(result == FALSE){
        LOGD("result IsMyCard False");
    }else{
        LOGD("result IsMyCard True");
    }

    return (*env)->NewStringUTF(env, strLogResponse);
}

JNI_METHOD(jstring ,BCACheckBalance) (JNIEnv* env, jobject thisObj){
    g_env = env;
    g_obj = thisObj;

    long Balance = -1;
    unsigned char CardNo[LENGTH_STR_CARDNO];
    memset(CardNo, 0x00, sizeof(CardNo));
    unsigned char strLogResponse[LENGTH_RESPONSE];
    memset(strLogResponse, 0x00, sizeof(strLogResponse));

    unsigned char result= BCACheckBalance(&Balance,CardNo, strLogResponse);
    if(result == FALSE){
        LOGD("result check balance False");
        return -1;
    }else{
        LOGD("result check balance True");
    }
    LOGD("CardNo %s", CardNo);
    LOGD("Balance : %ld", Balance);
    LOGD("StrLogResponse %s", strLogResponse);

//    jclass capdu = (*env)->FindClass(env, "com/ndp/flazzbca/library/FlazzLib");
//    jmethodID midInit = (*env)->GetMethodID(env, capdu, "<init>", "()V");
//    jobject newObj = (*env)->NewObjectA(env, capdu, midInit, Balance);
//    jmethodID sendApdu = (*env)->GetMethodID(env, capdu, "setBalance", "(I)V");
//
//
//    (*env)->CallVoidMethod(env, newObj, sendApdu, Balance);

    char combine[256];
//    unsigned char *balance = (unsigned char*) &Balance;
    char str[100];
    sprintf(combine, "%lu", Balance);
    strcat(combine, ",");
    strcat(combine, CardNo);

//    strcpy(str, (const char *) Balance);
//    strcat(str, CardNo);
    LOGD("cek balance %s", combine);

    return (*env)->NewStringUTF(env, combine);
}

JNI_METHOD(jstring, BCADebitBalance) (JNIEnv* env, jobject thisObj,jstring SAMS, jstring curenDate, jstring Amount){

    const char *date = (*env)->GetStringUTFChars(env, curenDate, 0);
    const char *amount = (*env)->GetStringUTFChars(env, Amount, 0);
    unsigned char strLogRsp[LENGTH_RESPONSE];
    memset(strLogRsp, 0x00, sizeof(strLogRsp));
    long balBefore = 0;
    long balAfter = 0;
    unsigned char samSl[LENGTH_RESPONSE];
    memset(samSl, 0x00, sizeof(samSl));

    unsigned char *sams = (*env)->GetStringUTFChars(env, SAMS, NULL);

    PubAsc2Bcd(sams, 2, samSl);


    long amounts = atol(amount);
    LOGD("amount %ld", amounts);
    LOGD("sams %02x", sams);
    LOGD("Afsams %02x", samSl);
    LOGD("date %s", date);



    unsigned char result = BCADebitBalance((int) samSl, date, amounts, &balBefore, &balAfter, strLogRsp);
    LOGD("result ", result);
    LOGD("respon %s", strLogRsp);

    char subbuff[5];
    memcpy( subbuff, &strLogRsp[0], 4 );
    subbuff[4] = '\0';
    LOGD("respon %s", subbuff);

    if (result == FALSE){
        LOGD("Sale Gagal");
        if (strcmp(strLogRsp, "804F") == 0 ) {
            return (*env)->NewStringUTF(env, "Saldo Kurang");
        }
//        else if (strcmp(strLogRsp, "8208") == 0 ) {
//            return (*env)->NewStringUTF(env, "Saldo Kurang");
//        }
        else if(strcmp(subbuff, "8054") == 0) {
            return (*env)->NewStringUTF(env, "Loss Contact");
        }
//        else if(strcmp(subbuff, "8045") == 0) {
//            return (*env)->NewStringUTF(env, "Loss Contact");
//        }
        else{
//        }else if(strcmp(strLogRsp, "820A") != 0) {
//            return (*env)->NewStringUTF(env, "Loss Contact");
//        }else{
            return (*env)->NewStringUTF(env, "Abnormal");

        }
    }else{
        LOGD("Sale Berhasil");
    }
    LOGD("Balance : %ld", balAfter);

//    (*env)->ReleaseStringUTFChars(env, curenDate, date);
//    (*env)->ReleaseStringUTFChars(env, Amount, amount);

    LOGD("BalBefore %s", strLogRsp);
    return (*env)->NewStringUTF(env, strLogRsp);
}

unsigned char BCAsendContactlessAPDU(unsigned char* cmdBYTE_BCA, unsigned short lenC, unsigned char* RAPDU, unsigned short* lenR)
{
    unsigned char dataIn[lenC*2+1];
    memset(dataIn, 0x00, sizeof(dataIn));
    PubBcd2Asc(cmdBYTE_BCA, lenC, dataIn);

    LOGD("Contactless APDU %s",dataIn);

    jclass capdu = (*g_env)->FindClass(g_env, "com/ndp/flazzbca/library/FlazzLib");

    jmethodID midInit = (*g_env)->GetMethodID(g_env, capdu, "<init>", "()V");
    if (NULL == midInit) return NULL;

    jobject newObj = (*g_env)->NewObjectA(g_env, capdu, midInit, dataIn);

    jmethodID sendApdu = (*g_env)->GetMethodID(g_env, capdu, "CLessAPDU","(Ljava/lang/String;)Ljava/lang/String;");
    if (NULL == sendApdu) {
        (*g_env)->DeleteLocalRef(g_env, capdu);
        LOGW("can't find method getStringFromStatic from JniHandle ");
        return 0;
    }else{
        LOGD("Method find");
    }
    jstring dt = (*g_env)->NewStringUTF(g_env, dataIn);
    jstring results = (*g_env)->CallObjectMethod(g_env, newObj, sendApdu, dt);
    unsigned char *resultChar = (*g_env)->GetStringUTFChars(g_env, results, NULL);
    int lenA = strlen(resultChar);
    LOGD("%d", lenA);
    LOGD("%s resp", resultChar);

    PubAsc2Bcd(resultChar, lenA, RAPDU);
    *lenR = lenA/2;
    return TRUE;

}

unsigned char BCAsendContactAPDU(int SAMSlotNo, unsigned char* cmdBYTE_BCA, unsigned short lenC, unsigned char* RAPDU, unsigned short* lenR)
{
    unsigned char dataIn[lenC*2+1];
    memset(dataIn, 0x00, sizeof(dataIn));
    PubBcd2Asc(cmdBYTE_BCA, lenC, dataIn);


    unsigned char dataSlot[2];
    memset(dataSlot, 0x00, sizeof(dataSlot));
    PubLong2Char((unsigned long)SAMSlotNo, 1, dataSlot);

    unsigned char dtSlot[1];
    memset(dtSlot, 0x00, sizeof(dtSlot));
    PubBcd2Asc(dataSlot, 1, dtSlot);

    LOGD("Contact APDU %s",dataSlot);
    LOGD("Contact APDU1 %s",dtSlot);

    jclass clapdu = (*g_env)->FindClass(g_env, "com/ndp/flazzbca/library/FlazzLib");

    jmethodID midInit = (*g_env)->GetMethodID(g_env, clapdu, "<init>", "()V");
    if (NULL == midInit) return NULL;

    jobject newObj = (*g_env)->NewObjectA(g_env, clapdu, midInit, dataIn);

    jmethodID sendApdu = (*g_env)->GetMethodID(g_env, clapdu, "CAPDU",
                                               "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
//
//    if (NULL == sendApdu) {
//        (*g_env)->DeleteLocalRef(g_env, clapdu);
//        LOGW("can't find method getStringFromStatic from JniHandle ");
//        return 0;
//    }else{
//        LOGD("Method find");
//    }
//
    jstring dt = (*g_env)->NewStringUTF(g_env, dataIn);
    jstring dtSL = (*g_env)->NewStringUTF(g_env, dtSlot);
    jstring results = (*g_env)->CallObjectMethod(g_env, newObj, sendApdu, dt, dtSL);
    unsigned char *resultChar = (*g_env)->GetStringUTFChars(g_env, results, NULL);
    int lenA = strlen(resultChar);
    LOGD("%d", lenA);
    LOGD("%s", resultChar);
    PubAsc2Bcd(resultChar, lenA, RAPDU);
    *lenR = lenA/2;
    return TRUE;
}



JNIEXPORT jint JNICALL
Java_com_ndp_flazzbca_library_FlazzLib_test(JNIEnv *env, jobject thiz, jstring jstrConfig) {
    const char *strConfig=(*env)->GetStringUTFChars(env, jstrConfig,0);
    unsigned char strLogResponse[LENGTH_RESPONSE];
    memset(strLogResponse, 0x00, sizeof(strLogResponse));

    LOGD("Data %s", strConfig);
    unsigned char result = BCASetConfig(strConfig, strLogResponse);
    LOGD("%s",strLogResponse);
    (*env)->ReleaseStringUTFChars(env, jstrConfig, strConfig);
    LOGD("%s","ReleaseStringUTFChars");
    return 0;
}

