package com.archit.ecommerce.repository;

import com.archit.ecommerce.model.Seller;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepo extends MongoRepository<Seller, String> {

//    @Query("email = ?0")
    @Query("{ 'email' : ?0 }")
    Seller findByEmail(String email);

    @Query("{'email' = ?0}")
    boolean existsByEmail(String email);

    @Query("{ '_id' : ?0}")
    Optional<Seller> findById(String id);
}
