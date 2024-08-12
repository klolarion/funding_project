package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> allProducts();
    Product getProduct(Long productId);
    Product addProduct(String productName, Long price, int stock);
    Product addStock(Long productId, int stock);
    boolean dispatchProduct(Long productId, int stock);
    boolean setRestock(Long productId);
    boolean setSellFinished(Long productId);
}
