# G-Ky Store - Ứng dụng Bán hàng Android

## 1. Giới thiệu

Đây là một dự án ứng dụng Android mô phỏng một sàn thương mại điện tử đơn giản. Ứng dụng được xây dựng nhằm mục đích học tập và áp dụng các kiến trúc, thư viện phổ biến trong phát triển Android.

## 2. Công nghệ & Kiến trúc sử dụng (Tech Stack & Architecture)

- **Ngôn ngữ:** Java
- **Kiến trúc:** Repository Pattern
- **Cơ sở dữ liệu:** Room Persistence Library (để lưu trữ dữ liệu sản phẩm, người dùng, giỏ hàng).
- **Luồng bất đồng bộ:** `ExecutorService` để xử lý các tác vụ nền (database operations).
- **Giao diện người dùng (UI):**
  - Android UI Toolkit (XML Layouts).
  - `RecyclerView` để hiển thị các danh sách động.
- **Tải ảnh:** Glide (Thư viện tải và cache ảnh hiệu quả).
- **Design Pattern:** Singleton (Sử dụng cho các lớp quản lý như `DBRepository`, `AuthManager`, `CartManager`).

## 3. Tính năng chính

**Đối với Người dùng (User):**

- Đăng ký và đăng nhập tài khoản.
- Xem danh sách sản phẩm trên trang chủ.
- Xem thông tin chi tiết của từng sản phẩm.
- Thêm sản phẩm vào giỏ hàng cá nhân.
- Quản lý giỏ hàng (thay đổi số lượng, xóa sản phẩm).
- Thực hiện quy trình thanh toán (mô phỏng) với việc nhập thông tin giao hàng.

**Đối với Quản trị viên (Admin):**

- Toàn bộ quyền của Người dùng.
- Thêm sản phẩm mới (bao gồm ảnh, tên, giá, mô tả...).
- Sửa thông tin chi tiết của sản phẩm đã có.

## 4. Cấu trúc thư mục dự án

```
app/src/main/
├── java/com/longg/gky/
│   ├── adapters/        # Chứa các Adapter cho RecyclerView
│   ├── data/            # Quản lý dữ liệu và CSDL
│   │   ├── dao/         # Data Access Objects (DAO) cho Room
│   │   ├── entities/    # Định nghĩa các bảng (Entities) cho Room
│   │   ├── AppDatabase.java    # Khởi tạo và cấu hình Room Database
│   │   └── DBRepository.java   # Repository: trung gian truy cập dữ liệu
│   ├── models/          # Các lớp Plain Old Java Object (POJO)
│   └── utils/           # Các lớp tiện ích, quản lý trạng thái
│   └── *.java           # Các Activity chính
│
└── res/
    ├── layout/          # Giao diện (XML)
    ├── drawable/        # Tài nguyên ảnh
    └── values/          # Tài nguyên giá trị (strings, colors, dimensions)
```

## 5. Luồng hoạt động chi tiết

1.  **Khởi động & Tải dữ liệu ban đầu**
    - `MainActivity` được khởi chạy.
    - Trong `onCreate`, nó gọi `DBRepository.get().getAllProducts()` để lấy danh sách sản phẩm.
    - `DBRepository` truy cập `productDao` của Room để thực hiện query `SELECT * FROM products`.
    - Dữ liệu `List<ProductEntity>` được trả về, `MainActivity` chuyển đổi nó thành `List<Product>` và đưa vào `ProductAdapter` để hiển thị lên `RecyclerView`.

2.  **Đăng ký / Đăng nhập**
    - Người dùng vào `ProfileActivity` và chọn Đăng nhập hoặc Đăng ký.
    - **Đăng ký (`SignUpActivity`):** Người dùng nhập thông tin. Nút đăng ký sẽ lưu thông tin người dùng (tên, mật khẩu, vai trò 'USER') vào `SharedPreferences` thông qua `AuthManager`.
    - **Đăng nhập (`LoginActivity`):** Người dùng nhập tên và mật khẩu. `AuthManager` sẽ kiểm tra thông tin này với dữ liệu đã lưu trong `SharedPreferences` để xác thực.
    - Sau khi đăng nhập, `ProfileActivity` cập nhật giao diện, hiển thị thông tin người dùng và các chức năng tương ứng (ví dụ: nút "Thêm sản phẩm" cho Admin).

