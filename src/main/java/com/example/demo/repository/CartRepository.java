package com.example.demo.repository;

import com.example.demo.entity.BillDetails;
import com.example.demo.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository {
//    HashMap<Integer , CartItem> addCart(Integer id , HashMap<Integer , CartItem> cart);
//    HashMap<Integer , CartItem> deleteCart(Integer id , HashMap<Integer , CartItem> cart);
//    int totalQuantity(HashMap<Integer , CartItem> cart);
//    double totalPrice(HashMap<Integer , CartItem> cart);
//    @Query("select b from BillDetails b where b.product.id = ?1" +
//            " and b.user_id = ?2 ")
    List<BillDetails> addCartByUserIdAndProductId(Integer productId , Integer userId , int qty , double price);
    void updateQtyByCartId(int cartId,int qty,double price) throws Exception;
    List<BillDetails> getCartByUserId(int userId);
    List<BillDetails> removeCartByUserId(int cartId,int userId);
    List<BillDetails> removeAllCartByUserId(int userId);
    List<Order> getAllCheckoutByUserId(int userId);
    List<Order> saveProductsForCheckout(List<Order> tmp)  throws Exception;
    Boolean checkTotalAmountAgainstCart(double totalAmount,int userId);
}
