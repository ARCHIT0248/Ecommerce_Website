package com.archit.ecommerce.dto.product.response;

import com.archit.ecommerce.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDescriptionListResponseDTO {
    private List<Product> productList;
}
