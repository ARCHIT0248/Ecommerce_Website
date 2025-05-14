package com.archit.ecommerce.repository;

import com.archit.ecommerce.model.Admin;
import com.archit.ecommerce.model.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepo extends MongoRepository<Admin, String> {

    @Query("{ 'email' : ?0 }")
    Admin findByEmail(String email);

    @Query(value = "{ '_id' : ?0 }", delete = true)
    Long deleteByAdminId(String id);

    @Query("{'email' = ?0}")
    boolean existsByEmail(String email);

    @Query("{ 'email' : ?0, 'roles' : ?1 }")
    Admin findByEmailAndRoles(String email, Roles role);

    @Query("{'roles' : ?0}")
    List<Admin> findByRoles(Roles roles);

}