3.  **Thêm sản phẩm vào giỏ hàng**
    - Từ `MainActivity`, người dùng nhấn vào một sản phẩm.
    - Một `Intent` được tạo để mở `ProductDetailActivity`, truyền `product_id` của sản phẩm được chọn.
    - Trong `ProductDetailActivity`, người dùng nhấn nút "Thêm vào giỏ hàng".
    - `CartManager.getInstance().addToCart(product)` được gọi.
    - `CartManager` tìm xem sản phẩm đã có trong giỏ hàng (trong bộ nhớ) của người dùng chưa. Nếu có, nó tăng số lượng. Nếu chưa, nó tạo một `CartItem` mới.
    - `CartManager` gọi phương thức `persistCartItem()`, phương thức này tạo một `CartItemEntity`.
    - `DBRepository.get().addOrUpdateCartItemAsync(entity)` được gọi. `DBRepository` sẽ tự động lấy `userName` từ `AuthManager` và gán vào `entity` trước khi insert/update vào CSDL Room thông qua `cartDao`.

4.  **Xem giỏ hàng & Thanh toán**
    - Người dùng vào `CartActivity`.
    - `CartActivity` lấy danh sách `CartItem` từ `CartManager.getInstance().getCartItems()` và hiển thị qua `CartAdapter`.
    - Khi người dùng nhấn nút thanh toán, `CheckoutActivity` được mở.
    - Người dùng nhập thông tin giao hàng. Nút "Đặt hàng" sẽ kiểm tra tính hợp lệ của thông tin.
    - Nếu hợp lệ, `CartManager.getInstance().clearCart()` được gọi. Lệnh này sẽ xóa toàn bộ `CartItemEntity` của người dùng hiện tại khỏi CSDL Room và xóa dữ liệu trong bộ nhớ của `CartManager`.

5.  **Quản lý sản phẩm (Luồng Admin)**
    - **Thêm sản phẩm:**
        - Trong `ProfileActivity`, Admin nhấn "Thêm sản phẩm", một `AlertDialog` hiện ra.
        - Admin nhập thông tin và chọn ảnh. `imagePickerLauncher` xử lý việc chọn ảnh và trả về một `Uri`.
        - Khi nhấn "Lưu", phương thức `saveProduct` được gọi. Nó sao chép file ảnh từ `Uri` nhận được vào bộ nhớ trong của ứng dụng (`/data/data/com.longg.gky/files/product_images/`) và lấy đường dẫn tuyệt đối của file đã sao chép.
        - Đường dẫn này được lưu vào trường `imageUrl` của `ProductEntity`, sau đó `DBRepository` sẽ lưu sản phẩm mới này vào CSDL.
    - **Sửa sản phẩm:**
        - Trong `ProductDetailActivity`, Admin nhấn "Sửa", một `AlertDialog` tương tự hiện ra.
        - Logic hoạt động tương tự như thêm sản phẩm. Nếu Admin chọn ảnh mới, ảnh sẽ được sao chép và đường dẫn mới sẽ được lưu. Nếu không, đường dẫn ảnh cũ được giữ nguyên.
        - `DBRepository` sẽ cập nhật (`UPDATE`) thông tin sản phẩm trong CSDL.
        - Sau khi cập nhật thành công, `loadProductData()` được gọi lại để tải lại dữ liệu mới nhất từ CSDL và hiển thị lên màn hình chi tiết.

---
*Cập nhật lần cuối vào ngày 16/11/2025 bởi: **[hoanglong2534](https://github.com/hoanglong2534)***
