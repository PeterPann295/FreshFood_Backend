package com.freshfood.repository;

import com.freshfood.model.Cart;
import com.freshfood.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser(User user);
    @Query(value = "SELECT SUM(c.quantity) FROM cart_items c WHERE c.cart_id = :cartId GROUP BY c.cart_id", nativeQuery = true)
    Optional<Integer> getTotalQuantityByCartId(@Param("cartId") Integer cartId);
}
