package com.klolarion.funding_project.dto;

import com.klolarion.funding_project.domain.entity.CodeMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDto {
    private CodeMaster codeMaster;
    private String accountNumber;
    private Long availableAmount;

}
