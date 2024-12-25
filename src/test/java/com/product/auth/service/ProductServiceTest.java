package com.product.auth.service;

import com.product.auth.entity.Product;
import com.product.auth.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService();
        productService.productRepository = productRepository; // Manual injection for the mock
    }

    @Test
    void testCreateProduct() {
        // Arrange
        Product product = new Product();
        product.setName("Test Product");
        product.setDesc("Test Description");
        product.setPrice(100.0);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product result = productService.createProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals("Test Description", result.getDesc());
        assertEquals(100.0, result.getPrice());
        verify(productRepository).save(product);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setName("Product 2");

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
        verify(productRepository).findAll();
    }

    @Test
    void testRemoveProduct() {
        // Arrange
        Long productId = 1L;

        doNothing().when(productRepository).deleteById(productId);

        // Act
        productService.removeProduct(productId);

        // Assert
        verify(productRepository).deleteById(productId);
    }

    @Test
    void testUpdateProduct() {
        // Arrange
        Long productId = 1L;

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Product");
        existingProduct.setDesc("Old Description");
        existingProduct.setPrice(50.0);

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setDesc("Updated Description");
        updatedProduct.setPrice(150.0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        // Act
        Product result = productService.updateProduct(productId, updatedProduct);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Description", result.getDesc());
        assertEquals(150.0, result.getPrice());
        verify(productRepository).findById(productId);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void testUpdateProductNotFound() {
        // Arrange
        Long productId = 1L;
        Product updatedProduct = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(productId, updatedProduct);
        });

        assertEquals("Product Not Found", exception.getMessage());
        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }
}
