package com.longg.gky.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.longg.gky.data.dao.CartDao;
import com.longg.gky.data.dao.ProductDao;
import com.longg.gky.data.entities.CartItemEntity;
import com.longg.gky.data.entities.ProductEntity;

// TĂNG PHIÊN BẢN LÊN 7 ĐỂ FIX LỖI DỨT ĐIỂM
@Database(entities = {ProductEntity.class, CartItemEntity.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "app_database.db";
    private static volatile AppDatabase instance;

    public abstract ProductDao productDao();
    public abstract CartDao cartDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                            // Xóa CSDL cũ và tạo lại khi nâng cấp phiên bản
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
