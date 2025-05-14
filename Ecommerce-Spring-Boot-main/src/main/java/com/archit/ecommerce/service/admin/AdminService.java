package com.archit.ecommerce.service.admin;

import com.archit.ecommerce.model.Admin;
import com.archit.ecommerce.model.Roles;

import java.security.NoSuchAlgorithmException;

public interface AdminService {
    public Admin login(String email, String password, Roles role) throws NoSuchAlgorithmException;
}
