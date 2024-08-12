package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Product;
import com.klolarion.funding_project.domain.entity.QProduct;
import com.klolarion.funding_project.repository.ProductRepository;
import com.klolarion.funding_project.service.blueprint.ProductService;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final EntityManager em;
    private final JPAQueryFactory query;


    @Override
    public List<Product> allProducts() {
        QProduct qProduct = QProduct.product;
        List<Product> products = query.selectFrom(qProduct).fetch();
        em.flush();
        em.clear();
        return products;
    }

    @Override
    public Product getProduct(Long productId) {
        QProduct qProduct = QProduct.product;
        Product product = query.selectFrom(qProduct).where(qProduct.productId.eq(productId)).fetchOne();
        em.flush();
        em.clear();
        return product;

    }



    @Override
    public boolean dispatchProduct(Long productId, int stock) {
        QProduct qProduct = QProduct.product;
        long result = query.update(qProduct)
                .set(qProduct.stock, new CaseBuilder()
                        .when(qProduct.stock.subtract(stock).gt(0)).then(qProduct.stock.subtract(stock))
                        .otherwise(qProduct.stock))
                .where(qProduct.productId.eq(productId)).execute();

        em.flush();
        em.clear();
        return result == 1L;
    }


}
