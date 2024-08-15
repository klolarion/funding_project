package com.klolarion.funding_project.dto.funding;

import com.klolarion.funding_project.domain.entity.Product;
import com.klolarion.funding_project.dto.group.GroupDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundingMainDto {
    private List<Product> productList;
    private List<GroupDto> groupDtoList;
}
