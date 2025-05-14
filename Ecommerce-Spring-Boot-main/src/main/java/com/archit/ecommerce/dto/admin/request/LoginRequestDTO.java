package com.archit.ecommerce.dto.admin.request;

import com.archit.ecommerce.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    private String email;
    private String password;
    private Roles role;
}
