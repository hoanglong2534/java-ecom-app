package com.longg.gky.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.longg.gky.data.entities.ProductEntity;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products")
    LiveData<List<ProductEntity>> getAll();

    @Query("SELECT * FROM products WHERE id = :productId")
    ProductEntity getProductById(int productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ProductEntity... products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductEntity product);

    @Update
    void update(ProductEntity product);

    @Query("SELECT COUNT(*) FROM products")
    int count();
}
