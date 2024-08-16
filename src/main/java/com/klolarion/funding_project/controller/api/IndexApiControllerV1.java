package com.klolarion.funding_project.controller.api;

import com.klolarion.funding_project.dto.IndexDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.GroupServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/f1/v1")
@Slf4j
public class IndexApiControllerV1 {
    private final FundingServiceImpl fundingServiceImpl;
    private final GroupServiceImpl groupServiceImpl;

    @GetMapping
    public ResponseEntity<?> getIndex(){
        try {
            IndexDto indexDto = new IndexDto(
                    fundingServiceImpl.allFundingList(),
                    groupServiceImpl.allGroupExceptMy()
            );
            if(indexDto != null) {
                log.debug("정보 조회 성공 : Index");
                return ResponseEntity.status(HttpStatus.OK).body(indexDto);
            }else {
                log.debug("정보 조회 실패 : Index");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정보 조회 실패");
            }
        }catch (Exception e){
            log.error("서버 오류, ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }
}
