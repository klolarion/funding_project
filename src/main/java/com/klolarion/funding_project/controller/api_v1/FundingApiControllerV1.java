package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.domain.entity.Funding;
import com.klolarion.funding_project.dto.funding.CreateFundingDto;
import com.klolarion.funding_project.dto.funding.FundingListDto;
import com.klolarion.funding_project.dto.funding.FundingMainDto;
import com.klolarion.funding_project.dto.funding.JoinFundingDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.GroupServiceImpl;
import com.klolarion.funding_project.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/f2/v1/funding")
@Slf4j
public class FundingApiControllerV1 {
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
        FundingMainDto fundingMainDto = new FundingMainDto(
                productService.allProducts(),
                groupService.myLeaderGroups()
        );
        return ResponseEntity.status(HttpStatus.OK).body(fundingMainDto);
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

        Funding createdFunding = fundingService.createFundingApi(createFundingDto.getProductId(), createFundingDto.getTravelId(), createFundingDto.getGroupId());
        if (createdFunding != null) {
            log.debug("펀딩 생성 성공, Data -  ", createFundingDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("펀딩 생성 성공");
        } else {
            log.debug("펀딩 생성 실패, Data -  ", createFundingDto);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펀딩 생성 실패");
        }
    }

    @Operation(summary = "펀딩 조회",
            tags = {"펀딩 API - V1"},
            description = "펀딩 ID로 특정 펀딩 정보를 조회",
            parameters = {
                    @Parameter(name = "fundingId", description = "조회할 펀딩의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 호출",
                            content = @Content(
                                    schema = @Schema(implementation = FundingListDto.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/{fundingId}")
    public ResponseEntity<?> detail(@PathVariable Long fundingId) {
        FundingListDto fundingListDto = fundingService.fundingDetail(fundingId);
        return ResponseEntity.status(HttpStatus.OK).body(fundingListDto);
    }

    @Operation(summary = "펀딩 참여",
            tags = {"펀딩 API - V1"},
            description = "펀딩 계좌로 송금",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "펀딩 참여 요청 Dto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = JoinFundingDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "펀딩 참여됨, 송금 정상", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "펀딩 참여 실패, 송금 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @PutMapping
    public ResponseEntity<?> joinFunding(@RequestBody JoinFundingDto joinFundingDto) {

        boolean result = fundingService.joinFunding(joinFundingDto);

        if (result) {
            log.debug("펀딩 참여 성공, Data - ", joinFundingDto);
            return ResponseEntity.status(HttpStatus.OK).body("펀딩 참여 성공");
        } else {
            log.debug("펀딩 참여 실패, Data -  ", joinFundingDto);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펀딩 참여 실패");
        }
    }


    @Operation(summary = "펀딩 검색",
            tags = {"펀딩 API - V1"},
            description = "펀딩 주최자, 그룹의 이름 그리고 카데고리 코드로 펀딩 검색",
            parameters = {
                    @Parameter(name = "searchParam", description = "조회할 펀딩의 검색어", required = true, in = ParameterIn.PATH),
                    @Parameter(name = "fundingCategoryCode", description = "조회할 펀딩의 카데고리 코드", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "펀딩 검색 성공", content = @Content(
                            schema = @Schema(implementation = FundingListDto.class),
                            mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "펀딩 검색 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/{searchParam}")
    public ResponseEntity<?> searchFunding(
            @PathVariable String searchParam,
            @RequestParam(required = false) String fundingCategoryCode) {

        List<FundingListDto> fundingListDtos = fundingService.searchFunding(searchParam, Integer.parseInt(fundingCategoryCode));
        if (fundingListDtos != null) {
            return ResponseEntity.status(HttpStatus.OK).body(fundingListDtos);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펀딩 검색 실패");

    }
}
