# Đặt phiên bản CMake tối thiểu
cmake_minimum_required(VERSION 3.22.1)

# Định nghĩa project
project("gky")

# Thêm thư viện native-lib từ file native-lib.cpp
add_library(
    gky-native
    SHARED
    native-lib.cpp
)

# Tìm và liên kết với các thư viện log của Android
find_library(
    log-lib
    log
)

# Liên kết thư viện log với thư viện của chúng ta
target_link_libraries(
    gky-native
    ${log-lib}
)
