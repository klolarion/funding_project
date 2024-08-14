package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.PaymentMethodDto;
import com.klolarion.funding_project.repository.CodeRepository;
import com.klolarion.funding_project.repository.PaymentMethodRepository;
import com.klolarion.funding_project.repository.PaymentRepository;
import com.klolarion.funding_project.repository.ProductRepository;
import com.klolarion.funding_project.service.blueprint.AdminService;
import com.klolarion.funding_project.util.CurrentMember;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AdminServiceImpl implements AdminService {
    private final PaymentRepository paymentRepository;
    private final CurrentMember currentMember;
    private final ProductRepository productRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final CodeRepository codeRepository;



    private final JPAQueryFactory query;
    private final EntityManager em;

    //payment
    @Override
    public List<Payment> getAllPayments() {
        QPayment qPayment = QPayment.payment;
        List<Payment> payments = query.selectFrom(qPayment).fetch();
        em.flush();
        em.close();
        return payments;
    }
    @Override
    public boolean changeComplete(Long paymentId, boolean completeStatus) {
        QPayment qPayment = QPayment.payment;
        long result = query.update(qPayment).set(qPayment.completed, true).where(qPayment.paymentId.eq(paymentId)).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    //product
    @Override
    public Product searchProduct(Long productId) {
        QProduct qProduct = QProduct.product;
        Product product = query.selectFrom(qProduct).where(qProduct.productId.eq(productId)).fetchOne();
        em.flush();
        em.clear();
        return product;
    }

    @Override
    public Product addProduct(String productName, Long price, int stock) {
        Product product = new Product(
                productName,
                price,
                stock
        );
        Product saved = productRepository.save(product);
        return saved;
    }

    /*동시성제어 필요*/
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Product addStock(Long productId, int stock){
        QProduct qProduct = QProduct.product;
        query.update(qProduct).set(qProduct.stock, qProduct.stock.add(stock)).execute();

        return null;
    }

    /*동시성제어 필요*/
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean setRestock(Long productId) {
        QProduct qProduct = QProduct.product;
        long result = query.update(qProduct).set(qProduct.restock, new CaseBuilder()
                .when(qProduct.restock.isTrue()).then(false)
                .otherwise(true)).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    /*동시성제어 필요*/
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean setSellFinished(Long productId) {
        QProduct qProduct = QProduct.product;
        long result = query.update(qProduct).set(qProduct.saleFinished,
                new CaseBuilder()
                        .when(qProduct.saleFinished.isTrue()).then(false)
                        .otherwise(true)).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    @Override
    public Funding searchFunding(Long fundingId) {
        QFunding qFunding = QFunding.funding;
        Funding funding = query.selectFrom(qFunding).where(qFunding.fundingId.eq(fundingId)).fetchOne();
        em.flush();
        em.clear();
        return funding;
    }

    /*동시성제어 필요*/
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean closeFunding(Long fundingId) {
        QFunding qFunding = QFunding.funding;
        long result = query.update(qFunding).set(qFunding.closed, true)
                .where(qFunding.fundingId.eq(fundingId)).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    /*동시성제어 필요*/
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean deleteFunding(Long fundingId) {
        QFunding qFunding = QFunding.funding;
        long result = query.update(qFunding).set(qFunding.deleted, true)
                .where(qFunding.fundingId.eq(fundingId)).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    @Override
    public Member searchMember(Long memberId) {
        QMember qMember = QMember.member;
        Member member = query.selectFrom(qMember).where(qMember.memberId.eq(memberId)).fetchOne();
        return member;
    }



    // paymentMethod

    @Override
    public PaymentMethod addPaymentMethod(PaymentMethodDto paymentMethodDto) {
        PaymentMethod paymentMethod = new PaymentMethod(
                paymentMethodDto.getCodeMaster().getCode(),
                paymentMethodDto.getCodeMaster().getDescription(),
                paymentMethodDto.getAccountNumber(),
                paymentMethodDto.getAvailableAmount());
        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);
        return saved;
    }

    @Override
    public boolean deletePaymentMethod(Long paymentMethodId) {
        QPaymentMethod qPaymentMethod = QPaymentMethod.paymentMethod;
        long result = query.delete(qPaymentMethod).where(qPaymentMethod.paymentMethodId.eq(paymentMethodId)).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    @Override
    public List<PaymentMethod> paymentMethodList() {
        QPaymentMethod qPaymentMethod = QPaymentMethod.paymentMethod;
        List<PaymentMethod> fetched = query.selectFrom(qPaymentMethod).fetch();
        em.flush();
        em.clear();
        return fetched;
    }

    // code

    @Override
    public List<CodeMaster> getCodes() {
        QCodeMaster qCodeMaster = QCodeMaster.codeMaster;
        List<CodeMaster> codes = query.selectFrom(qCodeMaster).fetch();
        em.flush();
        em.clear();
        return codes;
    }

    @Override
    public CodeMaster getCode(Long codeId) {
        QCodeMaster qCodeMaster = QCodeMaster.codeMaster;
        CodeMaster codeMaster = query.selectFrom(qCodeMaster).where(qCodeMaster.codeId.eq(codeId)).fetchOne();
        em.flush();
        em.clear();
        return codeMaster;
    }

    @Override
    public CodeMaster addCode(int code, String description, String reference) {
        CodeMaster codeMaster = new CodeMaster(
                code, description, reference
        );
        return codeRepository.save(codeMaster);

    }

    @Override
    public boolean deleteCode(Long codeId) {
        QCodeMaster qCodeMaster = QCodeMaster.codeMaster;
        long result = query.delete(qCodeMaster).where(qCodeMaster.codeId.eq(codeId)).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }
}
