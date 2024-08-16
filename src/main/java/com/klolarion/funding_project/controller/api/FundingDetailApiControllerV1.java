package com.klolarion.funding_project.controller.api;

import com.klolarion.funding_project.dto.funding.FundingListDto;
import com.klolarion.funding_project.dto.funding.JoinFundingDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/f1/v1/funding")
@Slf4j
public class FundingDetailApiControllerV1 {
    private final FundingServiceImpl fundingService;


    @Operation(summary = "펀딩 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 호출"),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            })
    @GetMapping("/{fundingId}")
    public ResponseEntity<?> detail(@PathVariable Long fundingId) {
        try {
            FundingListDto fundingListDto = fundingService.fundingDetail(fundingId);
            return ResponseEntity.status(HttpStatus.OK).body(fundingListDto);
        } catch (Exception e) {
            log.error("펀딩 조회 실패, Data - ", fundingId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("호출 실패");
        }
    }

    @Operation(summary = "펀딩 참여",
            responses = {
                    @ApiResponse(responseCode = "200", description = "펀딩 참여됨, 송금 정상"),
                    @ApiResponse(responseCode = "400", description = "펀딩 참여 실패, 송금 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            })
    @PutMapping("/")
    public ResponseEntity<?> joinFunding(@RequestBody JoinFundingDto joinFundingDto) {
        try {
            boolean result = fundingService.joinFunding(joinFundingDto);

            if (result) {
                log.debug("펀딩 참여 성공, Data - ", joinFundingDto);
                return ResponseEntity.status(HttpStatus.OK).body("펀딩 참여 성공");
            }else {
                log.error("펀딩 참여 실패, Data -  ", joinFundingDto);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펀딩 참여 실패");
            }
        }catch (Exception e){
            log.error("펀딩 참여 실패, Data -  ", joinFundingDto);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }
}
