package com.archit.ecommerce.repository;

import com.archit.ecommerce.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartItemRepo extends MongoRepository<CartItem, String> {
}
