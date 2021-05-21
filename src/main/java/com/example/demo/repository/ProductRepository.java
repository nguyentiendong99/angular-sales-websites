package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Repository
@CrossOrigin("http://localhost:4200")
public interface ProductRepository extends PagingAndSortingRepository<Product , Integer> {
    @Query(value = "select c from Product c where c.new_product = true order by rand()")
    List<Product> getProductByNew_product(Product product);

}
