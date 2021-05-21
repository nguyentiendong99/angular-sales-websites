package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductPage;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ProductService service;

    @GetMapping("/allProducts")
    public List<Product> getProductsAlls() {
        return (List<Product>) repository.findAll();
    }
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getAllproduct(ProductPage productPage) {
        return new ResponseEntity<>(service.getProducts(productPage) , HttpStatus.OK);
    }
    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        Product newProduct = repository.save(product);
        LOGGER.info("created " + newProduct);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    // get product by id
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductByID(@PathVariable("id") int id) {
        Product product = repository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Product could not find : " + id));
        return ResponseEntity.ok(product);
    }

    @GetMapping("/new-product")
    public List<Product> getNewProduct(Product product) {
        List<Product> products = service.getNewProduct(product);
        return products;
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") int id,
                                                 @RequestBody Product productDetails) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product by id : " + id));
        LOGGER.info(product + " is update ");
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setTitle(productDetails.getTitle());
        product.setDetails(productDetails.getDetails());
        Product updatedProduct = repository.save(product);
        LOGGER.info("Updated to " + updatedProduct);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product is not exist with id : " + id));
        repository.deleteById(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("delete", Boolean.TRUE);
        LOGGER.info(" Deleted  : " + product);
        return ResponseEntity.ok(response);
    }
}
