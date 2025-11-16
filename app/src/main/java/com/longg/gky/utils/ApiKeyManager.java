package com.longg.gky.utils;

public class ApiKeyManager {

    // Tải thư viện C++ "gky-native" khi lớp được nạp
    static {
        System.loadLibrary("gky-native");
    }

    // Khai báo phương thức native sẽ được liên kết với hàm C++ để lấy key
    public static native String getApiKey();
}
