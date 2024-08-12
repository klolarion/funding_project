package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Payment;
import com.klolarion.funding_project.domain.entity.Product;
import com.klolarion.funding_project.domain.entity.QProduct;
import com.klolarion.funding_project.repository.PaymentRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final PaymentRepository paymentRepository;


    private final JPAQueryFactory query;
    private final EntityManager em;

    //payment
    @Override
    public List<Payment> getAllPayments() {
        return null;
    }
    @Override
    public boolean changeComplete(Long paymentId, boolean completeStatus) {
        return false;
    }

    @Override
    public Product addProduct(String productName, Long price, int stock) {
        return null;
    }

    //product
    public Product addStock(Long productId, int stock){
        QProduct qProduct = QProduct.product;
        query.update(qProduct).set(qProduct.stock, qProduct.stock.add(stock)).execute();

        em.flush();
        em.clear();
        return null;
    }
    @Override
    public boolean setRestock(Long productId) {
        return false;
    }
    @Override
    public boolean setSellFinished(Long productId) {
        return false;
    }

    @Override
    public boolean closeFunding(Long fundingId) {
        return false;
    }

    @Override
    public boolean deleteFunding(Long fundingId) {
        return false;
    }
}
