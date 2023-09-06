#include <jni.h>
#include <string>

extern "C" {
JNIEXPORT jstring JNICALL
Java_com_example_moviedb_core_env_Environment_getBaseUrl(JNIEnv *env, jobject) {
    std::string baseUrl = "https://api.themoviedb.org/3/";
    return env->NewStringUTF(baseUrl.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_example_moviedb_core_env_Environment_getAPIKey(JNIEnv *env, jobject) {
    std::string apiKey = "1157aaf4cd28105165a7f5f43b853aae";
    return env->NewStringUTF(apiKey.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_example_moviedb_core_env_Environment_getBaseImageUrl(JNIEnv *env, jobject) {
    std::string url = "https://image.tmdb.org/t/p/w500";
    return env->NewStringUTF(url.c_str());
}
}