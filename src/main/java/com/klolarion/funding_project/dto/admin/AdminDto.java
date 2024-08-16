package com.klolarion.funding_project.dto.admin;

import com.klolarion.funding_project.domain.entity.CodeMaster;
import com.klolarion.funding_project.domain.entity.PaymentMethod;
import com.klolarion.funding_project.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    private List<Product> products;
    private List<CodeMaster> codes;
    private List<PaymentMethod> paymentMethods;
}
