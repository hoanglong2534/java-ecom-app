# ShopEasy - E-Commerce Android App

## Mô tả
ShopEasy là một ứng dụng mua sắm trực tuyến hiện đại được phát triển bằng Java cho Android. Ứng dụng có giao diện đẹp mắt, thân thiện với người dùng và tích hợp đầy đủ các tính năng của một ứng dụng E-Commerce.

## Tính năng chính

### 🎨 Giao diện người dùng
- **Splash Screen**: Màn hình chào mừng với animation đẹp mắt
- **Home Screen**: Trang chủ với banner khuyến mãi, danh mục sản phẩm và sản phẩm nổi bật
- **Product Grid/List**: Hiển thị sản phẩm dưới dạng lưới và danh sách
- **Product Detail**: Trang chi tiết sản phẩm với hình ảnh, mô tả, giá cả
- **Shopping Cart**: Giỏ hàng với tính năng thêm/xóa/cập nhật số lượng
- **Bottom Navigation**: Điều hướng dưới cùng với 5 tab chính

### 🛍️ Tính năng mua sắm
- **Browse Products**: Duyệt sản phẩm theo danh mục
- **Search**: Tìm kiếm sản phẩm
- **Favorites**: Thêm sản phẩm vào danh sách yêu thích
- **Add to Cart**: Thêm sản phẩm vào giỏ hàng
- **Price Display**: Hiển thị giá gốc, giá khuyến mãi và % giảm giá
- **Product Rating**: Đánh giá sản phẩm với sao và số lượng review

### 📱 Danh mục sản phẩm
- **Smartphones**: iPhone, Samsung Galaxy, v.v.
- **Laptops**: MacBook, Dell XPS, v.v.
- **Audio**: AirPods, Sony WH-1000XM5, v.v.
- **Accessories**: Apple Watch, iPad, v.v.
- **Fashion**: Nike shoes, v.v.
- **Photography**: Camera Canon, v.v.
- **Gaming**: PlayStation 5, v.v.

## Cấu trúc dự án

### 📁 Models
- `Product.java`: Model cho sản phẩm
- `Category.java`: Model cho danh mục
- `CartItem.java`: Model cho item trong giỏ hàng

### 📁 Adapters
- `ProductAdapter.java`: Adapter hiển thị danh sách sản phẩm
- `CategoryAdapter.java`: Adapter hiển thị danh mục
- `CartAdapter.java`: Adapter hiển thị giỏ hàng

### 📁 Activities
- `SplashActivity.java`: Màn hình splash
- `MainActivity.java`: Màn hình chính
- `ProductDetailActivity.java`: Chi tiết sản phẩm
- `CartActivity.java`: Giỏ hàng

### 📁 Utils
- `CartManager.java`: Quản lý giỏ hàng (Singleton pattern)
- `DataUtils.java`: Cung cấp dữ liệu mẫu

### 📁 Resources
- **Layouts**: XML layouts cho từng màn hình
- **Drawables**: Icons, backgrounds, gradients
- **Colors**: Bảng màu của ứng dụng
- **Strings**: Chuỗi văn bản đa ngôn ngữ
- **Animations**: Hiệu ứng chuyển cảnh

## Công nghệ sử dụng

### 🔧 Core Technologies
- **Java**: Ngôn ngữ lập trình chính
- **Android SDK**: Phát triển ứng dụng Android
- **Material Design**: Thiết kế giao diện theo chuẩn Google

### 📚 Libraries
- **Glide**: Load và cache hình ảnh từ URL
- **RecyclerView**: Hiển thị danh sách hiệu quả
- **CardView**: Thiết kế card đẹp mắt
- **ViewPager2**: Slider hình ảnh
- **ConstraintLayout**: Layout linh hoạt

### 🎨 Design Features
- **Gradient Backgrounds**: Nền gradient đẹp mắt
- **Material Cards**: Thẻ sản phẩm với shadow
- **Custom Icons**: Icons được thiết kế riêng
- **Responsive Layout**: Giao diện thích ứng nhiều kích thước màn hình
- **Smooth Animations**: Hiệu ứng chuyển cảnh mượt mà

