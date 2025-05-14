package com.archit.ecommerce.service.user;

import com.archit.ecommerce.dto.admin.response.LoginResponseDTO;
import com.archit.ecommerce.model.User;

public interface UserService<T extends User> {

    public void register(T user);

    public LoginResponseDTO login(T user);

//    public String login(String email, String password, AdminRoles role) throws NoSuchAlgorithmException;
}
