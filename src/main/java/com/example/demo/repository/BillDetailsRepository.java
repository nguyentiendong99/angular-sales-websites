package com.example.demo.repository;

import com.example.demo.entity.BillDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillDetailsRepository extends JpaRepository<BillDetails, Integer> {
    @Query("select b from BillDetails b where b.user_id =?1")
    List<BillDetails> getCartByUserId(@Param("user_id") Integer user_id);

    @Query("Select a  FROM BillDetails a WHERE a.product.id = :product_id and a.user_id=:user_id")
    Optional<BillDetails> getCartByProductIdAnduserId(@Param("user_id") Integer user_id, @Param("product_id") Integer product_id);
    @Query("Select sum(b.price) FROM BillDetails b WHERE b.user_id=:user_id")
    double getTotalAmountByUserId(@Param("user_id")Integer user_id);
    //TODO : update delete
    @Modifying
    @Transactional
    @Query("DELETE  FROM BillDetails b WHERE b.id =:cart_id   and b.user_id=:user_id")
    void deleteCartByIdAndUserId(@Param("user_id")Integer user_id,@Param("cart_id")Integer cart_id);
    @Modifying
    @Transactional
    @Query("DELETE  FROM BillDetails b WHERE   b.user_id=:user_id")
    void deleteAllCartByUserId(@Param("user_id")Integer user_id);

    @Modifying
    @Transactional
    @Query("DELETE  FROM BillDetails b WHERE b.user_id=:user_id")
    void deleteAllCartUserId(@Param("user_id")Integer user_id);
    @Modifying
    @Transactional
    @Query("update BillDetails b set b.quantity=:qty,b.price=:price WHERE b.id=:cart_id")
    void updateQtyByCartId(@Param("cart_id")Integer cart_id ,@Param("qty")Integer qty ,@Param("price") Double price);

}
