package com.klolarion.funding_project.infrastructure.persistence;

import com.klolarion.funding_project.domain.entity.Product;
import com.klolarion.funding_project.application.port.out.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    @Override
    public List<Product> productList() {
        return null;
    }

    @Override
    public boolean exportProduct(Long productId) {
        return false;
    }

    @Override
    public boolean addStock(Long productId, int amount) {
        return false;
    }

    @Override
    public boolean setRestock(Long productId) {
        return false;
    }

    @Override
    public boolean setSellFinished(Long productId) {
        return false;
    }


}
