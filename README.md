# G-Ky Store - Ứng dụng Bán hàng Android

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)

Đây là dự án ứng dụng Android mô phỏng một sàn thương mại điện tử đơn giản, được xây dựng nhằm mục đích học tập và áp dụng các kiến trúc, thư viện phổ biến trong phát triển Android.

## Tính năng chính

*   **Người dùng:**
    *   Đăng ký, đăng nhập tài khoản.
    *   Xem danh sách sản phẩm, xem chi tiết.
    *   Thêm sản phẩm vào giỏ hàng.
    *   Quản lý giỏ hàng và thực hiện thanh toán (mô phỏng).
*   **Quản trị viên (Admin):**
    *   Thêm, sửa thông tin sản phẩm.
*   **Chatbot AI:**
    *   Tích hợp chatbot thông minh để trả lời các câu hỏi của người dùng thông qua OpenRouter.

## Công nghệ & Kiến trúc

*   **Ngôn ngữ:** Java
*   **Kiến trúc:** Repository Pattern, Singleton
*   **Giao diện:** Android UI Toolkit (XML), `RecyclerView`, `CardView`.
*   **Cơ sở dữ liệu:** Room Persistence Library.
*   **Tác vụ nền:** `ExecutorService`.
*   **Tải ảnh:** Glide.
*   **Gọi API:** OkHttp.
*   **AI Chatbot:** Tích hợp với API của OpenRouter.

## Hướng dẫn cài đặt và chạy dự án

1.  **Clone repository về máy của bạn:**
    ```bash
    git clone https://github.com/hoanglong2534/BTL_GKI.git
    ```

2.  **Mở dự án bằng Android Studio.**

