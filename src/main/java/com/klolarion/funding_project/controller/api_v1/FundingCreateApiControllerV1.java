package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.domain.entity.Funding;
import com.klolarion.funding_project.dto.funding.CreateFundingDto;
import com.klolarion.funding_project.dto.funding.FundingMainDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.GroupServiceImpl;
import com.klolarion.funding_project.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/f2/v1/funding")
@Slf4j
public class FundingCreateApiControllerV1 {
    private final ProductServiceImpl productService;
    private final GroupServiceImpl groupService;
    private final FundingServiceImpl fundingService;


    @Operation(summary = "펀딩 메인",
            tags = {"펀딩 API - V1"},
            description = "모든 상품과 내가 그룹장인 모든 펀딩 목록 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 호출",
                            content = @Content(
                                    schema = @Schema(implementation = FundingMainDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(
                                    mediaType = "application/json")),
            })
    @GetMapping("/")
    public ResponseEntity<?> fundingMain() {
        try {
            FundingMainDto fundingMainDto = new FundingMainDto(
                    productService.allProducts(),
                    groupService.myLeaderGroups()
            );
            return ResponseEntity.status(HttpStatus.OK).body(fundingMainDto);
        } catch (Exception e) {
            log.error("펀딩 메인페이지 호출 실패(서버 오류)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("호출 실패");
        }
    }

    @Operation(summary = "펀딩 생성",
            tags = {"펀딩 API - V1"},
            description = "펀딩, 그룹, 분류 ID로 신규 펀딩 생성",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "펀딩 생성 요청 Dto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateFundingDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "펀딩 생성 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "펀딩 생성 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @PostMapping("/")
    public ResponseEntity<?> createFunding(@RequestBody CreateFundingDto createFundingDto) {
        try {
            Funding createdFunding = fundingService.createFundingApi(createFundingDto.getProductId(), createFundingDto.getGroupId(), createFundingDto.getFundingCategoryCode());
            if(createdFunding != null) {
                log.debug("펀딩 생성 성공, Data -  ", createFundingDto);
                return ResponseEntity.status(HttpStatus.CREATED).body("펀딩 생성 성공");
            }else {
                log.debug("펀딩 생성 실패, Data -  ", createFundingDto);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펀딩 생성 실패");
            }
        } catch (Exception e) {
            log.error("펀딩 생성 실패(서버 오류), Data -  ", createFundingDto, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("펀딩 생성 실패");
        }
    }

}
