package com.archit.ecommerce.repository;

import com.archit.ecommerce.model.Buyer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<Buyer, String> {
}
