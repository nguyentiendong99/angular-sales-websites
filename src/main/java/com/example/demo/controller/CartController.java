package com.example.demo.controller;

import com.example.demo.config.ShoppingConfiguration;
import com.example.demo.config.request.ApiResponse;
import com.example.demo.entity.BillDetails;
import com.example.demo.repository.BillDetailsRepository;
import com.example.demo.service.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CartController {

    @Autowired
    private CartServiceImpl cartService;

//    //get cart
//    @GetMapping("/cart")
//    public ResponseEntity<?> Cart(HttpSession session) {
//        HashMap<Integer, CartItem> cart = (HashMap<Integer, CartItem>)
//                session.getAttribute("cart");
//        return ResponseEntity.ok(cart);
//    }
//
//    @PostMapping("addCart/{id}")
//    public ResponseEntity<?> AddCart(@RequestBody HttpSession session, @PathVariable("id") Integer id) {
//        HashMap<Integer, CartItem> cart = new HashMap<>();
//        if (cart == null) {
//            cart = new HashMap<>();
//        }
//        cart = cartService.addCart(id, cart);
//        session.setAttribute("cart", cart);
//        session.setAttribute("totalQuantityCart", cartService.totalQuantity(cart));
//        session.setAttribute("totalPrice", cartService.totalPrice(cart));
//        System.out.println(cart);
//        return ResponseEntity.ok(cart);
//    }



//    // deleteCart
//    @RequestMapping("/DeleteCart/{id}")
//    public ResponseEntity<?> DeleteCart(HttpSession session, @PathVariable("id") int id) {
//        HashMap<Integer, CartItem> cart = (HashMap<Integer, CartItem>) session.getAttribute("cart");
//        if (cart == null) {
//            cart = new HashMap<>();
//        }
//        cart = cartService.deleteCart(id, cart);
//        session.setAttribute("cart", cart);
//        session.setAttribute("totalQuantityCart", cartService.totalQuantity(cart));
//        session.setAttribute("totalPrice", cartService.totalPrice(cart));
//        return ResponseEntity.ok(cart);
//    }

    @Autowired
    BillDetailsRepository billDetailsRepository;
    @RequestMapping("addCart/addProduct")
    public ResponseEntity<?> addCart(@RequestBody HashMap<String , String> addCartRequest){
        try{
            String keys[] = {"product_id" , "user_id" ,"quantity" ,"price"};
            if (ShoppingConfiguration.validationWithHashMap(keys, addCartRequest)) {

            }
            Integer product_id = Integer.parseInt(addCartRequest.get("product_id"));
            Integer user_id =  Integer.parseInt(addCartRequest.get("user_id"));
          //  Integer bills_id =  Integer.parseInt(addCartRequest.get("bills_id"));
            int quantity =  Integer.parseInt(addCartRequest.get("quantity"));
            double price = Double.parseDouble(addCartRequest.get("price"));
            List<BillDetails> obj = cartService.addCartByUserIdAndProductId(product_id , user_id ,quantity,price);
            System.out.println(obj);
            return ResponseEntity.ok(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), ""));
        }
    }
    @RequestMapping("updateQtyForCart")
    public ResponseEntity<?> updateQtyForCart(@RequestBody HashMap<String,String> addCartRequest) {
        try {
            String keys[] = {"cart_id","user_id","quantity","price"};
            if(ShoppingConfiguration.validationWithHashMap(keys, addCartRequest)) {

            }
            int cartId = Integer.parseInt(addCartRequest.get("cart_id"));
            int userId =  Integer.parseInt(addCartRequest.get("user_id"));
            int qty =  Integer.parseInt(addCartRequest.get("quantity"));
            double price = Double.parseDouble(addCartRequest.get("price"));
            cartService.updateQtyByCartId(cartId, qty, price);
            List<BillDetails> obj = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), ""));
        }

    }


    @RequestMapping("removeProductFromCart")
    public ResponseEntity<?> removeCartwithProductId(@RequestBody HashMap<String,String> removeCartRequest) {
        try {
            String keys[] = {"user_id","cart_id"};
            if(ShoppingConfiguration.validationWithHashMap(keys, removeCartRequest)) {

            }
            List<BillDetails> obj = cartService.removeCartByUserId(Integer.parseInt(removeCartRequest.get("cart_id")), Integer.parseInt(removeCartRequest.get("user_id")));
            return ResponseEntity.ok(obj);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), ""));
        }
    }

    @RequestMapping("getCartsByUserId")
    public ResponseEntity<?> getCartsByUserId(@RequestBody HashMap<String,String> getCartRequest) {
        try {
            String keys[] = {"user_id"};
            if(ShoppingConfiguration.validationWithHashMap(keys, getCartRequest)) {
            }
            List<BillDetails> obj = cartService.getCartByUserId(Integer.parseInt(getCartRequest.get("user_id")));
            return ResponseEntity.ok(obj);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), ""));
        }
    }
}
