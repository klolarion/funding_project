package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.dto.funding.FundingListDto;
import com.klolarion.funding_project.dto.funding.JoinFundingDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/f2/v1/funding")
@Slf4j
public class FundingDetailApiControllerV1 {
    private final FundingServiceImpl fundingService;


    @Operation(summary = "펀딩 조회",
            tags = {"펀딩 API"},
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
        try {
            FundingListDto fundingListDto = fundingService.fundingDetail(fundingId);
            return ResponseEntity.status(HttpStatus.OK).body(fundingListDto);
        } catch (Exception e) {
            log.error("펀딩 조회 실패(서버 오류), Data - ", fundingId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("호출 실패");
        }
    }

    @Operation(summary = "펀딩 참여",
            tags = {"펀딩 API"},
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

    @PutMapping("/")
    public ResponseEntity<?> joinFunding(@RequestBody JoinFundingDto joinFundingDto) {
        try {
            boolean result = fundingService.joinFunding(joinFundingDto);

            if (result) {
                log.debug("펀딩 참여 성공, Data - ", joinFundingDto);
                return ResponseEntity.status(HttpStatus.OK).body("펀딩 참여 성공");
            } else {
                log.debug("펀딩 참여 실패, Data -  ", joinFundingDto);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펀딩 참여 실패");
            }
        } catch (Exception e) {
            log.error("펀딩 참여 실패(서버 오류), Data -  ", joinFundingDto);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }
}
