package com.archit.ecommerce.repository;

import com.archit.ecommerce.model.Cart;
import com.archit.ecommerce.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CartRepo extends MongoRepository<Cart, String> {

    @Query("{'cartItems' : ?0 }")
    CartItem findByTag(String productId);

    @Query("{'_id' : ?0}")
    Cart findByCartId(String cartId);
}
