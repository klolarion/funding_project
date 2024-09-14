package com.klolarion.funding_project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSearchDto {
    private String title;
    private String link;
    private String image;
    private String lprice;
    private String mallname;
    private String productId;
    private String productType;
    private String category1;
    private String category2;
    private String category3;
}
