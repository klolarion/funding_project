package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.*;

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

    //paymentMethod
    PaymentMethod addPaymentMethod(int code, String paymentName, String accountNumber, Long availableAmount);
    boolean deletePaymentMethod(Long paymentMethodId);

    //code
    List<CodeMaster> getCodes();
    CodeMaster addCode(int code, String description, String reference);
    boolean deleteCode(Long codeId);

}