## Hướng dẫn cài đặt

### 📋 Yêu cầu hệ thống
- Android Studio Arctic Fox trở lên
- Android SDK 24+ (Android 7.0)
- Java 11
- Gradle 7.0+

### 🚀 Cách chạy ứng dụng
1. Clone repository
2. Mở project trong Android Studio
3. Sync Gradle
4. Build và chạy trên device/emulator

### 📱 APK Build
```bash
./gradlew assembleDebug
```

## Màn hình ứng dụng

### 🌟 Splash Screen
- Logo ứng dụng với animation
- Thương hiệu "ShopEasy"
- Tagline hấp dẫn

### 🏠 Home Screen
- Welcome message cá nhân hóa
- Search bar thông minh
- Banner khuyến mãi eye-catching
- Grid danh mục sản phẩm
- Sản phẩm nổi bật
- Sản phẩm khuyến mãi đặc biệt

### 🛒 Product Features
- Hình ảnh sản phẩm chất lượng cao từ Unsplash
- Badge giảm giá nổi bật
- Nút yêu thích dễ sử dụng
- Thông tin chi tiết: tên, thương hiệu, giá, đánh giá
- Multiple colors và sizes

### 🛍️ Shopping Cart
- Quản lý số lượng sản phẩm
- Tính toán tổng tiền tự động
- Xóa sản phẩm khỏi giỏ hàng
- Badge số lượng trên icon cart

## Đặc điểm nổi bật

### 🎯 User Experience
- **Intuitive Navigation**: Điều hướng trực quan
- **Fast Loading**: Tải nhanh với Glide
- **Smooth Scrolling**: Cuộn mượt mà
- **Responsive Touch**: Phản hồi nhanh khi chạm

### 🎨 Visual Design
- **Modern UI**: Giao diện hiện đại
- **Consistent Colors**: Bảng màu nhất quán
- **Beautiful Typography**: Typography đẹp mắt
- **Professional Icons**: Icons chuyên nghiệp

### 🔧 Technical Excellence
- **Clean Architecture**: Kiến trúc rõ ràng
- **Efficient Memory**: Quản lý bộ nhớ hiệu quả
- **Error Handling**: Xử lý lỗi tốt
- **Performance Optimized**: Tối ưu hiệu suất

## Hình ảnh sản phẩm

Ứng dụng sử dụng hình ảnh chất lượng cao từ Unsplash.com:
- **iPhones**: Hình ảnh iPhone thực tế
- **Laptops**: MacBook, Dell XPS
- **Accessories**: Apple Watch, AirPods
- **Fashion**: Nike sneakers
- **Electronics**: Camera, gaming console

## Future Enhancements

### 🚀 Planned Features
- **User Authentication**: Đăng nhập/đăng ký
- **Payment Integration**: Tích hợp thanh toán
- **Order History**: Lịch sử đơn hàng
- **Push Notifications**: Thông báo đẩy
- **Wishlist**: Danh sách yêu thích
- **Product Reviews**: Đánh giá sản phẩm
- **Filter & Sort**: Lọc và sắp xếp sản phẩm

### 🌐 Technical Improvements
- **API Integration**: Kết nối API thực tế
- **Database**: SQLite/Room database
- **Offline Support**: Hỗ trợ offline
- **Multi-language**: Đa ngôn ngữ
- **Dark Theme**: Chế độ tối

## Kết luận

ShopEasy là một ứng dụng E-Commerce hoàn chỉnh với giao diện đẹp mắt, tính năng đầy đủ và trải nghiệm người dùng tuyệt vời. Ứng dụng được phát triển theo các chuẩn modern Android development và sẵn sàng cho việc mở rộng thêm tính năng.

---
**Developer**: Longg  
**Version**: 1.0  
**Last Updated**: November 2024
