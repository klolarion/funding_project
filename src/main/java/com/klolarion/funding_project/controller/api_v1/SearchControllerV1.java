package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.client.NaverShoppingSearch;
import com.klolarion.funding_project.dto.ProductSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/f1/v1/search")
@RequiredArgsConstructor
public class SearchControllerV1 {
    private final NaverShoppingSearch naverShoppingSearch;

    @GetMapping("/naver/{param}")
    public ResponseEntity<?> main(@PathVariable String param){
        List<ProductSearchDto> result = naverShoppingSearch.search(param);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
