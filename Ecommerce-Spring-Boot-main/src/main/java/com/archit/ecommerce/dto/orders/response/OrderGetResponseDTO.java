package com.archit.ecommerce.dto.orders.response;

import com.archit.ecommerce.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderGetResponseDTO {
    private List<Order> orderList;
}
