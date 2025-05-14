package com.archit.ecommerce.service.order;


import com.mongodb.client.result.UpdateResult;
import com.archit.ecommerce.middleware.JwtAspect;
import com.archit.ecommerce.dto.orders.request.OrderIdRequestDTO;
import com.archit.ecommerce.dto.orders.response.OrderAddResponseDTO;
import com.archit.ecommerce.dto.orders.response.OrderGetResponseDTO;
import com.archit.ecommerce.exception.EntityDeletionException;
import com.archit.ecommerce.exception.EntityUpdationException;
import com.archit.ecommerce.exception.InvalidInputException;
import com.archit.ecommerce.exception.ResourceNotFoundException;
import com.archit.ecommerce.model.CartItem;
import com.archit.ecommerce.model.Order;
import com.archit.ecommerce.model.Seller;
import com.archit.ecommerce.repository.OrderRepo;
import com.archit.ecommerce.service.cart.CartService;
import com.archit.ecommerce.service.product.ProductService;
import com.archit.ecommerce.service.user.SellerImplementation;
import com.archit.ecommerce.util.IdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final CartService cartService;
    private final IdGenerator idGenerator;
    private final OrderRepo orderRepo;
    private final ProductService productService;
    private final SellerImplementation sellerImplementation;
    private final MongoTemplate mongoTemplate;

    public OrderService(CartService cartService, IdGenerator idGenerator, OrderRepo orderRepo, ProductService productService, @Lazy SellerImplementation sellerImplementation, MongoTemplate mongoTemplate) {
        this.cartService = cartService;
        this.idGenerator = idGenerator;
        this.orderRepo = orderRepo;
        this.productService = productService;
        this.sellerImplementation = sellerImplementation;
        this.mongoTemplate = mongoTemplate;
    }

    public OrderAddResponseDTO placeOrder() {

        String cartId = JwtAspect.getCurrentUserId();
        if (cartId == null || cartId.isEmpty()) {
            throw new ResourceNotFoundException("User ID not found in JWT token.");
        }

        List<CartItem> cartItems = cartService.getCartItems(cartId);
        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("No items found in cart");
        }

        List<String> successful = new ArrayList<>();
        List<String> failed = new ArrayList<>();
        List<String> outOfStock = new ArrayList<>();
        LocalDateTime orderDateTime = LocalDateTime.now();

        for (CartItem item : cartItems) {
            if (item.getQuantity() > productService.getAvailableStock(item.getProductId())) {
                outOfStock.add(item.getName());
                continue;
            }


            String productId = item.getProductId();
            Seller seller = sellerImplementation.getSellerFromProductId(productId);
            Order order = new Order(idGenerator.generateId(), idGenerator.generateOrderId(),cartId, item, item.getPrice(), item.getQuantity(), "pending", orderDateTime, seller.getId());

            Optional<Order> savedOrder = Optional.ofNullable(orderRepo.save(order));
            if (savedOrder.isPresent()) {
                productService.updateProductStock(item.getProductId(), item.getQuantity());
                successful.add(order.getOrderId());
            } else {
                failed.add(item.getName());
            }
        }

        OrderAddResponseDTO response = new OrderAddResponseDTO();
        response.setSuccessfulOrders(successful);
        response.setOutOfStockItems(outOfStock);
        response.setFailedOrders(failed);
        cartService.deleteSelectedCartItems(cartId, cartItems);
        return response;
    }


    // get all orders of a particular user
    public OrderGetResponseDTO getAllOrders() {
        String userId = JwtAspect.getCurrentUserId();
        if (userId.isEmpty() || userId ==null) {
            throw new ResourceNotFoundException("User ID not found in JWT token.");
        }

        OrderGetResponseDTO orderGetResponseDTO = new OrderGetResponseDTO(orderRepo.findByUserId(userId));
        return orderGetResponseDTO;
    }

    public Order getOrder(String orderId) {
        Optional<Order> optionalOrder = orderRepo.findByOrderId(orderId);
        if(optionalOrder.isPresent()){
            return optionalOrder.get();
        }
        throw new ResourceNotFoundException("Failed to fetch order with specified credentials.");
    }

    /*
     * Method: getOrdersBySellerId
     * Role: Returns the orders for seller
     */
    public List<Order> getOrdersBySellerId(String sellerId) {
        List<Order> orderList = orderRepo.findBySellerId(sellerId);
        return orderList;
    }

    /*
     * Method: getOrders
     * Role: Displays all orders for the seller
     */
    public String updateOrders(OrderIdRequestDTO orderIdRequestDTO,String sellerId) {
        String orderId = orderIdRequestDTO.getOrderId();
        Optional<Order> optionalOrder = orderRepo.findByOrderIdAndSellerId(orderId, sellerId);
        if(optionalOrder.isEmpty()){
            throw new ResourceNotFoundException("Failed to fetch order with specified credentials.");
        }

        Query query = new Query(Criteria.where("orderId").is(orderId));
        Update update = new Update();
        update.set("status", "delivered");
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Order.class);
        if(updateResult.getModifiedCount() > 0){
            return "Order updated successfully";
        }
        throw new EntityUpdationException("Failed to update order");
    }

    public Order getOrdersByStatus(String status, String sellerId) {
        Optional<Order> optionalOrder = orderRepo.findByStatusAndSellerId(status, sellerId);
        if(optionalOrder.isPresent()){
            return optionalOrder.get();
        }
        throw new ResourceNotFoundException("Failed to fetch the order with provided credentials");
    }

    /*
     * Method: deleteOrder
     * Role: Deletes the order
     */
    public String deleteOrder(String orderId) {
        Optional<Order> order = orderRepo.findByOrderId(orderId);
        if(order.isEmpty()){
            throw new ResourceNotFoundException("Failed to fetch order with provided credentials.");
        }

        if(order.get().getStatus().equals("pending")){
            long c = orderRepo.deleteByOrderId(orderId);
            if(c > 0){
                return "Order deleted successfully.";
            }
            throw new EntityDeletionException("Failed to delete the order.");
        }
        throw new InvalidInputException("Invalid order status provided.");
    }
}
