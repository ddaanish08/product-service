package com.product.auth.service;

import com.product.auth.entity.Product;
import com.product.auth.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void removeProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));
        existingProduct.setName(product.getName());
        existingProduct.setDesc(product.getDesc());
        existingProduct.setPrice(product.getPrice());
        return productRepository.save(existingProduct);
    }
}
