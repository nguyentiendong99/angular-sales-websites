package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductPage;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Service
@CrossOrigin("http://localhost:4200")
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
//    public Page<Product> ListPage(int pageNumber){
//        Sort sort = Sort.by("price").ascending();
//        Pageable pageable = PageRequest.of(pageNumber - 1 , 6 , sort);
//        return productRepository.findAll(pageable);
//    }
    public Page<Product> getProducts(ProductPage productPage){
        Integer currentPage = productPage.getCurrentPage();
        Sort sort = Sort.by(productPage.getSortDirection() , productPage.getSortBy());
        Pageable pageable = PageRequest.of(productPage.getPageNumber() ,
                productPage.getPageSize() , sort );
        return  productRepository.findAll(pageable);
    }
    public Product getProductById(Integer id){
        return productRepository.findById(id).get();
    }

    public List<Product> getNewProduct(Product product){
        return productRepository.getProductByNew_product(product);
    }
}
