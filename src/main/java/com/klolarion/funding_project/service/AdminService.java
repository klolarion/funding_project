package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Funding;
import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.Payment;
import com.klolarion.funding_project.domain.entity.Product;

import java.util.List;

public interface AdminService {
    // payment
    List<Payment> getAllPayments();
    boolean changeComplete(Long paymentId, boolean completeStatus);

    // product
    Product searchProduct(Long productId);
    Product addProduct(String productName, Long price, int stock);
    Product addStock(Long productId, int stock);
    boolean setRestock(Long productId);
    boolean setSellFinished(Long productId);

    //funding
    Funding searchFunding(Long fundingId);
    boolean closeFunding(Long fundingId);
    boolean deleteFunding(Long fundingId);

    //member
    Member searchMember(Long id);
}
