#include <jni.h>
#include <string>

// SỬA LỖI DỨT ĐIỂM: Bỏ phương pháp mã hóa phức tạp, thay bằng cách ghép chuỗi đơn giản.
// Cách này kém an toàn hơn nhưng đảm bảo không bị lỗi tính toán.
std::string getApiKey() {
    char key_parts[] = {
        'A', 'I', 'z', 'a', 'S', 'y', 'B', 'P', 'V', 'j', '3', 'e', '9', 'T', 'a', 'm',
        'O', 'p', 'z', 'X', 'x', 'k', '5', '3', '3', 'Z', 'K', 'M', 'd', 'H', 'J', 'o',
        'B', 'D', 'D', 'Q', 'L', 'd', 's'
    };
    // Ghép mảng ký tự lại thành một chuỗi (string) hoàn chỉnh.
    return std::string(key_parts, sizeof(key_parts));
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_longg_gky_utils_ApiKeyManager_getApiKey(JNIEnv* env, jclass /* this */) {
    std::string apiKey = getApiKey();
    return env->NewStringUTF(apiKey.c_str());
}
