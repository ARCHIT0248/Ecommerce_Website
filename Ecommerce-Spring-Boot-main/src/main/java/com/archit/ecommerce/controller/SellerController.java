package com.archit.ecommerce.controller;

import com.archit.ecommerce.dto.product.request.ProductRequestDTO;
import com.archit.ecommerce.dto.product.request.ProductUpdateRequestDTO;
import com.archit.ecommerce.dto.product.response.ProductDeleteResponseDTO;
import com.archit.ecommerce.dto.product.response.ProductGetResponseDTO;
import com.archit.ecommerce.middleware.AuthRequired;
import com.archit.ecommerce.dto.orders.request.OrderIdRequestDTO;
import com.archit.ecommerce.dto.orders.response.OrderSellerGetResponseDTO;
import com.archit.ecommerce.dto.product.request.*;
import com.archit.ecommerce.dto.product.response.*;
import com.archit.ecommerce.model.Product;
import com.archit.ecommerce.service.user.SellerImplementation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/seller")
public class SellerController {

    private SellerImplementation sellerImplementation;

    public SellerController(SellerImplementation sellerImplementation) {
        this.sellerImplementation = sellerImplementation;
    }

    /*
     * Method: getSellerDashboard
     * Role: GET: Displays Home Page For seller
     */
    @GetMapping("/get-product")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getSellerDashboard(HttpServletRequest request) {
        List<ProductGetResponseDTO> productDTOList = sellerImplementation.getAllProduct();

        return ResponseEntity.ok(productDTOList);
    }

    /*
     * Method: addProduct
     * Role: POST: Adds New Products by Seller
     */
    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthRequired
    public ResponseEntity<String> addProduct(
            @RequestPart("jsonData") ProductRequestDTO productRequestDTO,
            @RequestPart("images") MultipartFile[] images, HttpServletRequest request) {
        String msg = sellerImplementation.addProduct(productRequestDTO, images);
        return ResponseEntity.ok(msg);
    }

    /*
     * Method: getProductsByName
     * Role: GET: Displays specific products by name
     */
    @GetMapping("/products/name")
    @AuthRequired
    public ResponseEntity<ProductGetResponseDTO> getProductsByName(@RequestParam String productName){
        ProductGetResponseDTO productGetResponseDTO = sellerImplementation.getByNameAndSellerId(productName);
        return ResponseEntity.ok(productGetResponseDTO);
    }

    /*
     * Method: getProductsByCategory
     * Role: GET: Displays specific products by category
     */
    @GetMapping("/products/category")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getProductsByCategory(@RequestParam String category){
        List<Product> productList = sellerImplementation.getByCategoryAndSellerId(category);
        List<ProductGetResponseDTO> productDTOList = productList.stream()
                .map(product -> new ProductGetResponseDTO(
                        product.getName(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getStock(),
                        product.getImageUrls()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOList);
    }

    /*
     * Method: getProductsByPrice
     * Role: GET: Displays specific products by price range
     */
    @GetMapping("/products/price")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getProductsByPrice(@RequestParam double min, @RequestParam double max){
        List<Product> productList = sellerImplementation.getByPriceRangeAndSellerId( min, max);
        List<ProductGetResponseDTO> productGetResponseDTOList = productList.stream()
                .map(product -> new ProductGetResponseDTO(
                        product.getName(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getStock(),
                        product.getImageUrls()
                ))
                .collect((Collectors.toList()));
        return ResponseEntity.ok(productGetResponseDTOList);
    }

    /*
     * Method: deleteByName
     * Role: DELETE: Displays specific products by price range
     */
    @DeleteMapping("/product")
    @AuthRequired
    public ResponseEntity<String> deleteByName(@RequestParam String productName){
        ProductDeleteResponseDTO productDeleteResponseDTO = sellerImplementation.deleteByNameAndSellerId(productName);
        return ResponseEntity.ok(productDeleteResponseDTO.getProductMsg());
    }

    /*
     * Method: updateByName
     * Role: PUT: Updates product values
     */
    @PutMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthRequired
    public ResponseEntity<String> updateByName(
            @RequestPart("jsonData") ProductUpdateRequestDTO productUpdateRequestDTO,
            @RequestPart("images") MultipartFile[] images) {
        String msg = sellerImplementation.updateProductByNameAndSellerId(productUpdateRequestDTO, images);
        return ResponseEntity.ok(msg);
    }

    /*
     * Method: getOrders
     * Role: GET: Displays all orders for the seller
     */
    @GetMapping("/get-orders")
    @AuthRequired
    public ResponseEntity<List<OrderSellerGetResponseDTO>> getOrders(){
        List<OrderSellerGetResponseDTO> orderSellerGetResponseDTO = sellerImplementation.getOrders();
        return ResponseEntity.ok(orderSellerGetResponseDTO);
    }

    /*
     * Method: getOrders
     * Role: GET: Displays orders based on status
     */
    @GetMapping("/get-orders-by-status")
    @AuthRequired
    public ResponseEntity<OrderSellerGetResponseDTO> getOrdersByStatus(@RequestParam String status){
        OrderSellerGetResponseDTO orderSellerGetResponseDTO = sellerImplementation.getOrdersByStatus(status);
        return ResponseEntity.ok(orderSellerGetResponseDTO);
    }

    /*
     * Method: getOrders
     * Role: GET: Displays all orders for the seller
     */
    @PutMapping("/put-order")
    @AuthRequired
    public ResponseEntity<String> updateOrders(@RequestBody OrderIdRequestDTO orderIdRequestDTO){
        String msg = sellerImplementation.updateOrders(orderIdRequestDTO);
        return ResponseEntity.ok(msg);
    }

}