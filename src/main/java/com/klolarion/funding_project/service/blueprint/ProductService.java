package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> allProducts();
    Product getProduct(Long productId);

    boolean dispatchProduct(Long productId, int stock);

    boolean deleteProduct(Long productId);

}
