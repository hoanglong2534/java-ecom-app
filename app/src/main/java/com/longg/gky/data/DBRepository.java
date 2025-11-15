package com.longg.gky.data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.longg.gky.data.dao.CartDao;
import com.longg.gky.data.dao.ProductDao;
import com.longg.gky.data.entities.CartItemEntity;
import com.longg.gky.data.entities.ProductEntity;
import com.longg.gky.utils.AuthManager; // Import AuthManager

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBRepository {
    private static DBRepository instance;
    private final ProductDao productDao;
    private final CartDao cartDao;
    private final ExecutorService executorService;
    private final Context context; // Thêm context để lấy username

    public interface DBCallback {
        void onComplete();
    }

    public interface DBProductCallback {
        void onComplete(ProductEntity product);
    }

    public interface ValueCallback<T> {
        void onComplete(T value);
    }

    public interface CartItemsCallback {
        void onComplete(List<CartItemEntity> entities);
    }

    private DBRepository(Context context) {
        this.context = context.getApplicationContext(); // Lưu context
        AppDatabase database = AppDatabase.getInstance(context);
        productDao = database.productDao();
        cartDao = database.cartDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public static synchronized DBRepository get() {
        if (instance == null) {
            throw new IllegalStateException("DBRepository must be initialized");
        }
        return instance;
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new DBRepository(context);
        }
    }

    public LiveData<List<ProductEntity>> getAllProducts() {
        return productDao.getAll();
    }

    public void getProductByIdAsync(int productId, DBProductCallback callback) {
        executorService.execute(() -> {
            callback.onComplete(productDao.getProductById(productId));
        });
    }

    public void insertProductAsync(ProductEntity product, DBCallback callback) {
        executorService.execute(() -> {
            productDao.insert(product);
            callback.onComplete();
        });
    }

    public void updateProductAsync(ProductEntity product, DBCallback callback) {
        executorService.execute(() -> {
            productDao.update(product);
            callback.onComplete();
        });
    }

    private String getCurrentUserName() {
        return AuthManager.getUserName(context);
    }

    public void getAllCartItemsAsync(CartItemsCallback callback) {
        String userName = getCurrentUserName();
        executorService.execute(() -> callback.onComplete(cartDao.getAll(userName)));
    }

    public void addOrUpdateCartItemAsync(CartItemEntity cartItem) {
        cartItem.userName = getCurrentUserName(); // Gán userName trước khi insert
        executorService.execute(() -> cartDao.insert(cartItem));
    }

    public void removeCartItemByProductIdAsync(int productId) {
        String userName = getCurrentUserName();
        executorService.execute(() -> cartDao.deleteItemByProductId(productId, userName));
    }

    public void getCartCountAsync(ValueCallback<Integer> callback) {
        String userName = getCurrentUserName();
        executorService.execute(() -> callback.onComplete(cartDao.getCartItemCount(userName)));
    }

    public void clearCartAsync() {
        String userName = getCurrentUserName();
        executorService.execute(() -> cartDao.deleteAllItems(userName));
    }
}
