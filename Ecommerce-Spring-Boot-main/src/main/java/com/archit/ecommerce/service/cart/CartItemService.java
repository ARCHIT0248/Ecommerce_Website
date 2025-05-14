package com.archit.ecommerce.service.cart;

import com.archit.ecommerce.model.CartItem;
import com.archit.ecommerce.repository.CartItemRepo;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private CartItemRepo cartItemRepo;

    public CartItemService(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    public CartItem productToCartItem(String name, String productId, double price, int quantity, String image){
        CartItem cartItem = new CartItem();

        price = price * quantity;
        cartItem.setName(name);
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(price);
        cartItem.setImage(image);
        cartItem.setSelectedForPayment(true);

        return cartItem;
    }
}