3.  **Cấu hình API Key:**
    *   Tìm đến file `local.properties` ở thư mục gốc của dự án. Nếu chưa có, hãy tạo một file mới với tên này.
    *   Thêm API key của bạn từ [OpenRouter](https://openrouter.ai/keys) vào file theo cú pháp sau:
        ```properties
        OPENROUTER_API_KEY="YOUR_API_KEY_HERE"
        ```
    *   **Lưu ý:** Thay `YOUR_API_KEY_HERE` bằng API key thật của bạn.

4.  **Đồng bộ Gradle:**
    *   Android Studio sẽ hiển thị thông báo yêu cầu đồng bộ. Nhấp vào "Sync Now".

5.  **Chạy ứng dụng:**
    *   Chọn thiết bị (máy ảo hoặc máy thật) và nhấn nút Run.

## Luồng hoạt động chi tiết

1.  **Khởi động & Tải dữ liệu ban đầu**
    - `MainActivity` được khởi chạy. Trong phương thức `onCreate`, nó lấy instance của `DBRepository` (Singleton) và gọi `getAllProducts()`.
    - `DBRepository` sử dụng `ExecutorService` để tạo một luồng nền mới. Điều này cực kỳ quan trọng để các tác vụ tốn thời gian như truy vấn CSDL không làm chặn luồng giao diện chính, tránh gây ra lỗi ANR (Application Not Responding).
    - Trên luồng nền, `DBRepository` truy cập vào `AppDatabase.get().productDao().getAll()`. Lệnh này thực thi câu lệnh SQL `SELECT * FROM products` và trả về một `List<ProductEntity>`.
    - Sau khi có kết quả, `DBRepository` sử dụng một `Handler` để gửi dữ liệu trở lại luồng chính (UI thread). Tại đây, callback mà `MainActivity` đã cung cấp sẽ được gọi.
    - `MainActivity` nhận danh sách các `ProductEntity`, chuyển đổi chúng thành các model phù hợp với giao diện, và cập nhật vào `ProductAdapter`. Adapter sau đó sẽ thông báo cho `RecyclerView` để vẽ lại danh sách sản phẩm lên màn hình.

2.  **Đăng ký / Đăng nhập**
    - Người dùng vào `ProfileActivity`. Dựa vào trạng thái đăng nhập lấy từ `AuthManager.getInstance().isLoggedIn()`, giao diện sẽ hiển thị thông tin người dùng hoặc các nút Đăng nhập/Đăng ký.
    - **Đăng ký (`SignUpActivity`):** Người dùng nhập thông tin. Nút đăng ký gọi `AuthManager.registerUser()`. Lớp này (một Singleton) sẽ dùng `SharedPreferences` để lưu thông tin người dùng, bao gồm tên đăng nhập, mật khẩu đã được mã hóa (để tránh lưu mật khẩu dạng text), và vai trò mặc định là 'USER'.
    - **Đăng nhập (`LoginActivity`):** Người dùng nhập thông tin. `AuthManager.loginUser()` sẽ lấy thông tin đã lưu từ `SharedPreferences`, mã hóa mật khẩu người dùng vừa nhập và so sánh. Nếu khớp, trạng thái đăng nhập sẽ được cập nhật và lưu lại trong `AuthManager`.

3.  **Thêm sản phẩm vào giỏ hàng**
    - Từ `ProductDetailActivity`, người dùng nhấn "Thêm vào giỏ hàng".
    - `CartManager.getInstance().addToCart(product)` được gọi. `CartManager` là một Singleton, đảm bảo chỉ có một giỏ hàng duy nhất tồn tại trong suốt vòng đời ứng dụng. Nó sử dụng một `HashMap` để lưu các sản phẩm trong bộ nhớ, cho phép truy cập và cập nhật số lượng ngay lập tức.
    - Sau khi cập nhật giỏ hàng trong bộ nhớ, `CartManager` gọi `persistCartItem()` để đồng bộ thay đổi xuống CSDL. Phương thức này tạo ra một `CartItemEntity`.
    - `DBRepository.get().addOrUpdateCartItemAsync(entity)` được gọi. Repository sẽ lấy tên người dùng hiện tại từ `AuthManager` để gán vào `CartItemEntity` trước khi dùng luồng nền để thực hiện thao tác "upsert" (insert hoặc update nếu đã tồn tại) vào CSDL Room.

4.  **Xem giỏ hàng & Thanh toán**
    - `CartActivity` lấy danh sách sản phẩm từ `CartManager` và hiển thị qua `CartAdapter`.
    - Khi người dùng nhấn nút thanh toán trong `CheckoutActivity`, `CartManager.getInstance().clearCart()` được gọi.
    - Phương thức này sẽ xóa dữ liệu trong `HashMap` của `CartManager`, đồng thời gọi `DBRepository.get().clearCartForCurrentUser()`. Repository sẽ dùng luồng nền để thực thi `cartDao.deleteCartByUsername()` nhằm xóa toàn bộ sản phẩm trong giỏ hàng của người dùng đó khỏi CSDL.

5.  **Quản lý sản phẩm (Luồng Admin)**
    - Khi Admin thêm/sửa sản phẩm và chọn một ảnh từ thư viện, `ActivityResultLauncher` sẽ trả về một `Uri` của ảnh đó.
    - Phương thức `saveProduct` sẽ tạo một file mới trong bộ nhớ trong của ứng dụng (thư mục riêng tư, chỉ ứng dụng mới có thể truy cập). Nó đọc dữ liệu từ `Uri` và ghi vào file mới này.
    - Đường dẫn tuyệt đối của file ảnh vừa tạo sẽ được lưu vào trường `imageUrl` của `ProductEntity`. Cách làm này đảm bảo ứng dụng không bị mất ảnh nếu người dùng xóa ảnh gốc khỏi thư viện.
    - `DBRepository` sẽ lưu hoặc cập nhật `ProductEntity` này vào CSDL.

6.  **Tương tác với Chatbot AI**
    - Tại `ChatActivity`, API key được lấy một cách an toàn từ lớp `BuildConfig` (giá trị này được Gradle tiêm vào từ file `local.properties` lúc build ứng dụng, giúp key không bị lộ trong code).
    - Khi người dùng gửi tin nhắn, `OpenAiApiClient.generateText()` được gọi. Lớp này sử dụng `OkHttp` để tạo và gửi một yêu cầu HTTP POST đến địa chỉ API của OpenRouter.
    - Yêu cầu này chứa các Header quan trọng: `Authorization: Bearer <API_KEY>` để xác thực và `HTTP-Referer` để OpenRouter nhận dạng ứng dụng của bạn.
    - `OkHttp` thực hiện yêu cầu một cách bất đồng bộ. Khi có phản hồi, callback `onResponse` được kích hoạt trên một luồng nền. Nó sẽ kiểm tra mã trạng thái HTTP, nếu thành công (200 OK) thì sẽ phân tích chuỗi JSON để lấy ra nội dung tin nhắn của bot.
    - Cuối cùng, `runOnUiThread()` được gọi để đảm bảo việc cập nhật giao diện (thêm tin nhắn mới vào `RecyclerView`) được thực hiện một cách an toàn trên luồng UI chính.

## Cấu trúc thư mục

```
app/src/main/
├── java/com/longg/gky/
│   ├── adapters/        # Chứa các Adapter cho RecyclerView
│   ├── data/            # Quản lý CSDL (Room, DAO, Entities, Repository)
│   ├── models/          # Các lớp Plain Old Java Object (POJO)
│   ├── network/         # Client gọi API (OpenAiApiClient)
│   ├── utils/           # Các lớp tiện ích, quản lý trạng thái
│   └── *.java           # Các Activity chính
│
└── res/
    ├── layout/          # Giao diện (XML)
    └── ...
```

---

*Đóng góp và phát triển bởi [hoanglong2534](https://github.com/hoanglong2534) và [Phong (Neil-06-hub)](https://github.com/Neil-06-hub)*
