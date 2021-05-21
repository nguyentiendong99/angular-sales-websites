package com.example.demo.controller;

import com.example.demo.config.ShoppingConfiguration;
import com.example.demo.config.request.ApiResponse;
import com.example.demo.entity.BillDetails;
import com.example.demo.entity.Bills;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("order")
public class OrderController {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductService productService;
    @Autowired
    OrderRepository orderRepository;
    @RequestMapping("/checkout_order")
    public ResponseEntity<?> checkout_order(@RequestBody HashMap<String,String> addCartRequest) {
        try {
            String keys[] = {"user_id","price","address"};
            if(ShoppingConfiguration.validationWithHashMap(keys, addCartRequest)) {


            }
            int user_Id = Integer.parseInt(addCartRequest.get("user_id"));
            double total_amt = Double.parseDouble(addCartRequest.get("price"));
            if(cartRepository.checkTotalAmountAgainstCart(total_amt,user_Id)) {
                List<BillDetails> cartItems = cartRepository.getCartByUserId(user_Id);
                List<Order> tmp = new ArrayList<Order>();
                for(BillDetails billDetails : cartItems) {
                  //  int billId = "" + getOrderId();
                    Order cart = new Order();
                    cart.setPrice(billDetails.getPrice());
                    cart.setUser_id(user_Id);
                    cart.setOrderDate(new Date());
                   // cart.setBill_id(orderId);
                    cart.setProduct(billDetails.getProduct());
                    cart.setQuantity(billDetails.getQuantity());
                    cart.setAddress(addCartRequest.get("address"));
                    tmp.add(cart);
                }
                cartRepository.saveProductsForCheckout(tmp);
                return ResponseEntity.ok(new ApiResponse("Order placed successfully", ""));
            }else {
                throw new Exception("Total amount is mismatch");
            }
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), ""));
        }
    }
    public int getOrderId() {
        Random r = new Random( System.currentTimeMillis() );
        return 10000 + r.nextInt(20000);
    }
    @RequestMapping("getOrdersByUserId")
    public ResponseEntity<?> getOrdersByUserId(@RequestBody HashMap<String,String> ordersRequest) {
        try {
            String keys[] = {"user_id"};
            return ResponseEntity.ok(new ApiResponse("Order placed successfully", ""));
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), ""));
        }
    }

    // get list orders
    @GetMapping("/orders")
    public List<?> getAllproduct() {
        return (List<?>) orderRepository.getDistinctByUser_id();
    }
    // get list order by userId
    @GetMapping("/orders/{user_id}")
    public List<Order> getOrdersByUserID(@PathVariable("user_id") Integer user_id){
        List<Order> orders = orderRepository.getByuserId(user_id);
        return orders;
    }

    // delete Order By User Id
    @DeleteMapping("/orders/{user_id}")
    public ResponseEntity<?> deleteOrderByUserId(@PathVariable("user_id") Integer user_id){
        List<Order> orders = orderRepository.getByuserId(user_id);
        if (orders == null){
            throw new ResourceNotFoundException("user_id is not find : " + user_id);
        }
        orderRepository.deleteOrderByUser_id(user_id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("delete", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
