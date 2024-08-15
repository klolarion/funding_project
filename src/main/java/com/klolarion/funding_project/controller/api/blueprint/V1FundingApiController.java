package com.klolarion.funding_project.controller.api.blueprint;

import com.klolarion.funding_project.dto.funding.CreateFundingDto;
import com.klolarion.funding_project.dto.funding.JoinFundingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface V1FundingApiController {
    @Operation(summary = "펀딩 메인",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 호출"),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            })
    @GetMapping("/funding")
    ResponseEntity<?> fundingMain();

    @Operation(summary = "펀딩 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "펀딩 생성됨"),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            })
    @GetMapping("/funding/{fundingId}")
    ResponseEntity<?> detail(@PathVariable Long fundingId);


    @Operation(summary = "펀딩 생성",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 호출"),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            })
    @PostMapping("/funding/new")
    ResponseEntity<?> createFunding(@RequestBody CreateFundingDto createFundingDto);


    @Operation(summary = "펀딩 참여",
            responses = {
                    @ApiResponse(responseCode = "200", description = "펀딩 참여됨, 송금 정상"),
                    @ApiResponse(responseCode = "400", description = "펀딩 참여 실패, 송금 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            })
    @PostMapping("/funding")
    ResponseEntity<?> joinFunding(@RequestBody JoinFundingDto joinFundingDto);
}
