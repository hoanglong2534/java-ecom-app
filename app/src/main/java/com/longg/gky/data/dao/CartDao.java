package com.longg.gky.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.longg.gky.data.entities.CartItemEntity;

import java.util.List;

@Dao
public interface CartDao {

    @Query("SELECT * FROM cart_items WHERE userName = :userName")
    List<CartItemEntity> getAll(String userName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartItemEntity cartItem);

    @Query("DELETE FROM cart_items WHERE productId = :productId AND userName = :userName")
    void deleteItemByProductId(int productId, String userName);

    @Query("SELECT SUM(quantity) FROM cart_items WHERE userName = :userName")
    int getCartItemCount(String userName);

    @Query("DELETE FROM cart_items WHERE userName = :userName")
    void deleteAllItems(String userName);
}
