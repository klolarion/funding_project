package com.klolarion.funding_project.controller.api;

import com.klolarion.funding_project.domain.entity.Funding;
import com.klolarion.funding_project.dto.funding.CreateFundingDto;
import com.klolarion.funding_project.dto.funding.FundingListDto;
import com.klolarion.funding_project.dto.funding.FundingMainDto;
import com.klolarion.funding_project.dto.funding.JoinFundingDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.GroupServiceImpl;
import com.klolarion.funding_project.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/f1/v1")
@Slf4j
public class FundingCreateApiControllerV1 {
    private final ProductServiceImpl productService;
    private final GroupServiceImpl groupService;
    private final FundingServiceImpl fundingService;


    @Operation(summary = "펀딩 메인",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 호출"),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            })
    @GetMapping("/funding")
    public ResponseEntity<?> fundingMain() {
        try {
            FundingMainDto fundingMainDto = new FundingMainDto(
                    productService.allProducts(),
                    groupService.myLeaderGroups()
            );
            return ResponseEntity.status(HttpStatus.OK).body(fundingMainDto);
        } catch (Exception e) {
            log.error("펀딩 메인페이지 호출 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("호출 실패");
        }
    }

    @Operation(summary = "펀딩 생성",
            responses = {
                    @ApiResponse(responseCode = "201", description = "펀딩 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "펀딩 생성 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            })
    @PostMapping("/funding/new")
    public ResponseEntity<?> createFunding(@RequestBody CreateFundingDto createFundingDto) {
        try {
            Funding createdFunding = fundingService.createFundingApi(createFundingDto.getProductId(), createFundingDto.getGroupId());
            if(createdFunding != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body("펀딩 생성 성공");
            }else {
                log.error("펀딩 생성 실패, Data -  ", createFundingDto);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펀딩 생성 실패");
            }
        } catch (Exception e) {
            log.error("펀딩 생성 실패, Data -  ", createFundingDto);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("펀딩 생성 실패");
        }
    }


    @Operation(summary = "펀딩 참여",
            responses = {
                    @ApiResponse(responseCode = "200", description = "펀딩 참여됨, 송금 정상"),
                    @ApiResponse(responseCode = "400", description = "펀딩 참여 실패, 송금 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            })
    @PostMapping("/funding")
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
