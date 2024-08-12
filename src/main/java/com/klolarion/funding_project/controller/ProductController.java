package com.klolarion.funding_project.controller;

import org.springframework.http.ResponseEntity;

/**
 * 상품 관련 기능
 * 윤효정
 * */

public interface ProductController {
    ResponseEntity<?> allProducts();
    ResponseEntity<?> searchProduct();
    ResponseEntity<?> productInfo();
}
